package it.adriano.tumino.gamepoint.backgroundprocesses.catchgame;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.Game;
import it.adriano.tumino.gamepoint.utils.TaskRunner;
import it.adriano.tumino.gamepoint.utils.Utils;

public class CatchGameFromPSN extends TaskRunner<Integer, String> {

    private final String finalURL;
    private final Game game;

    public AsyncResponse<Game> delegate = null;

    public CatchGameFromPSN(String gameUrl) {
        finalURL = gameUrl;
        game = new Game();
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
            jsonParser(jsonText);
        } catch (JSONException exception) {
            Log.e("S", "Impossibile prelevare il gioco");
            return null;
        }

        return null;
    }

    private void jsonParser(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        String name = "N.A.";
        if (jsonObject.has("name")) name = jsonObject.getString("name");
        game.setName(name);

        String releaseData = "N.A.";
        if (jsonObject.has("release_date")) releaseData = jsonObject.getString("release_date");
        game.setDate(releaseData);

        String description = "N.A.";
        if (jsonObject.has("long_desc")) description = jsonObject.getString("long_desc");

        String legalText = "";
        if (jsonObject.has("legal_text")) legalText = jsonObject.getString("legal_text");
        if (!legalText.isEmpty()) description += "<br/>" + legalText + "<br/>";
        game.setDescription(description);

        String imageHeader = "https://images6.alphacoders.com/591/thumb-1920-591158.jpg";
        if (jsonObject.has("images")) {
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                if (tmp.getInt("type") == 13) imageHeader = tmp.getString("url");
            }
        }
        game.setImage(imageHeader);

        ArrayList<String> screenshotsUrl = new ArrayList<>();
        screenshotsUrl.add("https://wallpaperaccess.com/full/4419873.png");
        if (jsonObject.has("mediaList") && jsonObject.getJSONObject("mediaList").has("screenshots")) {
            screenshotsUrl.clear();
            JSONArray jsonArray = jsonObject.getJSONObject("mediaList").getJSONArray("screenshots");
            for (int i = 0; i < jsonArray.length(); i++) {
                screenshotsUrl.add(jsonArray.getJSONObject(i).getString("url"));
            }
        }
        game.setScreenshots(screenshotsUrl);

        String rating = "0";
        if (jsonObject.has("content_rating"))
            rating = jsonObject.getJSONObject("content_rating").getString("description");
        game.setWebsite(rating);

        //catogorie + generi
        ArrayList<String> generi = new ArrayList<>();
        generi.add("N.A.");
        if (jsonObject.has("attributes") && jsonObject.getJSONObject("attributes").has("facets") && jsonObject.getJSONObject("attributes").getJSONObject("facets").has("genre")) {
            JSONArray jsonArray = jsonObject.getJSONObject("attributes").getJSONObject("facets").getJSONArray("genre"); //non c'è sempre sia attributes che genre
            generi.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                generi.add(jsonArray.getJSONObject(i).getString("name"));
            }
        }
        game.setGenres(generi);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("N.A.");
        if (jsonObject.has("content_descriptors")) {
            JSONArray jsonArray = jsonObject.getJSONArray("content_descriptors");
            categories.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                categories.add(jsonArray.getJSONObject(i).getString("name"));
            }
        }
        game.setCategories(categories);

        //lingue
        ArrayList<String> voiceLaunguage = new ArrayList<>();
        voiceLaunguage.add("N.A.");
        ArrayList<String> subtitleLanguage = new ArrayList<>();
        subtitleLanguage.add("N.A.");
        String price = "N.A.";
        if (jsonObject.has("skus")) {
            JSONObject skus = jsonObject.getJSONArray("skus").getJSONObject(0);

            if (skus.has("display_price")) price = skus.getString("display_price"); //this

            JSONObject lingue = skus.getJSONArray("entitlements").getJSONObject(0).getJSONObject("metadata");
            if (lingue.has("voiceLanguageCode")) {
                voiceLaunguage.clear();
                JSONArray jsonArray = lingue.getJSONArray("voiceLanguageCode"); //non sempre c'è
                for (int i = 0; i < jsonArray.length(); i++) {
                    voiceLaunguage.add(jsonArray.getString(i));
                }
            }

            if (lingue.has("subtitleLanguageCode")) {
                subtitleLanguage.clear();
                JSONArray jsonArray = lingue.getJSONArray("subtitleLanguageCode");
                for (int i = 0; i < jsonArray.length(); i++) {
                    subtitleLanguage.add(jsonArray.getString(i));
                }
            }
        }
        
        String voice = String.join(", ", voiceLaunguage);
        String subtitle = String.join(", ", subtitleLanguage);
        String languages = "<h3>VOICE</h3><p>" + voice + "</p><h3>SUBTITLE</h3><p>" + subtitle + "</p>";
        game.setLanguages(languages);
        game.setPrice(price);

        //console supportate
        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("N.A.");
        if (jsonObject.has("playable_platform")) {
            platforms.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("playable_platform");
            for (int i = 0; i < jsonArray.length(); i++) {
                platforms.add(jsonArray.getString(i));
            }
        }
        game.setMinimumRequirement(String.join(",", platforms));

    }

    @Override
    public void onPostExecute(String o) {
        delegate.processFinish(game);
    }
}
