package it.adriano.tumino.gamepoint.backgroundprocesses.searchgame;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class SearchOnSteam extends TaskRunner<String, ArrayList<GameSearchResult>> {
    public static final String TAG = "SearchOnSteam";

    private final static String STORE = "STEAM";
    private final static String URL = "https://store.steampowered.com/search/?term=";

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    public SearchOnSteam() {
    }

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... input) {
        Log.i(TAG, "Ricerca gioco su STEAM");
        String name = input[0].toLowerCase();

        String url = URL + name.replaceAll(" ", "+");
        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .get();
        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }

        Element resultsRows = document.getElementById("search_resultsRows");
        if (resultsRows == null || resultsRows.getAllElements().isEmpty()) {
            Log.i(TAG, "Nessun gioco trovato");
            return null;
        }

        ArrayList<GameSearchResult> listOfGames = new ArrayList<>();
        Elements links = resultsRows.getElementsByTag("a");
        for (Element link : links) {
            String title = link.getElementsByClass("title").get(0).text();
            String verify = Normalizer.normalize(title, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            if (verify.toLowerCase().contains(name)) {
                String gameID = link.attributes().get("data-ds-appid");
                String imgUrl = link.getElementsByTag("img").get(0).attributes().get("src");
                String platfoms = getPlatform(link.getElementsByClass("platform_img"));
                String releaseData = link.getElementsByClass("search_released").get(0).text();
                String price = link.getElementsByClass("search_price").get(0).text();
                GameSearchResult result = new GameSearchResult(title, imgUrl, null, gameID, platfoms, STORE, price);
                listOfGames.add(result);
            }
        }

        return listOfGames;
    }

    private String getPlatform(Elements platforms) {
        StringBuilder piattaforme = new StringBuilder();
        for (Element platform : platforms) {
            switch (platform.attributes().get("class")) {
                case "platform_img win":
                    piattaforme.append("WIN ");
                    break;
                case "platform_img mac":
                    piattaforme.append("MAC ");
                    break;
                case "platform_img linux":
                    piattaforme.append("LIN ");
                    break;
            }
        }
        return piattaforme.toString();
    }

    @Override
    public void onPostExecute(ArrayList<GameSearchResult> steamList) {
        if (steamList != null) delegate.processFinish(steamList);
    }
}
