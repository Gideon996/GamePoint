package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.content.Intent;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;


import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.databinding.NewsLayoutBinding;

public class NewsHolder extends RecyclerView.ViewHolder {
    public NewsLayoutBinding binding;

    public NewsHolder(NewsLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.news, obj);
        binding.executePendingBindings();
        binding.lastGameLayout.setOnClickListener(v -> {
            News news = (News) obj;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getNewsUrl()));
            binding.lastGameLayout.getContext().startActivity(intent);
        });
    }
}
