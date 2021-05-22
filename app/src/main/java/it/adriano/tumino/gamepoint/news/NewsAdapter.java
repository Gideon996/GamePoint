package it.adriano.tumino.gamepoint.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import java.util.List;

import it.adriano.tumino.gamepoint.R;

public class NewsAdapter extends BaseAdapter {

    private List<GameNews> gameNews;
    private Context context;
    private Activity activity;

    public NewsAdapter(Context context, List<GameNews> gameNews, Activity activity) {
        this.context = context;
        this.gameNews = gameNews;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return gameNews.size();
    }

    @Override
    public Object getItem(int position) {
        return gameNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_news_row, null);
        }
        GameNews news = (GameNews) getItem(position);
        TextView title = convertView.findViewById(R.id.newsTitleTextView);
        TextView description = convertView.findViewById(R.id.newsDescriptionTextView);
        ImageView imageView = convertView.findViewById(R.id.newsImageView);
        title.setText(news.getTitle());
        description.setText(news.getDescription());
        Glide.with(convertView).load(news.getImageUrl()).placeholder(new ColorDrawable(Color.CYAN)).fitCenter().into(imageView);
        imageView.setContentDescription(news.getTitle());

        LinearLayout linearLayout = convertView.findViewById(R.id.newsRowLayout);
        linearLayout.setOnClickListener(v -> {
            String url = news.getNewsUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        });

        return convertView;
    }
}
