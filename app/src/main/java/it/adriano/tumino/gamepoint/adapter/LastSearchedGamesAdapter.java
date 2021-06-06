package it.adriano.tumino.gamepoint.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.databinding.LastSearchLayoutBinding;
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
        LastSearchLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.last_search_layout, parent, false);
        return new LastSearchedGamesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastSearchedGamesHolder holder, int position) {
        Log.i(TAG, "Riempimento Item");
        GameSearchResult gameSearchResult = lastSearchedGamesList.get(position);
        holder.bind(gameSearchResult);
    }

    @Override
    public int getItemCount() {
        return lastSearchedGamesList.size();
    }
}
