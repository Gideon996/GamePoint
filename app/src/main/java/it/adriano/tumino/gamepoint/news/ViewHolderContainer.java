package it.adriano.tumino.gamepoint.news;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.R;

public class ViewHolderContainer {

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public FooterViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.visualizzaAltro);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView description;
        TextView data;
        TextView sito;
        LinearLayout relativeLayout;
        Context context;

        public ItemViewHolder(View itemView) {
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
}
