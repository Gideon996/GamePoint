package it.adriano.tumino.gamepoint.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.NewsLayoutBinding;
import it.adriano.tumino.gamepoint.holder.FooterNewsHolder;
import it.adriano.tumino.gamepoint.backgroundprocesses.CatchNews;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.holder.NewsHolder;
import it.adriano.tumino.gamepoint.ui.news.NewsViewModel;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ClickNews {
    public static final String TAG = "NewsAdapter";

    private final ArrayList<News> newsList;
    private int currentPage;
    private final NewsViewModel newsViewModel;
    private Activity activity;

    private static final int TYPE_FOOTER = 1;

    public NewsAdapter(ArrayList<News> newsList, int currentPage, Activity activity, NewsViewModel newsViewModel) {
        Log.i(TAG, "Generazione News Adapter");
        this.newsList = newsList;
        this.currentPage = currentPage;
        this.activity = activity;
        this.newsViewModel = newsViewModel;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            Log.i(TAG, "Inserimento Footer Layout");
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_news_layout, parent, false);
            return new FooterNewsHolder(itemView);
        }
        Log.i(TAG, "Inserimento Item Layout");
        NewsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_layout, parent, false);
        return new NewsHolder(binding);
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

            NewsHolder holderBinding = (NewsHolder) holder;
            News news = newsList.get(position);
            holderBinding.bind(news);
            holderBinding.binding.setNewsClickListener(this);
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

    @Override
    public void newsClicked(String urlNews) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlNews));
        activity.startActivity(intent);
    }
}

