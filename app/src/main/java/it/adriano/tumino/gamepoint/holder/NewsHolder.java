package it.adriano.tumino.gamepoint.holder;

import androidx.recyclerview.widget.RecyclerView;


import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.databinding.NewsLayoutBinding;

public class NewsHolder extends RecyclerView.ViewHolder {
    public NewsLayoutBinding binding;

    public NewsHolder(NewsLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.news, obj); //nome della variabile da usare
        binding.executePendingBindings();
    }
}
