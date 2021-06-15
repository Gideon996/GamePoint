package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;

public class ScreenshotHolder extends RecyclerView.ViewHolder{

    private final ImageView imageView;

    public ScreenshotHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.screenshotImageView);
    }

    public ImageView getImageView() {
        return imageView;
    }

}
