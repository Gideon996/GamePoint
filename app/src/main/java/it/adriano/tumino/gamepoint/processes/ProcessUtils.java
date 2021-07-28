package it.adriano.tumino.gamepoint.processes;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Locale;

public class ProcessUtils {
    private static final String TAG = "SearchUtils";

    private static final String[] ITALIAN_MONTHS = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
    private static final String[] ENGLISH_MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

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

    public static String normalizeSteamMonth(String month) {
        if (Locale.getDefault().getLanguage().equals("it")) {
            for (int i = 0; i < ITALIAN_MONTHS.length; i++) {
                if (ITALIAN_MONTHS[i].contains(month)) return fromNumberToName("" + (i + 1));
            }
        } else {
            for (int i = 0; i < ENGLISH_MONTHS.length; i++) {
                if (ENGLISH_MONTHS[i].contains(month)) return fromNumberToName("" + (i + 1));
            }
        }
        return "";
    }

    public static String normalizeNintendoDate(String date) {
        String onlyNumber = date.split(":")[1].trim();
        String[] tmp = onlyNumber.split("/");
        return tmp[0] + " " + fromNumberToName(tmp[1]) + " " + tmp[2];
    }

    public static String normalizePlayStationDate(String date) {
        String[] tmp = date.split("/");
        return tmp[2] + " " + fromNumberToName(tmp[1]) + " " + tmp[0];
    }

    public static String normalizePCGamerDate(String date) {
        String tmp = date.split("T")[0];
        String[] parserDate = tmp.split("-");
        String month = fromNumberToName(parserDate[1]);

        return parserDate[2] + " " + month + " " + parserDate[0];
    }

    public static String fromNumberToName(String number) {
        int month = Integer.parseInt(number);
        if (month > 0 && month < 13) {
            if (Locale.getDefault().getLanguage().equals("it")) return ITALIAN_MONTHS[month - 1];

            return ENGLISH_MONTHS[month - 1];
        }
        return "Error";
    }
}
