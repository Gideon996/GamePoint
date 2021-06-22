package it.adriano.tumino.gamepoint.holder.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.databinding.LastSearchLayoutBinding;

public class LastSearchedGamesHolder extends RecyclerView.ViewHolder {

    public LastSearchLayoutBinding binding;

    public LastSearchedGamesHolder(LastSearchLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.lastGames, obj); //nome della variabile da usare
        binding.executePendingBindings();
    }
}
