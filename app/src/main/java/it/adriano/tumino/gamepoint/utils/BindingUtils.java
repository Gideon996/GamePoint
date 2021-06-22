package it.adriano.tumino.gamepoint.utils;

import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import it.adriano.tumino.gamepoint.ui.showgame.DescriptionFragment;

public class BindingUtils {

    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url).fit()
                    .centerInside()
                    .into(view);
        }
    }

    @BindingAdapter("imageHeaderResult")
    public static void showImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url)
                    .resize(view.getMaxWidth(), 700)
                    .centerInside()
                    .into(view);
        }
    }
}
