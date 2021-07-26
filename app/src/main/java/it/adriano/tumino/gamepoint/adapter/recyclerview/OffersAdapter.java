package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.databinding.FavoriteGameLayoutBinding;
import it.adriano.tumino.gamepoint.databinding.OffersLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.FavoriteGamesHolder;
import it.adriano.tumino.gamepoint.holder.recyclerview.OffersHolder;

public class OffersAdapter extends RecyclerView.Adapter<OffersHolder> {

    private final List<GameOffers> favoriteGames;

    public OffersAdapter(List<GameOffers> favoriteGames) {
        this.favoriteGames = favoriteGames;
    }

    @NotNull
    @Override
    public OffersHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        OffersLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.offers_layout, parent, false);
        return new OffersHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull OffersHolder holder, int position) {
        BasicGameInformation basicGameInformation = favoriteGames.get(position);
        holder.bind(basicGameInformation);
    }

    @Override
    public int getItemCount() {
        return favoriteGames.size();
    }

}
