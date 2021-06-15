package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.holder.recyclerview.FavoriteGamesHolder;

public class FavoriteGamesAdapter extends RecyclerView.Adapter<FavoriteGamesHolder> {
    public static final String TAG = "FavoriteAdapter";

    public FavoriteGamesAdapter() {
        Log.i(TAG, "Generazione Favorite Adapter");
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

        //position = position % favoriteGames.length;

        /*Picasso.get()
                .load(favoriteGames[position].getImageURL())
                .resize(500, 500)
                .centerCrop(Gravity.CENTER)
                .into(holder.getImageView());
        holder.getTitle().setText(favoriteGames[position].getTitle());*/
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

}
