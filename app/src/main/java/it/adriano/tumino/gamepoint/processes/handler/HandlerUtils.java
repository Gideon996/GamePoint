package it.adriano.tumino.gamepoint.processes.handler;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Locale;

public class HandlerUtils {
    private static final String TAG = "SearchUtils";

    private final static String MICROSOFT_URL = "https://www.microsoft.com/it-it/search/shop/games?q=";

    private final static String FIRST_PSN_URL = "https://store.playstation.com/store/api/chihiro/00_09_000/tumbler/IT/it/999/";
    private final static String SECOND_PSN_URL = "?suggested_size=100&mode=game";

    private static final String[] ITALIAN_MONTHS = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
    private static final String[] ENGLISH_MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static String generateMicrosoftUrl(String title) {
        return MICROSOFT_URL + title;
    }

    public static String generatePlayStationUrl(String title) {
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

    public static String normalizePCGamerDate(String date) {
        String tmp = date.split("T")[0];
        String[] parserDate = tmp.split("-");
        String month = fromNumberToName(parserDate[1]);

        return parserDate[2] + " " + month + " " + parserDate[0];
    }

    public static String fromNumberToName(String number) {
        int month = Integer.parseInt(number);
        Log.e("TEST", number + "  " + month);
        if (month > 0 && month < 13) {
            if (Locale.getDefault().getLanguage().equals("it")) return ITALIAN_MONTHS[month - 1];

            return ENGLISH_MONTHS[month - 1];
        }
        return "Error";
    }
}
