package it.adriano.tumino.gamepoint.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import it.adriano.tumino.gamepoint.R;

public class BindingUtils {

    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url).fit()
                    .centerInside()
                    .placeholder(new ColorDrawable(Color.CYAN))
                    .into(view);
        }
    }

    @BindingAdapter("imageHeaderResult")
    public static void showImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url)
                    .resize(view.getMaxWidth(), 700)
                    .centerInside()
                    .placeholder(new ColorDrawable(Color.CYAN))
                    .into(view);
        }
    }

    @BindingAdapter("storeImage")
    public static void storeImage(ImageView view, String store) {
        if (store != null && !store.isEmpty()) {
            String logoName = getLogoForStore(store);
            Drawable d;
            try {
                d = Drawable.createFromStream(view.getContext().getAssets().open(logoName), null);
            } catch (IOException exception) {
                d = new ColorDrawable(Color.CYAN);
            }
            view.setImageDrawable(d);
        }
    }

    private static String getLogoForStore(String store) {
        switch (store) {
            case "STEAM":
                return "logo_steam.png";
            case "MCS":
                return "logo_xbox.png";
            case "PSN":
                return "logo_psn.png";
            case "ESHOP":
                return "logo_eshop.png";
            default:
                return "icon.png";
        }
    }
}
