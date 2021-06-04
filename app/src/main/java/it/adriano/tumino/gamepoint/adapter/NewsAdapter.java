package it.adriano.tumino.gamepoint.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.holder.FooterNewsHolder;
import it.adriano.tumino.gamepoint.holder.NewsHolder;
import it.adriano.tumino.gamepoint.backgroundprocesses.CatchNews;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.ui.news.NewsViewModel;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "NewsAdapter";

    private final ArrayList<News> newsList;
    private final Activity activity;
    private int currentPage;
    private final NewsViewModel newsViewModel;

    private static final int TYPE_FOOTER = 1;

    public NewsAdapter(ArrayList<News> newsList, Activity activity, int currentPage, NewsViewModel newsViewModel) {
        Log.i(TAG, "Generazione News Adapter");
        this.newsList = newsList;
        this.activity = activity;
        this.currentPage = currentPage;
        this.newsViewModel = newsViewModel;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            Log.i(TAG, "Inserimento Footer Layout");
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false);
            return new FooterNewsHolder(itemView);
        }
        Log.i(TAG, "Inserimento Item Layout");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_news_row, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterNewsHolder) {
            Log.i(TAG, "Riempimento Footer Layout e aggiunta onClickListener");

            FooterNewsHolder footerViewHolder = (FooterNewsHolder) holder;
            Button visualizzaAltro = footerViewHolder.getButton();
            visualizzaAltro.setOnClickListener(v -> {
                currentPage++;
                CatchNews updateNews = new CatchNews(newsViewModel, this);
                updateNews.execute(currentPage);
            });
        } else if (holder instanceof NewsHolder) {
            Log.i(TAG, "Riempimento News Layout e aggiunta onClickListener");

            NewsHolder itemViewHolder = (NewsHolder) holder;
            News gameNews = newsList.get(position);
            itemViewHolder.getTitle().setText(gameNews.getTitle());
            itemViewHolder.getDescription().setText(gameNews.getBody());
            itemViewHolder.getData().setText(gameNews.getDate());
            itemViewHolder.getSito().setText(gameNews.getWebsite());

            if(gameNews.getImageURL() != null) {
                Picasso.get().load(gameNews.getImageURL())
                        .fit()
                        .centerInside()
                        .into(itemViewHolder.getImageView());
            }else{

            }
            itemViewHolder.getRelativeLayout().setOnClickListener(v -> {
                String url = gameNews.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == newsList.size()) {
            return TYPE_FOOTER;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return newsList.size() + 1;
    }
}
