package it.adriano.tumino.gamepoint.news;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView title;
    public TextView description;
    public TextView data;
    public TextView sito;
    public LinearLayout relativeLayout;
    public Context context;

    public ViewHolder(View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.newsImageView);
        title = itemView.findViewById(R.id.newsTitleTextView);
        description = itemView.findViewById(R.id.newsDescriptionTextView);
        data = itemView.findViewById(R.id.dataTextView);
        sito = itemView.findViewById(R.id.sitoTextView);
        relativeLayout = itemView.findViewById(R.id.newsRowLayout);
        context = itemView.getContext();
    }
}
