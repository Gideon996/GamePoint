package it.adriano.tumino.gamepoint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.FavoriteGames;
import it.adriano.tumino.gamepoint.holder.UltimeRicercheHolder;


public class UltimeRicercheAdapter extends RecyclerView.Adapter<UltimeRicercheHolder>{

    private ArrayList<FavoriteGames> favoriteGames;

    public UltimeRicercheAdapter(ArrayList<FavoriteGames> favoriteGames) {
        this.favoriteGames = favoriteGames;
    }

    @NonNull
    @NotNull
    @Override
    public UltimeRicercheHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ultime_ricerche_layout, parent, false);
        return new UltimeRicercheHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UltimeRicercheHolder holder, int position) {
        Picasso.get().load(favoriteGames.get(position).getImageURL()).resize(1500, 1500).onlyScaleDown().into(holder.getImageView());
        holder.getTextView1().setText(favoriteGames.get(position).getTitle());
        holder.getTextView2().setText(favoriteGames.get(position).getStore());
    }

    @Override
    public int getItemCount() {
        return favoriteGames.size();
    }
}
