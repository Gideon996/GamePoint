package it.adriano.tumino.gamepoint.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;

public class LastSearchedGamesHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final TextView textView1;
    private final TextView textView2;

    public LastSearchedGamesHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.gameCoverSearchedImageView);
        textView1 = itemView.findViewById(R.id.gameTitleSearchedTextView);
        textView2 = itemView.findViewById(R.id.storeGameSearchedTextView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView1() {
        return textView1;
    }

    public TextView getTextView2() {
        return textView2;
    }
}
