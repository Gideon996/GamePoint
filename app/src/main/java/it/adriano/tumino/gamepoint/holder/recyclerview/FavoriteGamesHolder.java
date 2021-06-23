package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;

public class FavoriteGamesHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;
    private final TextView title;
    private final ConstraintLayout layout;

    public FavoriteGamesHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.favoriteImageHeaderView);
        title = itemView.findViewById(R.id.favoriteTitleTextView);
        layout = itemView.findViewById(R.id.favoriteGameLayout);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTitle() {
        return title;
    }

    public ConstraintLayout getLayout() {
        return layout;
    }
}


