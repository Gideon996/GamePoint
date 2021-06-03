package it.adriano.tumino.gamepoint.backgroundprocesses;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.data.GameShow;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class SearchOnSteam extends TaskRunner<String, ArrayList<GameSearchResult>> {
    public final String TAG = getClass().getSimpleName();


    private final static String STORE = "STEAM";
    private final static String BASE_URL = "https://store.steampowered.com/search/?term=";
    private ArrayList<GameSearchResult> listOfGame = new ArrayList<>();
    private String name;

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    public SearchOnSteam() {
        //this.name =
    }

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... i) {
        String url = BASE_URL + i[0].toLowerCase(Locale.ROOT).replaceAll(" ", "+");
        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .get();
        } catch (IOException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }

        if (document == null) {//controllo per sicurezza, ma se è null allora ho ritornato dal catch
            return null;
        }

        Element resultsRows = document.getElementById("search_resultsRows");
        if (resultsRows == null || resultsRows.getAllElements().isEmpty()) { //nessun risultato
            return null;
        }

        //se arrivo qui allora sicuramente è presente almeno un elemento
        Elements links = resultsRows.getElementsByTag("a");
        for (Element link : links) {
            String appid = link.attributes().get("data-ds-appid");
            String imgUrl = link.getElementsByTag("img").get(0).attributes().get("src");
            String titolo = link.getElementsByClass("title").get(0).text();
            String piattaforme = getPlatform(link.getElementsByClass("platform_img"));
            String releaseData = link.getElementsByClass("search_released").get(0).text();
            GameSearchResult result = new GameSearchResult(titolo, imgUrl, null, appid, piattaforme, STORE);
            listOfGame.add(result);
        }

        return listOfGame;
    }

    private String getPlatform(Elements platforms) {
        String piattaforme = "";
        for (Element platform : platforms) {
            switch (platform.attributes().get("class")) {
                case "platform_img win":
                    piattaforme += "WIN ";
                    break;
                case "platform_img mac":
                    piattaforme += "MAC ";
                    break;
                case "platform_img linux":
                    piattaforme += "LIN ";
                    break;
            }
        }
        return piattaforme;
    }

    @Override
    public void onPostExecute(ArrayList<GameSearchResult> steamList) {
        delegate.processFinish(steamList);
    }
}
