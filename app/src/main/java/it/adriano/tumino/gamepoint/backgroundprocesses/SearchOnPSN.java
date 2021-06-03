package it.adriano.tumino.gamepoint.backgroundprocesses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class SearchOnPSN extends TaskRunner<String, ArrayList<GameSearchResult>> {
    public final String TAG = getClass().getSimpleName();

    private final static String STORE = "PSN";
    private final static String BASE_URL = "https://store.steampowered.com/search/?term=";
    private ArrayList<GameSearchResult> listOfGame = new ArrayList<>();

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    public SearchOnPSN(){

    }

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... i) {
        String name = i[0];
        String nameForUrl = name.replaceAll(" ", "_");
        try {
            nameForUrl = URLEncoder.encode(nameForUrl, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        String urlAPI = "https://store.playstation.com/store/api/chihiro/00_09_000/tumbler/IT/it/999/"+nameForUrl+"?suggested_size=100&mode=game";
        String json = null;
        try {
            json = getJSONFromUrl(urlAPI);
            JSONObject result = new JSONObject(json);
            int size = result.optInt("size");
            if (size == 0) return null;

            JSONArray links = result.optJSONArray("links");
            if (links == null || links.length() == 0) return null;

            for (int j = 0; j < links.length(); j++) {
                JSONObject gameInfomation = links.optJSONObject(j);
                if (gameInfomation == null || gameInfomation.length() == 0) continue;
                String titleGame = gameInfomation.optString("name");
                if(titleGame.toLowerCase().contains(name)){
                    //images[{}]
                    String imageURL = gameInfomation.optJSONArray("images").optJSONObject(0).optString("url");
                    //name

                    //playable_platform[]
                    String console = gameInfomation.optJSONArray("playable_platform").join(", ").replaceAll("\"", "");
                    //provider_name
                    String publisher = gameInfomation.optString("provider_name");
                    //release_date
                    String releaseData = gameInfomation.optString("release_date");
                    //url
                    String gameURL = gameInfomation.optString("url");

                    GameSearchResult gameSearchResult = new GameSearchResult(titleGame, imageURL, gameURL, null, console, STORE);
                    listOfGame.add(gameSearchResult);
                }
            }
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
            return null;
        }
        return listOfGame;
    }

    private static String getJSONFromUrl(String url) throws IOException {
        String jsonText = "";
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            jsonText = sb.toString();
        }
        return jsonText;
    }

    @Override
    public void onPostExecute(ArrayList<GameSearchResult> o) {
        delegate.processFinish(o);
    }
}
