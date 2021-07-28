package it.adriano.tumino.gamepoint.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class BindingUtils {
    @BindingAdapter("showImageFromUrl")
    public static void showImageFromUrl(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url)
                    .fit()
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

    @BindingAdapter("profileImage")
    public static void profileImage(ShapeableImageView imageView, String userID) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + userID + "/profile.jpg");
        profileRef.getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri).fit().into(imageView))
                .addOnFailureListener(exception -> {
                    Drawable d;
                    try {
                        Context context = imageView.getContext();
                        d = Drawable.createFromStream(context.getResources().getAssets().open("profile_default.png"), null);
                    } catch (IOException e) {
                        d = new ColorDrawable(Color.CYAN);
                    }
                    imageView.setImageDrawable(d);
                });
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
