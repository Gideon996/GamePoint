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
import it.adriano.tumino.gamepoint.holder.LastSearchedGamesHolder;


public class LastSearchedGamesAdapter extends RecyclerView.Adapter<LastSearchedGamesHolder> {
    public static final String TAG = "SearchGamesAdapter";
    private ArrayList<GameSearchResult> lastSearchedGamesList;

    public LastSearchedGamesAdapter(ArrayList<GameSearchResult> lastSearchedGamesList) {
        Log.i(TAG, "Generazione Last Searched Games Adapter");
        this.lastSearchedGamesList = lastSearchedGamesList;
    }

    @NonNull
    @NotNull
    @Override
    public LastSearchedGamesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Layout");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.last_search_layout, parent, false);
        return new LastSearchedGamesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastSearchedGamesHolder holder, int position) {
        Log.i(TAG, "Riempimento Item");
        if (!lastSearchedGamesList.get(position).getImageURL().isEmpty()) {
            Picasso.get().load(lastSearchedGamesList.get(position).getImageURL()).resize(1500, 1500).onlyScaleDown().into(holder.getImageView());
        } else {
            Log.i(TAG, "Immagine non disponibile, inserimento placeholder");
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(Color.BLACK);
            holder.getImageView().setImageDrawable(gd);
        }
        holder.getTextView1().setText(lastSearchedGamesList.get(position).getTitle());
        holder.getTextView2().setText(lastSearchedGamesList.get(position).getStore());
    }

    @Override
    public int getItemCount() {
        return lastSearchedGamesList.size();
    }
}
