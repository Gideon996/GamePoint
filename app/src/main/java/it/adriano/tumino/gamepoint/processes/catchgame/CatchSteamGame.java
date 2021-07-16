package it.adriano.tumino.gamepoint.processes.catchgame;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.data.storegame.SteamStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.TaskRunner;

public class CatchSteamGame extends TaskRunner<Void, StoreGame> implements JsonParser<SteamStoreGame> {
    public static final String TAG = "CatchGameFromSteam";

    private static final String URL_API = "https://store.steampowered.com/api/appdetails?appids=";

    private final String finalURL;
    private final String appID;

    public AsyncResponse<StoreGame> delegate = null;

    public CatchSteamGame(String appID) {
        finalURL = URL_API + appID;
        this.appID = appID;
    }

    @Override
    public StoreGame doInBackground(Void... input) {
        return getJsonAndParsing(finalURL, TAG);
    }

    @Override
    public SteamStoreGame jsonParser(@NotNull String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has(appID)) {
            JSONObject result = jsonObject.getJSONObject(appID);

            SteamStoreGame game = new SteamStoreGame();

            if (!result.getBoolean("success")) {
                return null;
            }

            String name = "N.A.";
            String description = "N.A.";
            String languages = "N.A.";
            String image = "https://www.pdvg.it/wp-content/uploads/2021/05/Steam-2.jpeg";
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
                if (data.has("name")) name = data.getString("name");
                game.setTitle(name);

                if (data.has("is_free")) {
                    boolean isFree = data.getBoolean("is_free");
                    if (!isFree) {
                        if (data.has("price_overview") && data.getJSONObject("price_overview").has("final_formatted")) {
                            price = data.getJSONObject("price_overview").getString("final_formatted");
                        } else {
                            price = "Disponibile a breve";
                        }
                    }
                }
                game.setPrice(price);

                if (data.has("detailed_description"))
                    description = data.getString("detailed_description"); //description in HTML format
                game.setDescription(description);

                if (data.has("supported_languages"))
                    languages = data.getString("supported_languages");
                game.setLanguages(languages);

                if (data.has("header_image")) image = data.getString("header_image");
                game.setImageHeaderURL(image);

                if (data.has("website")) website = data.getString("website");
                game.setWebsite(website);

                if (data.has("pc_requirements")) {
                    JSONObject object = data.getJSONObject("pc_requirements");
                    if (object.has("minimum")) minimum = object.getString("minimum");
                    if (object.has("recommended")) recommended = object.getString("recommended");
                }
                game.setMinimumRequirement(minimum);
                game.setRecommendedRequirement(recommended);

                if (data.has("developers"))
                    developers = getValuesFromJSONArray(data.getJSONArray("developers"), "");
                game.setDevelopers(developers);

                if (data.has("publishers"))
                    publishers = getValuesFromJSONArray(data.getJSONArray("publishers"), "");
                game.setPublishers(publishers);

                if (data.has("metacritic"))
                    scoreMetacritic = data.getJSONObject("metacritic").getInt("score");
                game.setScoreMetacritic("" + scoreMetacritic);

                if (data.has("categories"))
                    categories = getValuesFromJSONArray(data.getJSONArray("categories"), "description");
                game.setCategories(categories);

                if (data.has("genres"))
                    genres = getValuesFromJSONArray(data.getJSONArray("genres"), "description");
                game.setGenres(genres);

                if (data.has("screenshots"))
                    screenshots = getValuesFromJSONArray(data.getJSONArray("screenshots"), "path_full");
                game.setScreenshotsUrl(screenshots);

                String videoTrailerUrl = "https://cdn.akamai.steamstatic.com/steam/clusters/frontpage/ae4424ffb1beda926e14cd43/mp4_page_bg_english.mp4?t=1626369244";
                if (data.has("movies") && data.getJSONArray("movies").length() > 0) {
                    JSONObject object = data.getJSONArray("movies").getJSONObject(0);
                    if (object.has("mp4") && object.getJSONObject("mp4").has("max")) {
                        videoTrailerUrl = object.getJSONObject("mp4").getString("max");
                    }
                }
                game.setVideoUrl(videoTrailerUrl);

                if (data.has("release_date")) {
                    boolean comingSoon = data.getJSONObject("release_date").getBoolean("coming_soon");
                    date = "Coming Soon";
                    if (!comingSoon) {
                        date = "Data di uscita: " + data.getJSONObject("release_date").getString("date");
                    }
                }
                game.setReleaseData(date);

                return game;
            }
        }

        return null;
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
    public void onPostExecute(StoreGame output) {
        delegate.processFinish(output);
    }
}
