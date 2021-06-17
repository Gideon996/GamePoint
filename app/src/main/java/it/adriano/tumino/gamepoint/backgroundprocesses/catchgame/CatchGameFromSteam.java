package it.adriano.tumino.gamepoint.backgroundprocesses.catchgame;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.Game;
import it.adriano.tumino.gamepoint.data.GameShow;
import it.adriano.tumino.gamepoint.utils.TaskRunner;
import it.adriano.tumino.gamepoint.utils.Utils;

public class CatchGameFromSteam extends TaskRunner<Integer, String> {

    private static final String URL_API = "https://store.steampowered.com/api/appdetails?appids=";

    private final String finalURL;
    private final String appID;
    private Game game = new Game();

    public AsyncResponse<Game> delegate = null;

    public CatchGameFromSteam(String appID) {
        finalURL = URL_API + appID;
        this.appID = appID;
    }

    @Override
    public String doInBackground(Integer... i) {
        String jsonText;
        try {
            jsonText = Utils.getJsonFromUrl(finalURL);
        } catch (IOException exception) {
            Log.e("S", exception.getMessage());
            return null;
        }

        if (jsonText.isEmpty()) {
            Log.e("S", "Nessun json prelevato");
            return null;
        }

        try {
            game = jsonParser(jsonText);
        } catch (JSONException exception) {
            Log.e("S", exception.getMessage());
            return null;
        }

        return null;
    }

    private Game jsonParser(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json); //creo l'oggetto
        JSONObject result = jsonObject.getJSONObject(String.valueOf(appID)); //prendo il risultato

        Game game = new Game();

        if (!result.getBoolean("success")) {
            return null;
        }

        String name = "N.A.";
        String description = "N.A.";
        String languages = "N.A.";
        String image = "https://i2.wp.com/www.meganerd.it/wp-content/uploads/2020/11/full-metal-alchemisy-kanzeban.jpg?fit=2560%2C1680&ssl=1";
        String website = "https://store.steampowered.com/";
        String minimum = "N.A.";
        String recommended = "";
        String price = "FREE";
        String date = "3 Ottobre 1911";

        int scoreMetacritic = 0;
        ArrayList<String> developers = new ArrayList<>();
        developers.add("N.A.");
        ArrayList<String> publishers = new ArrayList<>();
        publishers.add("N.A.");
        ArrayList<String> categories = new ArrayList<>();
        categories.add("N.A.");
        ArrayList<String> genres = new ArrayList<>();
        genres.add("N.A.");
        ArrayList<String> screenshots = new ArrayList<>();
        screenshots.add(image);


        JSONObject data;
        if (result.has("data")) {

            data = result.getJSONObject("data");
            if (data.has("name")) name = data.getString("name"); //titolo del gioco
            game.setName(name);

            if (data.has("is_free")) {
                boolean isFree = data.getBoolean("is_free");
                if (!isFree) {
                    price = data.getJSONObject("price_overview").getString("final_formatted"); //prezzo finale
                }
            }
            game.setPrice(price);

            if (data.has("detailed_description"))
                description = data.getString("detailed_description"); //Html.fromHtml(, Html.FROM_HTML_MODE_LEGACY).toString();
            game.setDescription(description);

            if (data.has("supported_languages"))
                languages = data.getString("supported_languages"); //lingue del gioco
            game.setLanguages(languages);

            if (data.has("header_image")) image = data.getString("header_image"); //copertina
            game.setImage(image);

            if (data.has("website")) website = data.getString("website"); //immagine di supporto
            game.setWebsite(website);

            if (data.has("pc_requirements")) {
                JSONObject object = data.getJSONObject("pc_requirements");
                if (object.has("minimum")) minimum = object.getString("minimum");
                if (object.has("recommended")) recommended = object.getString("recommended");
            }
            game.setMinimumRequirement(minimum.replace("<strong>Minimum:</strong><br>", ""));
            game.setRecommendedRequirement(recommended.replace("<strong>Recommended:</strong><br>", ""));

            if (data.has("developers"))
                developers = getValuesFromJSONArray(data.getJSONArray("developers"), "");

            game.setDevelopers(developers);
            if (data.has("publishers"))
                publishers = getValuesFromJSONArray(data.getJSONArray("publishers"), "");
            game.setPublishers(publishers);

            if (data.has("metacritic"))
                scoreMetacritic = data.getJSONObject("metacritic").getInt("score");
            game.setScoreMetacritic(scoreMetacritic);

            if (data.has("categories"))
                categories = getValuesFromJSONArray(data.getJSONArray("categories"), "description");
            game.setCategories(categories);

            if (data.has("genres"))
                genres = getValuesFromJSONArray(data.getJSONArray("genres"), "description");
            game.setGenres(genres);

            if (data.has("screenshots"))
                screenshots = getValuesFromJSONArray(data.getJSONArray("screenshots"), "path_full");
            game.setScreenshots(screenshots);


            if (data.has("release_date")) {
                boolean comingSoon = data.getJSONObject("release_date").getBoolean("coming_soon");
                date = "Coming Soon";
                if (!comingSoon) {
                    date = data.getJSONObject("release_date").getString("date");
                }
            }
            game.setDate(date);

            return game;
        } else {
            return null;
        }
    }

    private ArrayList<String> getValuesFromJSONArray(JSONArray array, String... valuesName) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        if (valuesName[0].isEmpty()) { //simpleJsonArray
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } else { //JsonArray of object
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                for (String valueName : valuesName) {
                    list.add(object.getString(valueName));
                }
            }
        }
        return list;
    }

    @Override
    public void onPostExecute(String o) {
        delegate.processFinish(game);
    }
}
