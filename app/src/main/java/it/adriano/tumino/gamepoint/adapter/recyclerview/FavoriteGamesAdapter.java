package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.holder.recyclerview.FavoriteGamesHolder;
import it.adriano.tumino.gamepoint.ui.showgame.GameResultFragment;

public class FavoriteGamesAdapter extends RecyclerView.Adapter<FavoriteGamesHolder> {
    public static final String TAG = "FavoriteAdapter";
    private List<GameSearchResult> favoriteGames;

    public FavoriteGamesAdapter(List<GameSearchResult> favoriteGames) {
        Log.i(TAG, "Generazione Favorite Adapter");
        this.favoriteGames = favoriteGames;
    }

    @NotNull
    @Override
    public FavoriteGamesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Favorite Layout");

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.favorite_game_layout, parent, false);

        return new FavoriteGamesHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteGamesHolder holder, int position) {
        Log.i(TAG, "Riempimento Favorite Item");

        position = position % favoriteGames.size();
        Picasso.get()
                .load(favoriteGames.get(position).getImageURL())
                .resize(500, 500)
                .centerCrop(Gravity.CENTER)
                .into(holder.getImageView());
        holder.getTitle().setText(favoriteGames.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

}
