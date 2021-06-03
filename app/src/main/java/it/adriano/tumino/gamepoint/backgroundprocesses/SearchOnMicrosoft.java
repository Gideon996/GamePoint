package it.adriano.tumino.gamepoint.backgroundprocesses;

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

    public final String TAG = getClass().getSimpleName();

    private final static String STORE = "MCS";
    private final static String BASE_URL = "https://store.steampowered.com/search/?term=";
    private ArrayList<GameSearchResult> listOfGame = new ArrayList<>();

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    public SearchOnMicrosoft(){}

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... i) {
        String name = i[0].toLowerCase();
        String nameUrl = name.replaceAll(" ", "+");
        String url = "https://www.microsoft.com/en-us/search/shop/games?q=";
        String finalUrl = url + nameUrl;

        Document document = null;
        try {
            document = Jsoup.connect(finalUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36") //per visualizzare tutto correttamente
                    .get();
        } catch (IOException exception) {
            return null;
        }
        Elements grid = document.getElementsByClass("context-list-page");

        if (grid == null || grid.isEmpty()) return null;

        Elements listOfGames = grid.get(0).getElementsByClass("m-channel-placement-item");
        for (int j = 0; j < listOfGames.size(); j++) {
            Element game = listOfGames.get(j);
            Element gameUrl = game.getElementsByTag("a").first();
            Element titlediv = gameUrl.getElementsByClass("c-subheading-6").first();
            String title = titlediv.text();
            if(title.toLowerCase().contains(name)){
                String gameID = null;
                try {
                    gameID = new JSONObject(gameUrl.attributes().get("data-m")).getString("pid");
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
                Element image = gameUrl.getElementsByTag("source").first();
                String imageURl = null;
                if (image != null) imageURl = image.attributes().get("data-srcset"); //decodifica in automatico

                GameSearchResult gameSearchResult = new GameSearchResult(title, imageURl, null, gameID, null, STORE);
                listOfGame.add(gameSearchResult);
            }
        }
        return listOfGame;
    }

    @Override
    public void onPostExecute(ArrayList<GameSearchResult> o) {
        if(o != null) delegate.processFinish(o);
        //altrimenti non ho trovato nulla o si è generato un errore
    }
}
