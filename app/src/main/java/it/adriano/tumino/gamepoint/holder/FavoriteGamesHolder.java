package it.adriano.tumino.gamepoint.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;

public class FavoriteGamesHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;
    private final TextView title;

    public FavoriteGamesHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.favoriteImageView);
        title = itemView.findViewById(R.id.favoriteTitleTextView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTitle() {
        return title;
    }
}


