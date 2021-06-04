package it.adriano.tumino.gamepoint.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.holder.SearchGamesHolder;

public class SearchGamesAdapter extends RecyclerView.Adapter<SearchGamesHolder> {
    public static final String TAG = "SearchGamesAdapter";
    private ArrayList<GameSearchResult> searchedGames;

    public SearchGamesAdapter(ArrayList<GameSearchResult> searchedGames) {
        Log.i(TAG, "Generazione Search Games Adapter");
        this.searchedGames = searchedGames;
    }

    @NonNull
    @NotNull
    @Override
    public SearchGamesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Layout");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ultime_ricerche_layout, parent, false);
        return new SearchGamesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchGamesHolder holder, int position) {
        Log.i(TAG, "Riempimento Item");
        if (!searchedGames.get(position).getImageURL().isEmpty()) {
            Picasso.get().load(searchedGames.get(position).getImageURL()).resize(1500, 1500).onlyScaleDown().into(holder.getImageView());
        } else {
            Log.i(TAG, "Immagine non disponibile, inserimento placeholder");
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(Color.BLACK);
            holder.getImageView().setImageDrawable(gd);
        }
        holder.getTitle().setText(searchedGames.get(position).getTitle());
        holder.getAltro().setText(searchedGames.get(position).getAppID());
    }

    @Override
    public int getItemCount() {
        return searchedGames.size();
    }
}
