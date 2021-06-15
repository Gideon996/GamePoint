package it.adriano.tumino.gamepoint.holder.viewpager;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.R;

public class ImageGalleryHolder extends RecyclerView.ViewHolder { //Holder per l'img

    public ImageView imageView;

    public ImageGalleryHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.screenshotImageView);
    }
}
