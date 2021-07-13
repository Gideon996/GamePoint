package it.adriano.tumino.gamepoint.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {
    private static final String TAG = "Utils";

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

    public static String getJsonFromUrl(String url) {
        //I need to do two tries to set the request headers
        try {
            URL connection = new URL(url);
            HttpURLConnection myURLConnection = (HttpURLConnection) connection.openConnection();
            myURLConnection.setRequestMethod("GET");
            myURLConnection.setRequestProperty("Accept-Language", "it");
            myURLConnection.setRequestProperty("Content-Language", "it-IT");
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream(), StandardCharsets.UTF_8))) { //autoclossable
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                return sb.toString();
            }
        } catch (IOException e) { //catch all exception from BufferedReader and URL
            Log.e(TAG, e.getMessage());
            return "";
        }


    }

    public static Document getDocumentFromUrl(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .header("Content-Language", "it-IT")
                    .header("Accept-Language", "it")
                    .get();
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public static void shareContent(Context context, String imageUrl, String text) {
        Picasso.get().load(imageUrl).into(new Target() { //carico l'immagine in cache
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND); //Inizializzo Intent da fare
                i.setType("image/*"); //setto il tipo del contenuto
                i.putExtra(Intent.EXTRA_STREAM, Utils.getLocalBitmapUri(context, bitmap)); //Inserisco l'immagine
                i.putExtra(Intent.EXTRA_TEXT, text); //Inserisco il testo per l'immagine
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //Uso i permessi
                context.startActivity(Intent.createChooser(i, "Share Image")); //Inizio l'activity
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { //Nel caso non riesco a prelevare l'immagine
                Log.e(TAG, "Impossibile prelevare l'immagine");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { //per prelevare l'immagine
            }
        });
    }
}
