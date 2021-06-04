package it.adriano.tumino.gamepoint.backgroundprocesses;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class SearchOnMicrosoft extends TaskRunner<String, ArrayList<GameSearchResult>> {
    public static final String TAG = "SearchOnMicrosoft";

    private final static String STORE = "MCS";
    private final static String URL = "https://www.microsoft.com/en-us/search/shop/games?q=";

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    public SearchOnMicrosoft() {
    }

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... input) {
        Log.i(TAG, "Ricerca gioco su MCS");

        String name = input[0].toLowerCase();
        String encodedName = name.replaceAll(" ", "+");
        String url = URL + encodedName;

        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36") //per visualizzare tutto correttamente
                    .get();
        } catch (IOException exception) {
            Log.e(TAG, "Non è stato possibile prelevare la pagina web");
            return null;
        }

        Elements grid = document.getElementsByClass("context-list-page");

        if (grid == null || grid.isEmpty()) {
            Log.i(TAG, "Nessun valore trovato");
            return null;
        }

        ArrayList<GameSearchResult> listOfGames = new ArrayList<>();
        Elements placementItems = grid.get(0).getElementsByClass("m-channel-placement-item");
        for (int j = 0; j < placementItems.size(); j++) {
            Element game = placementItems.get(j);
            Element gameUrl = game.getElementsByTag("a").first();
            Element titlediv = gameUrl.getElementsByClass("c-subheading-6").first();
            String title = titlediv.text();
            if (title.toLowerCase().contains(name)) {
                String gameID;
                try {
                    gameID = new JSONObject(gameUrl.attributes().get("data-m")).getString("pid");
                } catch (JSONException exception) {
                    Log.e(TAG, exception.toString());
                    return null;
                }

                Element image = gameUrl.getElementsByTag("source").first();
                String imageURl = null;
                if (image != null) {
                    imageURl = image.attributes().get("data-srcset"); //decodifica in automatico
                }

                GameSearchResult gameSearchResult = new GameSearchResult(title, imageURl, null, gameID, null, STORE);
                listOfGames.add(gameSearchResult);
            }
        }
        return listOfGames;
    }

    @Override
    public void onPostExecute(ArrayList<GameSearchResult> microsoftList) {
        if (microsoftList != null) delegate.processFinish(microsoftList);
    }
}
