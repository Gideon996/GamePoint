package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;
import it.adriano.tumino.gamepoint.holder.recyclerview.FavoriteGamesHolder;
import it.adriano.tumino.gamepoint.ui.showgame.GameResultFragment;

public class FavoriteGamesAdapter extends RecyclerView.Adapter<FavoriteGamesHolder> {
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

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.favorite_game_layout, parent, false);

        return new FavoriteGamesHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteGamesHolder holder, int position) {
        Log.i(TAG, "Riempimento Favorite Item");
        if (favoriteGames.size() != 0) {
            position = position % favoriteGames.size();
            Picasso.get()
                    .load(favoriteGames.get(position).getImageURL())
                    .resize(500, 500)
                    .centerCrop(Gravity.CENTER)
                    .into(holder.getImageView());
            holder.getTitle().setText(favoriteGames.get(position).getTitle());

            int finalPosition = position;
            holder.getLayout().setOnClickListener(v -> {
                GameSearchResult gameSearchResult = favoriteGames.get(finalPosition);
                Bundle bundle = new Bundle();
                bundle.putParcelable("game", gameSearchResult);
                GameResultFragment fragment = new GameResultFragment(this);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.homeLayout, fragment)
                        .setReorderingAllowed(true)
                        .commit();
            });
        }else {
            holder.getTitle().setText("Nessun Elemento Presente");
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
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

}
