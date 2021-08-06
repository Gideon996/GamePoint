package it.adriano.tumino.gamepoint.processes.handler;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.ProcessUtils;
import it.adriano.tumino.gamepoint.processes.catchgame.CatchMicrosoftGame;
import it.adriano.tumino.gamepoint.utils.Utils;

public class MicrosoftHandler {
    private static final String TAG = "MicrosoftHandler";

    private final static String MICROSOFT_URL = "https://www.microsoft.com/en-us/search/shop/games?q=";

    public static String generateMicrosoftUrl(String title) {
        String url = MICROSOFT_URL;
        if (Locale.getDefault().getLanguage().equals("it")) url = url.replace("en-us", "it-it");
        return url + title;
    }

    public static List<BasicGameInformation> microsoftGames(@NonNull String title) {
        title = title.toLowerCase();
        List<BasicGameInformation> result = new ArrayList<>();
        String titleEncoded = ProcessUtils.encodedTitle(title);
        if (titleEncoded.isEmpty()) return result;

        Document document = Utils.getDocumentFromUrl(generateMicrosoftUrl(titleEncoded));
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

            Element titleDiv = gameUrlElement.getElementsByClass("c-subheading-6").first();
            String titleGame = titleDiv.text();

            if (ProcessUtils.deleteSpecialCharacter(titleGame).toLowerCase().contains(title)) {
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

                BasicGameInformation basicGameInformation = new BasicGameInformation(titleGame, imageURl, gameUrl, gameID, null, "MCS", null);
                result.add(basicGameInformation);
            }
        }
        return result;
    }

    /*Method to catch Microsoft's game*/
    public static void catchGame(String url, AsyncResponse<StoreGame> delegate) {
        if (Locale.getDefault().getLanguage().equals("it")) url = url.replace("en-us", "it-it");
        CatchMicrosoftGame catchMicrosoftGame = new CatchMicrosoftGame(url);
        catchMicrosoftGame.delegate = delegate;
        catchMicrosoftGame.execute();
    }
}
