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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Utils {
    private static final String TAG = "Utils";

    public static Uri getLocalBitmapUri(Context context, Bitmap bmp) {
        Uri bmpUri;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String FILE_AUTHORITY = "it.adriano.tumino.gamepoint";
        bmpUri = FileProvider.getUriForFile(context, FILE_AUTHORITY, file);

        return bmpUri;
    }

    public static String getJsonFromUrl(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setDefaultUseCaches(false);
            connection.setUseCaches(false);
            if (Locale.getDefault().getLanguage().equals("it")) {
                connection.setRequestProperty("Accept-Language", "it-IT");
            } else {
                connection.setRequestProperty("Accept-Language", "en-US");
            }


            InputStream response = connection.getInputStream();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8))) { //autoclossable
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                return sb.toString();
            }
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage());
            return "";
        }
    }

    public static Document getDocumentFromUrl(String url) {
        HashMap<String, String> languages = getAcceptAndContentLanguage();
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .header("Content-Language", languages.get("content"))
                    .header("Accept-Language", languages.get("accept"))
                    .get();
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    private static HashMap<String, String> getAcceptAndContentLanguage() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("content", "en");
        hashMap.put("accept", "en-GB");

        if (Locale.getDefault().getLanguage().equals("it")) {
            hashMap.replace("content", "it");
            hashMap.replace("accept", "it-IT");
        }

        return hashMap;
    }

    public static void shareContent(Context context, String imageUrl, String text) {
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, Utils.getLocalBitmapUri(context, bitmap));
                i.putExtra(Intent.EXTRA_TEXT, text);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, "Impossibile prelevare l'immagine");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }
}
