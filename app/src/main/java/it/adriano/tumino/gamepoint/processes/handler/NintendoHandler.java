package it.adriano.tumino.gamepoint.processes.handler;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.catchgame.CatchNintendoGame;
import it.adriano.tumino.gamepoint.utils.Utils;

public class NintendoHandler {
    private static final String TAG = "NintendoHandler";

    private final static String FIRST_PIECE_SEARCH_URL = "https://search.nintendo-europe.com/en/select?q=";
    private final static String SECOND_PIECE_SEARCH_URL = "&fq=type%3A*%20AND%20*%3A*&start=0&rows=24&wt=json&group=true&group.field=pg_s&group.limit=100&group.sort=score%20desc,%20date_from%20desc&sort=score%20desc,%20date_from%20desc";
    private final static String SEARCH_DEFAULT_URL = "/Search/Search-299117.html?f=147394-7042-11772";
    private final static String SEARCH_ITALIAN_URL = "/Cerca/Cerca-299117.html?f=147394-7042-11772";
    private final static String DEFAULT_URL = "https://www.nintendo.co.uk";
    private final static String ITALIAN_URL = "https://www.nintendo.it";


    public static String generateSearchUrl(String title) {
        return FIRST_PIECE_SEARCH_URL + title + SECOND_PIECE_SEARCH_URL;
    }

    public static String generateGameUrl() {
        return null;
    }

    public static List<BasicGameInformation> nintendoGames(@NonNull String title) {
        title = title.toLowerCase();
        List<BasicGameInformation> result = new ArrayList<>();
        String titleEncoded = SearchUtils.encodedTitle(title);
        if (titleEncoded.isEmpty()) return result;

        String finalUrl = generateSearchUrl(titleEncoded);
        String json = Utils.getJsonFromUrl(finalUrl);
        if (json.isEmpty()) return result;

        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("grouped") && jsonObject.getJSONObject("grouped").has("pg_s")) {
                JSONObject allElements = jsonObject.getJSONObject("grouped").getJSONObject("pg_s");
                int matches = allElements.getInt("matches");
                if (matches == 0) {
                    Log.i(TAG, "No matches");
                    return result;
                }

                JSONArray groups = allElements.getJSONArray("groups");
                JSONArray games = null;
                for (int j = 0; j < groups.length(); j++) {
                    JSONObject object = (JSONObject) groups.get(j);
                    if (object.has("groupValue") && object.getString("groupValue").equals("GAME")) {
                        games = object.getJSONObject("doclist").getJSONArray("docs");
                    }
                }

                if (games != null) {
                    for (int j = 0; j < games.length(); j++) {
                        JSONObject object = games.optJSONObject(j);
                        if (object == null || !object.has("title")) continue;

                        String gameTitle = object.getString("title");
                        if (SearchUtils.deleteSpecialCharacter(gameTitle).toLowerCase().contains(title)) {
                            String gameUrl = object.getString("url");
                            if (gameUrl.isEmpty()) continue;

                            String imageUrl = "https://wallpaperaccess.com/full/417026.jpg";
                            if (object.has("image_url")) imageUrl = object.getString("image_url");

                            String finalPrice = "N.A.";
                            if (object.has("price_sorting_f")) {
                                double price = object.getDouble("price_sorting_f");
                                finalPrice = "" + price;
                                if (price == 999999.0) finalPrice = "Unavailable";
                            }

                            StringBuilder console = new StringBuilder();
                            console.append("");
                            if (object.has("system_names_txt")) {
                                JSONArray consoleArray = object.getJSONArray("system_names_txt");
                                for (int k = 0; k < consoleArray.length(); k++) {
                                    console.append(consoleArray.optString(j));
                                }
                            }

                            BasicGameInformation basicGameInformation = new BasicGameInformation(gameTitle, imageUrl, gameUrl, null, console.toString(), "ESHOP", finalPrice);
                            result.add(basicGameInformation);

                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    /*Method to catch Nintendo's game*/
    public static void catchGame(String url, String price, AsyncResponse<StoreGame> delegate) {
        CatchNintendoGame catchNintendoGame = new CatchNintendoGame(url, price);
        catchNintendoGame.delegate = delegate;
        catchNintendoGame.execute();
    }
}
