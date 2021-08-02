package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.MainSharedViewModel;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.databinding.FooterNewsLayoutBinding;
import it.adriano.tumino.gamepoint.databinding.NewsLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.FooterNewsHolder;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.holder.recyclerview.NewsHolder;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AsyncResponse<List<News>> {
    private final List<News> newsList;
    private final MainSharedViewModel viewModel;

    private static final int TYPE_FOOTER = 1;

    public NewsAdapter(List<News> newsList, Activity activity) {
        this.newsList = newsList;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(MainSharedViewModel.class);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            FooterNewsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.footer_news_layout, parent, false);
            return new FooterNewsHolder(binding, this);
        }
        NewsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_layout, parent, false);
        return new NewsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterNewsHolder) {
            FooterNewsHolder holderBinding = (FooterNewsHolder) holder;
            holderBinding.bind(viewModel.nextPage());
        } else if (holder instanceof NewsHolder) {
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
        viewModel.setNewsList(result);
        notifyItemRangeInserted(newsList.size() - result.size(), result.size());
    }
}

