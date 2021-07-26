package it.adriano.tumino.gamepoint.processes.handler;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class SearchUtils {
    private static final String TAG = "SearchUtils";

    private final static String MICROSOFT_URL = "https://www.microsoft.com/it-it/search/shop/games?q=";

    private final static String FIRST_PSN_URL = "https://store.playstation.com/store/api/chihiro/00_09_000/tumbler/IT/it/999/";
    private final static String SECOND_PSN_URL = "?suggested_size=100&mode=game";


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
