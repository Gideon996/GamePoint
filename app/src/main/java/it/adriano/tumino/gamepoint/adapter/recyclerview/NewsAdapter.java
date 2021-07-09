package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.databinding.FooterNewsLayoutBinding;
import it.adriano.tumino.gamepoint.databinding.NewsLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.FooterNewsHolder;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.holder.recyclerview.NewsHolder;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AsyncResponse<List<News>> {
    public static final String TAG = "NewsAdapter";

    private final List<News> newsList;
    private int currentPage;

    private static final int TYPE_FOOTER = 1;

    public NewsAdapter(List<News> newsList, int currentPage) {
        Log.i(TAG, "Generazione News Adapter");
        this.newsList = newsList;
        this.currentPage = currentPage;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            Log.i(TAG, "Inserimento Footer Layout");
            FooterNewsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.footer_news_layout, parent, false);
            return new FooterNewsHolder(binding, this);
        }
        Log.i(TAG, "Inserimento Item Layout");
        NewsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_layout, parent, false);
        return new NewsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterNewsHolder) {
            Log.i(TAG, "Riempimento Footer Layout e aggiunta onClickListener");
            FooterNewsHolder holderBinding = (FooterNewsHolder) holder;
            holderBinding.bind(currentPage);
            currentPage++;
        } else if (holder instanceof NewsHolder) {
            Log.i(TAG, "Riempimento News Layout e aggiunta onClickListener");
            NewsHolder holderBinding = (NewsHolder) holder;
            News news = newsList.get(position);
            holderBinding.bind(news);
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
    public void processFinish(List<News> result) {
        newsList.addAll(result);
        notifyItemRangeInserted(newsList.size() - result.size(), result.size());
    }
}

