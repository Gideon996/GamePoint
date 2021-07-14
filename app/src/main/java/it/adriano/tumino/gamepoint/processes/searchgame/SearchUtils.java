package it.adriano.tumino.gamepoint.processes.searchgame;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class SearchUtils {
    private static final String TAG = "SearchUtils";

    private static final String STEAM_URL = "https://store.steampowered.com/search/?term=";

    private final static String FIRST_NINTENDO_URL = "https://search.nintendo-europe.com/it/select?q=";
    private final static String SECOND_NINTENDO_URL = "&fq=type%3A*%20AND%20*%3A*&start=0&rows=24&wt=json&group=true&group.field=pg_s&group.limit=100&group.sort=score%20desc,%20date_from%20desc&sort=score%20desc,%20date_from%20desc";

    private final static String MICROSOFT_URL = "https://www.microsoft.com/it-it/search/shop/games?q=";

    private final static String FIRST_PSN_URL = "https://store.playstation.com/store/api/chihiro/00_09_000/tumbler/IT/it/999/";
    private final static String SECOND_PSN_URL = "?suggested_size=100&mode=game";

    public static String generateSteamUrl(String title){
        return STEAM_URL + title;
    }

    public static String generateNintendoUrl(String title){
        return FIRST_NINTENDO_URL + title + SECOND_NINTENDO_URL;
    }

    public static String generateMicrosoftUrl(String title){
        return MICROSOFT_URL + title;
    }

    public static String generatePlayStationUrl(String title){
        return FIRST_PSN_URL + title + SECOND_PSN_URL;
    }

    public static String encodedTitle(String titleToEncode) {
        if (titleToEncode == null || titleToEncode.isEmpty()) return "";

        titleToEncode = titleToEncode.toLowerCase();
        String encodedName = "";
        try {
            encodedName = URLEncoder.encode(titleToEncode, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Cannot encode the gameTitle");
        }

        return encodedName;
    }

    public static String deleteSpecialCharacter(@NonNull String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
