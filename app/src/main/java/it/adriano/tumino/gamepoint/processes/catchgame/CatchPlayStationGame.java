package it.adriano.tumino.gamepoint.processes.catchgame;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.data.storegame.PlayStationStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.TaskRunner;

public class CatchPlayStationGame extends TaskRunner<Void, StoreGame> implements JsonParser<PlayStationStoreGame> {
    public static final String TAG = "CatchGameFromPSN";

    private final String finalURL;

    public AsyncResponse<StoreGame> delegate = null;

    public CatchPlayStationGame(String gameUrl) {
        finalURL = gameUrl;
    }

    @Override
    public StoreGame doInBackground(Void... i) {
        return getJsonAndParsing(finalURL, TAG);
    }

    @Override
    public PlayStationStoreGame jsonParser(@NotNull String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        //Only when the game doesn't exist
        //Theoretically impossible because the link comes directly from playstation
        if (jsonObject.has("codeName")) {
            return null;
        }

        PlayStationStoreGame game = new PlayStationStoreGame();

        String titleGame = "N.A.";
        String releaseData = "N.A.";
        String description = "N.A.";
        String legalText = "";
        String rating = "0";
        String price = "N.A.";
        String numberOfPlayers = "N.A.";
        String inGamePurchases = "N.A.";
        String onlinePlayMode = "N.A.";
        String imageHeader = "https://images6.alphacoders.com/591/thumb-1920-591158.jpg";

        ArrayList<String> screenshotsUrl = new ArrayList<>();
        screenshotsUrl.add("https://wallpaperaccess.com/full/4419873.png");

        ArrayList<String> genres = new ArrayList<>();
        genres.add("N.A.");

        ArrayList<String> categories = new ArrayList<>();
        categories.add("N.A.");

        ArrayList<String> voiceLanguages = new ArrayList<>();
        voiceLanguages.add("N.A.");

        ArrayList<String> subtitleLanguages = new ArrayList<>();
        subtitleLanguages.add("N.A.");

        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("N.A.");

        ArrayList<String> subGenreList = new ArrayList<>();

        if (jsonObject.has("name")) titleGame = jsonObject.getString("name");
        game.setTitle(titleGame);

        if (jsonObject.has("release_date")) {
            String tmp = jsonObject.getString("release_date");
            String onlyDate = tmp.split("T")[0];
            releaseData = "Data di uscita: " + onlyDate.replaceAll("-", "/");
        }
        game.setReleaseData(releaseData);

        if (jsonObject.has("long_desc")) description = jsonObject.getString("long_desc");

        if (jsonObject.has("legal_text")) legalText = jsonObject.getString("legal_text");

        if (!legalText.isEmpty()) description += "<br/>" + legalText + "<br/>";
        game.setDescription(description);


        if (jsonObject.has("images")) {
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                if (tmp.getInt("type") == 13) imageHeader = tmp.getString("url");
            }
        }
        game.setImageHeaderURL(imageHeader);

        if (jsonObject.has("mediaList") && jsonObject.getJSONObject("mediaList").has("screenshots")) {
            screenshotsUrl.clear();
            JSONArray jsonArray = jsonObject.getJSONObject("mediaList").getJSONArray("screenshots");
            for (int i = 0; i < jsonArray.length(); i++) {
                screenshotsUrl.add(jsonArray.getJSONObject(i).getString("url"));
            }
        }
        game.setScreenshotsUrl(screenshotsUrl);

        if (jsonObject.has("content_rating"))
            rating = jsonObject.getJSONObject("content_rating").getString("description");
        game.setRating(rating);

        if (jsonObject.has("attributes") && jsonObject.getJSONObject("attributes").has("facets") &&
                jsonObject.getJSONObject("attributes").getJSONObject("facets").has("genre")) {

            genres.clear();
            JSONArray jsonArray = jsonObject.getJSONObject("attributes").getJSONObject("facets").getJSONArray("genre");
            for (int i = 0; i < jsonArray.length(); i++) {
                genres.add(jsonArray.getJSONObject(i).getString("name"));
            }
        }
        game.setGenres(genres);

        if (jsonObject.has("content_descriptors")) {
            categories.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("content_descriptors");
            for (int i = 0; i < jsonArray.length(); i++) {
                categories.add(jsonArray.getJSONObject(i).getString("name"));
            }
        }
        game.setCategories(categories);

        if (jsonObject.has("skus")) {
            JSONObject skus = jsonObject.getJSONArray("skus").getJSONObject(0);

            if (skus.has("display_price")) price = skus.getString("display_price");

            JSONObject entitlements = skus.getJSONArray("entitlements").getJSONObject(0);
            if (!entitlements.isNull("metadata")) {
                JSONObject languages = entitlements.getJSONObject("metadata");

                if (languages.has("voiceLanguageCode")) {
                    voiceLanguages.clear();
                    JSONArray jsonArray = languages.getJSONArray("voiceLanguageCode");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        voiceLanguages.add(jsonArray.getString(i));
                    }
                }

                if (languages.has("subtitleLanguageCode")) {
                    subtitleLanguages.clear();
                    JSONArray jsonArray = languages.getJSONArray("subtitleLanguageCode");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        subtitleLanguages.add(jsonArray.getString(i));
                    }
                }
            }
        }
        game.setVoiceLanguages(voiceLanguages);
        game.setSubtitleLanguages(subtitleLanguages);
        game.setPrice(price);

        if (jsonObject.has("playable_platform")) {
            platforms.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("playable_platform");
            for (int i = 0; i < jsonArray.length(); i++) {
                platforms.add(jsonArray.getString(i));
            }
        }
        game.setPlatforms(platforms);

        if (jsonObject.has("metadata")) {
            JSONObject metadataObject = jsonObject.getJSONObject("metadata");
            if (metadataObject.has("game_subgenre")) {
                JSONArray tmp = metadataObject.getJSONObject("game_subgenre").getJSONArray("values");
                for (int i = 0; i < tmp.length(); i++) {
                    subGenreList.add(tmp.getString(i));
                }
            }

            if (metadataObject.has("cn_numberOfPlayers"))
                numberOfPlayers = metadataObject.getJSONObject("cn_numberOfPlayers").getJSONArray("values").getString(0);

            if (metadataObject.has("cn_inGamePurchases")) {
                JSONArray tmp = metadataObject.getJSONObject("cn_inGamePurchases").getJSONArray("values");
                if (!tmp.getString(0).equals("NOTREQUIRED"))
                    inGamePurchases = tmp.getString(0);
            }

            if (metadataObject.has("cn_onlinePlayMode"))
                onlinePlayMode = metadataObject.getJSONObject("cn_onlinePlayMode").getJSONArray("values").getString(0);

        }
        game.setSubGenreList(subGenreList);
        game.setNumberOfPlayers(numberOfPlayers);
        game.setInGamePurchases(inGamePurchases);
        game.setOnlinePlayMode(onlinePlayMode);

        return game;
    }

    @Override
    public void onPostExecute(StoreGame output) {
        delegate.processFinish(output);
    }
}
