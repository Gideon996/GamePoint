package it.adriano.tumino.gamepoint.holder;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.databinding.GameSearchedLayoutBinding;

public class SearchGameHolder extends RecyclerView.ViewHolder {
    private GameSearchedLayoutBinding binding;

    public SearchGameHolder(GameSearchedLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.game, obj); //nome della variabile da usare
        binding.executePendingBindings();
    }
}
