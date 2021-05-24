package it.adriano.tumino.gamepoint.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import it.adriano.tumino.gamepoint.R;

public class NewsAdapterRecycle extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<GameNews> news;
    private Activity activity;

    public NewsAdapterRecycle(ArrayList<GameNews> news, Activity activity) {
        this.news = news;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.list_news_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GameNews gameNews = news.get(position);
        holder.title.setText(gameNews.getTitle());
        holder.description.setText(gameNews.getDescription());
        holder.data.setText(gameNews.getData());
        holder.sito.setText(gameNews.getSito());
        Glide.with(holder.context).load(gameNews.getImageUrl()).placeholder(new ColorDrawable(Color.CYAN)).fitCenter().into(holder.imageView);
        holder.relativeLayout.setOnClickListener(v -> {
            String url = gameNews.getNewsUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

}
