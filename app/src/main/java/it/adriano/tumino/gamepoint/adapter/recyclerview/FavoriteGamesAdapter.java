package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.databinding.FavoriteGameLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.FavoriteGamesHolder;
import it.adriano.tumino.gamepoint.ui.showgame.GameResultFragment;

public class FavoriteGamesAdapter extends RecyclerView.Adapter<FavoriteGamesHolder> implements ClickItemList {
    public static final String TAG = "FavoriteAdapter";
    private final List<GameSearchResult> favoriteGames;
    private final FragmentManager fragmentManager;

    public FavoriteGamesAdapter(List<GameSearchResult> favoriteGames, FragmentManager fragmentManager) {
        Log.i(TAG, "Generazione Favorite Adapter");
        this.favoriteGames = favoriteGames;
        this.fragmentManager = fragmentManager;
    }

    @NotNull
    @Override
    public FavoriteGamesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Favorite Layout");

        FavoriteGameLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.favorite_game_layout, parent, false);
        return new FavoriteGamesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull FavoriteGamesHolder holder, int position) {
        Log.i(TAG, "Riempimento Favorite Item");
        GameSearchResult gameSearchResult = favoriteGames.get(position);
        holder.bind(gameSearchResult);
        holder.binding.setGameClicked(this);
    }

    @Override
    public int getItemCount() {
        return favoriteGames.size();
    }

    public void removeFavoriteGame(GameSearchResult gameSearchResult) {
        favoriteGames.remove(gameSearchResult);
        notifyItemRemoved(favoriteGames.indexOf(gameSearchResult));
        notifyItemRangeChanged(favoriteGames.indexOf(gameSearchResult), favoriteGames.size());
        notifyDataSetChanged();
    }

    public void addFavoriteGame(GameSearchResult gameSearchResult) {
        favoriteGames.add(gameSearchResult);
        notifyItemInserted(favoriteGames.indexOf(gameSearchResult));
        notifyItemRangeChanged(favoriteGames.indexOf(gameSearchResult), favoriteGames.size());
        notifyDataSetChanged();
    }

    @Override
    public void itemClicked(Object game) {
        GameSearchResult gameSearchResult = (GameSearchResult) game;
        Bundle bundle = new Bundle();
        bundle.putParcelable("game", gameSearchResult);
        GameResultFragment fragment = new GameResultFragment(this);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.homeLayout, fragment)
                .setReorderingAllowed(true)
                .commit();
    }
}
