package it.adriano.tumino.gamepoint.backgroundprocesses.searchgame;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.backgroundprocesses.TaskRunner;

public class SearchOnMicrosoft extends TaskRunner<String, ArrayList<BasicGameInformation>> {
    public static final String TAG = "SearchOnMicrosoft";

    private final static String STORE = "MCS";
    private final static String URL = "https://www.microsoft.com/it-it/search/shop/games?q=";

    public AsyncResponse<ArrayList<BasicGameInformation>> delegate = null;

    public SearchOnMicrosoft() {
    }

    @Override
    public ArrayList<BasicGameInformation> doInBackground(String... input) {
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

        ArrayList<BasicGameInformation> listOfGames = new ArrayList<>();
        Elements placementItems = grid.get(0).getElementsByClass("m-channel-placement-item");
        for (int j = 0; j < placementItems.size(); j++) {
            Element game = placementItems.get(j);
            Element gameUrl = game.getElementsByTag("a").first();
            String gameUrl1 = "https://www.microsoft.com" + gameUrl.attributes().get("href");
            Element titlediv = gameUrl.getElementsByClass("c-subheading-6").first();
            String title = titlediv.text();
            String verify = Normalizer.normalize(title, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            if (verify.toLowerCase().contains(name)) {
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

                BasicGameInformation basicGameInformation = new BasicGameInformation(title, imageURl, gameUrl1, gameID, null, STORE, "N.A.");
                listOfGames.add(basicGameInformation);
            }
        }
        return listOfGames;
    }

    @Override
    public void onPostExecute(ArrayList<BasicGameInformation> microsoftList) {
        if (microsoftList != null) delegate.processFinish(microsoftList);
    }
}
