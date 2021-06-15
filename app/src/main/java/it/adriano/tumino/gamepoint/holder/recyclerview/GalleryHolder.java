package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.R;

public class GalleryHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final ImageView imageView;
    private final ConstraintLayout layout;

    public GalleryHolder(View view) {
        super(view);
        textView = view.findViewById(R.id.titleImageTextView);
        imageView = view.findViewById(R.id.screenImageView);
        layout = view.findViewById(R.id.galleryItemLayout);
    }

    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ConstraintLayout getLayout() {
        return layout;
    }
}
