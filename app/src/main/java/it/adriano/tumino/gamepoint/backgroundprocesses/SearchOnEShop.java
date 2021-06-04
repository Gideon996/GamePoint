package it.adriano.tumino.gamepoint.backgroundprocesses;

import android.util.Log;

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
    public static final String TAG = "SearchOnEshop";

    private final static String STORE = "ESHOP";
    private final static String FIRST_PIECE_URL = "https://search.nintendo-europe.com/it/select?q=";
    private final static String SECOND_PIECE_URL = "&fq=type%3A*%20AND%20*%3A*&start=0&rows=24&wt=json&group=true&group.field=pg_s&group.limit=100&group.sort=score%20desc,%20date_from%20desc&sort=score%20desc,%20date_from%20desc";

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    public SearchOnEShop() {
    }

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... input) {
        Log.i(TAG, "Ricerca gioco su EShop");

        String name = input[0].toLowerCase();
        String encodedName;
        try {
            encodedName = URLEncoder.encode(name, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Impossibile codificare il nome del gioco");
            return null;
        }

        String url = FIRST_PIECE_URL + encodedName + SECOND_PIECE_URL;
        try {
            String json = getJSONFromUrl(url);
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject("grouped").getJSONObject("pg_s");
            int matches = result.getInt("matches");
            if (matches == 0) {
                Log.i(TAG, "Nessun elemento per quel nome trovato");
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
                ArrayList<GameSearchResult> listOfGames = new ArrayList<>();
                for (int j = 0; j < games.length(); j++) {
                    JSONObject object = games.optJSONObject(j);
                    if (object == null) continue;
                    String title = object.optString("title");
                    if (title.toLowerCase().contains(name)) {
                        String gameUrl = object.optString("url");
                        String releaseData = object.getJSONArray("dates_released_dts").getString(0);
                        String imageUrl = object.optString("image_url");

                        JSONArray consoleArray = object.getJSONArray("system_names_txt");
                        StringBuilder console = new StringBuilder();
                        for (int k = 0; k < consoleArray.length(); k++) {
                            console.append(consoleArray.optString(j));
                        }
                        GameSearchResult gameSearchResult = new GameSearchResult(title, imageUrl, gameUrl, null, console.toString(), STORE);
                        listOfGames.add(gameSearchResult);
                    }
                }
                return listOfGames;
            } else {
                Log.i(TAG, "Non ci sono giochi con quel nome");
                return null;
            }
        } catch (IOException | JSONException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }

    }

    private static String getJSONFromUrl(String url) throws IOException {
        String jsonText;
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
    public void onPostExecute(ArrayList<GameSearchResult> nintendoList) {
        if (nintendoList != null) delegate.processFinish(nintendoList);
    }
}
