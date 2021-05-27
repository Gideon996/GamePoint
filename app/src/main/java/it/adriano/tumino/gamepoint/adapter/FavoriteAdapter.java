package it.adriano.tumino.gamepoint.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.holder.FavoriteHolder;
import it.adriano.tumino.gamepoint.data.FavoriteGames;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteHolder> {
    public static final String TAG = "FavoriteAdapter";

    public FavoriteAdapter() {
        Log.i(TAG, "Generazione Favorite Adapter");
    }

    @NotNull
    @Override
    public FavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Favorite Layout");

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.favorite_game_layout, parent, false);

        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteHolder holder, int position) {
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
