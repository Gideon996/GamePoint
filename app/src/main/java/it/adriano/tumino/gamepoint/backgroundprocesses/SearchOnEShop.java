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

public class SearchOnEShop extends TaskRunner<String, ArrayList<GameSearchResult>> {
    private final static String STORE = "ESHOP";
    private final static String BASE_URL = "https://search.nintendo-europe.com/it/select?q=";
    private final static String SECONDO_PEZZO = "&fq=type%3A*%20AND%20*%3A*&start=0&rows=24&wt=json&group=true&group.field=pg_s&group.limit=100&group.sort=score%20desc,%20date_from%20desc&sort=score%20desc,%20date_from%20desc";
    private ArrayList<GameSearchResult> listOfGame = new ArrayList<>();

    public SearchOnEShop() {

    }

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... i) {
        String name = i[0].toLowerCase();
        try {
            name = URLEncoder.encode(name, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        String url = BASE_URL + name + SECONDO_PEZZO;
        try {
            String json = getJSONFromUrl(url);
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject("grouped").getJSONObject("pg_s");
            int matches = result.getInt("matches");
            if (matches == 0) {
                System.out.println("nessun valore");
                return null;
            }
            JSONArray groups = result.getJSONArray("groups");
            JSONArray games = null;
            for (int j = 0; j < groups.length(); j++) {
                JSONObject object = (JSONObject) groups.get(j);
                if (object.getString("groupValue").equals("GAME")) {
                    games = object.getJSONObject("doclist").getJSONArray("docs");
                }
            }

            if (games != null) {
                for (int j = 0; j < games.length(); j++) {
                    JSONObject object = games.optJSONObject(j);
                    if (object == null) return null;
                    String title = object.optString("title");
                    if (title.toLowerCase().contains(name)) {
                        String gameUrl = object.optString("url");
                        String data = object.optJSONArray("dates_released_dts").getString(0);
                        String imageUrl = object.optString("image_url");

                        JSONArray consoleArray = object.optJSONArray("system_names_txt");
                        String console = "";
                        for (int k = 0; k < consoleArray.length(); k++) {
                            console += consoleArray.optString(j);
                        }
                        GameSearchResult gameSearchResult = new GameSearchResult(title, imageUrl, gameUrl, null, console, STORE);
                        listOfGame.add(gameSearchResult);
                    }
                }
            }
        } catch (IOException | JSONException exception) {
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
