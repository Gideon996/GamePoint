package it.adriano.tumino.gamepoint.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.R;

public class NewsHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final TextView title;
    private final TextView description;
    private final TextView data;
    private final TextView sito;
    private final LinearLayout relativeLayout;

    public NewsHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.newsImageView);
        title = itemView.findViewById(R.id.newsTitleTextView);
        description = itemView.findViewById(R.id.newsDescriptionTextView);
        data = itemView.findViewById(R.id.dateTextView);
        sito = itemView.findViewById(R.id.newsSiteTextView);
        relativeLayout = itemView.findViewById(R.id.newsRowLayout);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getData() {
        return data;
    }

    public TextView getSito() {
        return sito;
    }

    public LinearLayout getRelativeLayout() {
        return relativeLayout;
    }
}
