package it.adriano.tumino.gamepoint.processes.handler;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.ProcessUtils;
import it.adriano.tumino.gamepoint.processes.catchgame.CatchSteamGame;
import it.adriano.tumino.gamepoint.utils.Utils;

public class SteamHandler {
    private static final String TAG = "SteamHandler";

    private static final String STEAM_SEARCH_URL = "https://store.steampowered.com/search/?term=";
    private static final String STEAM_OFFERS_URL = "https://store.steampowered.com/search/?filter=topsellers&specials=1";

    public static String generateSteamUrl(String title){
        return STEAM_SEARCH_URL + title;
    }

    /*Method to search for a game through the title on steam*/
    public static List<BasicGameInformation> steamGames(@NonNull String title) {
        title = title.toLowerCase();
        List<BasicGameInformation> result = new ArrayList<>();

        String titleEncoded = ProcessUtils.encodedTitle(title);
        if (titleEncoded.isEmpty()) return result;

        Document document = Utils.getDocumentFromUrl(generateSteamUrl(titleEncoded));
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
            if (ProcessUtils.deleteSpecialCharacter(gameTitle).toLowerCase().contains(title)) { //I only take games with the correct titles
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

    /*Method to search for the latest offers on steam*/
    public static List<GameOffers> topSellerAndSpecialsSteam() {
        List<GameOffers> result = new ArrayList<>();

        Document document = Utils.getDocumentFromUrl(STEAM_OFFERS_URL);
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
            if (link.attributes().hasKey("data-ds-appid")) {
                String gameID = link.attributes().get("data-ds-appid");
                if (gameID == null || gameID.isEmpty()) continue;
                String imgUrl = link.getElementsByTag("img").get(0).attributes().get("src");

                String platforms = getPlatform(link.getElementsByClass("platform_img"));

                String discountPercentage = link.getElementsByClass("search_discount").get(0).text();
                String[] allPrice = link.getElementsByClass("search_price").get(0).text().split(" ");//prezzo€ prezzo€
                String originalPrice = allPrice[0];
                String finalPrice = allPrice[1];

                GameOffers gameOffers = new GameOffers(gameTitle, imgUrl, null, gameID, platforms, "STEAM", finalPrice, originalPrice, discountPercentage);
                result.add(gameOffers);
            }
        }
        return result;
    }

    /*Method to catch Steam's game*/
    public static void catchGame(String appID, AsyncResponse<StoreGame> delegate) {
        CatchSteamGame catchSteamGame = new CatchSteamGame(appID);
        catchSteamGame.delegate = delegate;
        catchSteamGame.execute();
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
