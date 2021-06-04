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

public class SearchOnPSN extends TaskRunner<String, ArrayList<GameSearchResult>> {
    public static final String TAG = "SearchOnPSN";

    private final static String STORE = "PSN";
    private final static String FIRST_PIECE_URL = "https://store.playstation.com/store/api/chihiro/00_09_000/tumbler/IT/it/999/";
    private final static String SECOND_PIECE_URL = "?suggested_size=100&mode=game";

    public AsyncResponse<ArrayList<GameSearchResult>> delegate = null;

    public SearchOnPSN() {

    }

    @Override
    public ArrayList<GameSearchResult> doInBackground(String... input) {
        Log.i(TAG, "Ricerca gioco su PSN");

        String name = input[0];
        String encodedName = name.replaceAll(" ", "_");
        try {
            encodedName = URLEncoder.encode(encodedName, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Impossibile codificare il nome");
            return null;
        }

        String url = FIRST_PIECE_URL + encodedName + SECOND_PIECE_URL;
        String json;
        try {
            json = getJSONFromUrl(url);
            JSONObject result = new JSONObject(json);
            int size = result.optInt("size");

            if (size == 0) {
                Log.i(TAG, "Nessun elemento trovato");
                return null;
            }

            JSONArray links = result.optJSONArray("links");
            if (links == null || links.length() == 0) {
                Log.i(TAG, "Nessun gioco trovato");
                return null;
            }

            ArrayList<GameSearchResult> listOfGame = new ArrayList<>();
            for (int j = 0; j < links.length(); j++) {
                JSONObject gameInfomation = links.optJSONObject(j);
                if (gameInfomation == null || gameInfomation.length() == 0) continue;

                String titleGame = gameInfomation.optString("name");
                if (titleGame.toLowerCase().contains(name)) {
                    String imageURL = gameInfomation.optJSONArray("images").optJSONObject(0).optString("url");
                    String console = gameInfomation.optJSONArray("playable_platform").join(", ").replaceAll("\"", "");
                    String publisher = gameInfomation.optString("provider_name");
                    String releaseData = gameInfomation.optString("release_date");
                    String gameURL = gameInfomation.optString("url");

                    GameSearchResult gameSearchResult = new GameSearchResult(titleGame, imageURL, gameURL, null, console, STORE);
                    listOfGame.add(gameSearchResult);
                }
            }
            return listOfGame;

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
    public void onPostExecute(ArrayList<GameSearchResult> psnList) {
        if (psnList != null) delegate.processFinish(psnList);
    }
}
