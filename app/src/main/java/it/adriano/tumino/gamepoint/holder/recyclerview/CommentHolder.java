package it.adriano.tumino.gamepoint.holder.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.databinding.CommentLayoutBinding;

public class CommentHolder extends RecyclerView.ViewHolder {
    public CommentLayoutBinding binding;

    public CommentHolder(CommentLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.comment, obj); //nome della variabile da usare
        binding.executePendingBindings();
    }
}
