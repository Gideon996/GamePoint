package it.adriano.tumino.gamepoint.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static Uri getLocalBitmapUri(Context context, Bitmap bmp) { //Salvo l'img in locale e ne prelevo l'url per la condivisione
        Uri bmpUri;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png"); //Creo il file

        try (FileOutputStream out = new FileOutputStream(file)) {
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out); //Salvo l'img
        } catch (IOException e) {
            e.printStackTrace();
        }

        String FILE_AUTHORITY = "it.adriano.tumino.gamepoint"; //Prendo l'autorizzazione
        bmpUri = FileProvider.getUriForFile(context, FILE_AUTHORITY, file); //uso il fileProvider per prelevare l'uri dell'img

        return bmpUri;
    }

    public static String getJsonFromUrl(String url) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(new URL(url).openStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }

    public static Document getDocumentFromUrl(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                .header("Accept-Language", "it")
                .get();
    }
}
