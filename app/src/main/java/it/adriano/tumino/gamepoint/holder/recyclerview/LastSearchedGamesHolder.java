package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
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
        binding.lastGameLayout.setOnClickListener(v -> {
            BasicGameInformation basicGameInformation = (BasicGameInformation) obj;
            Bundle bundle = new Bundle();

            bundle.putParcelable("game", basicGameInformation);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.select_action, bundle);
        });
    }
}
