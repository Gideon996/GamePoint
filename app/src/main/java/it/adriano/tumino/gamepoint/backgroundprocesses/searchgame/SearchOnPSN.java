package it.adriano.tumino.gamepoint.backgroundprocesses.searchgame;

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
import java.text.Normalizer;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.backgroundprocesses.TaskRunner;

public class SearchOnPSN extends TaskRunner<String, ArrayList<BasicGameInformation>> {
    public static final String TAG = "SearchOnPSN";

    private final static String STORE = "PSN";
    private final static String FIRST_PIECE_URL = "https://store.playstation.com/store/api/chihiro/00_09_000/tumbler/IT/it/999/";
    private final static String SECOND_PIECE_URL = "?suggested_size=100&mode=game";

    public AsyncResponse<ArrayList<BasicGameInformation>> delegate = null;

    public SearchOnPSN() {

    }

    @Override
    public ArrayList<BasicGameInformation> doInBackground(String... input) {
        Log.i(TAG, "Ricerca gioco su PSN");

        String name = input[0].toLowerCase();
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

            ArrayList<BasicGameInformation> listOfGame = new ArrayList<>();
            for (int j = 0; j < links.length(); j++) {
                JSONObject gameInfomation = links.optJSONObject(j);
                if (gameInfomation == null || gameInfomation.length() == 0) continue;

                String titleGame = gameInfomation.optString("name");
                String verify = Normalizer.normalize(titleGame, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                if (verify.toLowerCase().contains(name)) {
                    String imageURL = gameInfomation.optJSONArray("images").optJSONObject(0).optString("url");
                    String console = gameInfomation.optJSONArray("playable_platform").join(", ").replaceAll("\"", "");
                    String publisher = gameInfomation.optString("provider_name");
                    String releaseData = gameInfomation.optString("release_date");
                    String gameURL = gameInfomation.optString("url");
                    String id = gameURL.split("-")[1];

                    String price = "N.A.";
                    if(gameInfomation.has("default_sku")){
                        price = gameInfomation.getJSONObject("default_sku").getString("display_price");
                    }

                    BasicGameInformation basicGameInformation = new BasicGameInformation(titleGame, imageURL, gameURL, id, console, STORE, price);
                    listOfGame.add(basicGameInformation);
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
    public void onPostExecute(ArrayList<BasicGameInformation> psnList) {
        if (psnList != null) delegate.processFinish(psnList);
    }
}
