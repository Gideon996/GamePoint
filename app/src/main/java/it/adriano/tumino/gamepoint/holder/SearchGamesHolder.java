package it.adriano.tumino.gamepoint.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;

public class SearchGamesHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final TextView title;
    private final TextView altro;

    public SearchGamesHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageHeaderRicerche);
        title = itemView.findViewById(R.id.titoloUltimeRicerche);
        altro = itemView.findViewById(R.id.storeUltimeRicerche);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getAltro() {
        return altro;
    }
}
