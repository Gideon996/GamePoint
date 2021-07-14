package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.CatchNews;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.databinding.FooterNewsLayoutBinding;

public class FooterNewsHolder extends RecyclerView.ViewHolder {
    public FooterNewsLayoutBinding binding;
    private final AsyncResponse<List<News>> delegate;

    public FooterNewsHolder(FooterNewsLayoutBinding binding, AsyncResponse<List<News>> delegate) {
        super(binding.getRoot());
        this.binding = binding;
        this.delegate = delegate;
    }

    public void bind(Object obj) {
        binding.loadingOtherNewsTextView.setVisibility(View.GONE);
        binding.viewMoreNewsButton.setVisibility(View.VISIBLE);

        binding.executePendingBindings();
        binding.viewMoreNewsButton.setOnClickListener(v -> {
            binding.loadingOtherNewsTextView.setVisibility(View.VISIBLE);
            binding.viewMoreNewsButton.setVisibility(View.GONE);
            int nextPage = ((Integer) obj) + 1;
            CatchNews updateNews = new CatchNews(delegate);
            updateNews.execute(nextPage);
        });
    }

}
