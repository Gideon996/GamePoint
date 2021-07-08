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
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.databinding.FavoriteGameLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.FavoriteGamesHolder;
import it.adriano.tumino.gamepoint.ui.showgame.GameResultFragment;

public class FavoriteGamesAdapter extends RecyclerView.Adapter<FavoriteGamesHolder> implements ClickItemList {
    private static final String TAG = "FavoriteAdapter";

    private final List<BasicGameInformation> favoriteGames;
    private final FragmentManager fragmentManager;

    public FavoriteGamesAdapter(List<BasicGameInformation> favoriteGames, FragmentManager fragmentManager) {
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
        BasicGameInformation basicGameInformation = favoriteGames.get(position);
        holder.bind(basicGameInformation);
        holder.binding.setGameClicked(this);

    }

    @Override
    public int getItemCount() {
        return favoriteGames.size();
    }

    public void removeFavoriteGame(BasicGameInformation basicGameInformation) {
        favoriteGames.remove(basicGameInformation);
        notifyItemRemoved(favoriteGames.indexOf(basicGameInformation));
    }

    public void addFavoriteGame(BasicGameInformation basicGameInformation) {
        favoriteGames.add(basicGameInformation);
        notifyItemInserted(favoriteGames.indexOf(basicGameInformation));
    }

    @Override
    public void itemClicked(Object game) {
        BasicGameInformation basicGameInformation = (BasicGameInformation) game;
        Bundle bundle = new Bundle();
        bundle.putParcelable("game", basicGameInformation);
        GameResultFragment fragment = new GameResultFragment(this);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.homeLayout, fragment)
                .setReorderingAllowed(true)
                .commit();
    }
}
