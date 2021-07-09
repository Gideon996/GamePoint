package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.databinding.FavoriteGameLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.FavoriteGamesHolder;

public class FavoriteGamesAdapter extends RecyclerView.Adapter<FavoriteGamesHolder>{
    private static final String TAG = "FavoriteAdapter";

    private final List<BasicGameInformation> favoriteGames;

    public FavoriteGamesAdapter(List<BasicGameInformation> favoriteGames) {
        Log.d(TAG, "Constructor of Favorite Games");
        this.favoriteGames = favoriteGames;
    }

    @NotNull
    @Override
    public FavoriteGamesHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Create View Holder and binding Layout");
        FavoriteGameLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.favorite_game_layout, parent, false);
        return new FavoriteGamesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull FavoriteGamesHolder holder, int position) {
        Log.i(TAG, "Binding data on View");
        BasicGameInformation basicGameInformation = favoriteGames.get(position);
        holder.bind(basicGameInformation);
    }

    @Override
    public int getItemCount() {
        return favoriteGames.size();
    }

}
