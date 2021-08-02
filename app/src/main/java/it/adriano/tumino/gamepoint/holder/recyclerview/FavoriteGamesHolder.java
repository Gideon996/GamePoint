package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.databinding.FavoriteGameLayoutBinding;

public class FavoriteGamesHolder extends RecyclerView.ViewHolder {
    public FavoriteGameLayoutBinding binding;

    public FavoriteGamesHolder(FavoriteGameLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.lastGames, obj);
        binding.executePendingBindings();
        binding.favoriteGameLayout.setOnClickListener(v -> {
            BasicGameInformation basicGameInformation = (BasicGameInformation) obj;
            Bundle bundle = new Bundle();
            bundle.putString("title", basicGameInformation.getTitle());
            bundle.putParcelable("game", basicGameInformation);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.navigate_to_favorite, bundle);
        });
    }
}


