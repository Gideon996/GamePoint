package it.adriano.tumino.gamepoint.holder.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.databinding.FavoriteGameLayoutBinding;

public class FavoriteGamesHolder extends RecyclerView.ViewHolder {

    public FavoriteGameLayoutBinding binding;

    public FavoriteGamesHolder(FavoriteGameLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.lastGames, obj); //nome della variabile da usare
        binding.executePendingBindings();
    }
}


