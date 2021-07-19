package it.adriano.tumino.gamepoint.processes.searchgame;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.utils.Utils;

public class StoresSearchManager {
    private static final String TAG = "StoresSearchManager";

    public static List<BasicGameInformation> steamGames(@NonNull String title) {
        title = title.toLowerCase();
        List<BasicGameInformation> result = new ArrayList<>();

        String titleEncoded = SearchUtils.encodedTitle(title);
        if (titleEncoded.isEmpty()) return result;

        Document document = Utils.getDocumentFromUrl(SearchUtils.generateSteamUrl(titleEncoded));
        if (document == null) {
            Log.i(TAG, "No document");
            return result;
        }

        Element resultsRows = document.getElementById("search_resultsRows");
        if (resultsRows == null || resultsRows.getAllElements().isEmpty()) {
            Log.i(TAG, "No games found STEAM");
            return result;
        }

        Elements links = resultsRows.getElementsByTag("a");
        for (Element link : links) {
            String gameTitle = link.getElementsByClass("title").get(0).text();
            if (SearchUtils.deleteSpecialCharacter(gameTitle).toLowerCase().contains(title)) { //I only take games with the correct titles
                String gameID = link.attributes().get("data-ds-appid");
                if (gameID == null || gameID.isEmpty()) continue;
                String imgUrl = link.getElementsByTag("img").get(0).attributes().get("src");
                String platforms = getPlatform(link.getElementsByClass("platform_img"));
                String price = link.getElementsByClass("search_price").get(0).text();

                BasicGameInformation gameInformation = new BasicGameInformation(gameTitle, imgUrl, null, gameID, platforms, "STEAM", price);
                result.add(gameInformation);
            }
        }

        return result;
    }

    public static List<BasicGameInformation> nintendoGames(@NonNull String title) {
        title = title.toLowerCase();
        List<BasicGameInformation> result = new ArrayList<>();
        String titleEncoded = SearchUtils.encodedTitle(title);
        if (titleEncoded.isEmpty()) return result;

        String finalUrl = SearchUtils.generateNintendoUrl(titleEncoded);
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

    public static List<BasicGameInformation> microsoftGames(@NonNull String title) {
        title = title.toLowerCase();
        List<BasicGameInformation> result = new ArrayList<>();
        String titleEncoded = SearchUtils.encodedTitle(title);
        if (titleEncoded.isEmpty()) return result;

        Document document = Utils.getDocumentFromUrl(SearchUtils.generateMicrosoftUrl(titleEncoded));
        if (document == null) {
            Log.i(TAG, "No document");
            return result;
        }

        Elements grid = document.getElementsByClass("context-list-page");
        if (grid == null || grid.isEmpty()) {
            Log.i(TAG, "No games found MCS");
            return result;
        }

        Elements placementItems = grid.get(0).getElementsByClass("m-channel-placement-item");
        for (int j = 0; j < placementItems.size(); j++) {
            Element game = placementItems.get(j);
            Element gameUrlElement = game.getElementsByTag("a").first();
            String gameUrl = "https://www.microsoft.com" + gameUrlElement.attributes().get("href");

            Element titlediv = gameUrlElement.getElementsByClass("c-subheading-6").first();
            String titleGame = titlediv.text();

            if (SearchUtils.deleteSpecialCharacter(titleGame).toLowerCase().contains(title)) {
                String gameID;
                try {
                    gameID = new JSONObject(gameUrlElement.attributes().get("data-m")).getString("pid");
                } catch (JSONException exception) {
                    Log.e(TAG, exception.toString());
                    continue;
                }

                Element image = gameUrlElement.getElementsByTag("source").first();
                String imageURl = null;
                if (image != null) {
                    imageURl = image.attributes().get("data-srcset");
                }

                BasicGameInformation basicGameInformation = new BasicGameInformation(title, imageURl, gameUrl, gameID, null, "MCS", null);
                result.add(basicGameInformation);
            }
        }
        return result;
    }

    public static List<BasicGameInformation> playStationGames(@NonNull String title) {
        title = title.toLowerCase();
        List<BasicGameInformation> result = new ArrayList<>();
        String titleEncoded = SearchUtils.encodedTitle(title);
        if (titleEncoded.isEmpty()) return result;

        String finalUrl = SearchUtils.generatePlayStationUrl(titleEncoded);
        String json = Utils.getJsonFromUrl(finalUrl);
        if (json.isEmpty()) return result;

        try {
            JSONObject jsonObject = new JSONObject(json);
            int size = jsonObject.optInt("size");

            if (size == 0) {
                Log.i(TAG, "No games found");
                return result;
            }

            if (jsonObject.has("links")) {
                JSONArray links = jsonObject.optJSONArray("links");
                if (links == null || links.length() == 0) {
                    Log.i(TAG, "No games found");
                    return result;
                }

                for (int j = 0; j < links.length(); j++) {
                    JSONObject gameInformation = links.getJSONObject(j);
                    if (gameInformation == null || gameInformation.length() == 0) continue;

                    String titleGame = gameInformation.getString("name");

                    if (SearchUtils.deleteSpecialCharacter(titleGame).toLowerCase().contains(title)) {

                        String imageURL = "https://upload.wikimedia.org/wikipedia/it/thumb/4/4e/Playstation_logo_colour.svg/1200px-Playstation_logo_colour.svg.png";
                        if (gameInformation.has("images"))
                            imageURL = gameInformation.getJSONArray("images").getJSONObject(0).getString("url");

                        String console = "N.A.";
                        if (gameInformation.has("playable_platform"))
                            console = gameInformation.getJSONArray("playable_platform").join(", ").replaceAll("\"", "");

                        String gameURL;
                        String id;
                        if (gameInformation.has("url")) {
                            gameURL = gameInformation.getString("url");
                            id = gameURL.split("-")[1];
                        } else {
                            continue;
                        }

                        String price = "N.A.";
                        if (gameInformation.has("default_sku")) {
                            price = gameInformation.getJSONObject("default_sku").getString("display_price");
                            price = price.replaceAll("€", "") + "€";
                        }

                        BasicGameInformation basicGameInformation = new BasicGameInformation(titleGame, imageURL, gameURL, id, console, "PSN", price);
                        result.add(basicGameInformation);
                    }
                }
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    private static String getPlatform(Elements platforms) {
        if (platforms == null || platforms.isEmpty()) return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (Element platform : platforms) {
            switch (platform.attributes().get("class")) {
                case "platform_img win":
                    stringBuilder.append("WIN ");
                    break;
                case "platform_img mac":
                    stringBuilder.append("MAC ");
                    break;
                case "platform_img linux":
                    stringBuilder.append("LIN ");
                    break;
            }
        }
        return stringBuilder.toString();
    }

}
