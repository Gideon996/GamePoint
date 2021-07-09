package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.databinding.FavoriteGameLayoutBinding;
import it.adriano.tumino.gamepoint.ui.showgame.GameResultFragment;

public class FavoriteGamesHolder extends RecyclerView.ViewHolder {

    public FavoriteGameLayoutBinding binding;

    public FavoriteGamesHolder(FavoriteGameLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.lastGames, obj); //nome della variabile da usare
        binding.executePendingBindings();
        binding.favoriteGameLayout.setOnClickListener(v -> {
            BasicGameInformation basicGameInformation = (BasicGameInformation) obj;
            Bundle bundle = new Bundle();
            bundle.putParcelable("game", basicGameInformation);
            GameResultFragment fragment = new GameResultFragment();
            fragment.setArguments(bundle);
            FragmentActivity activity = (FragmentActivity) binding.favoriteGameLayout.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeLayout, fragment)
                    .setReorderingAllowed(true)
                    .commit();
        });
    }
}


