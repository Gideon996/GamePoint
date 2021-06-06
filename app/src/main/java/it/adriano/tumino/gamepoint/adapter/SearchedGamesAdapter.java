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
import it.adriano.tumino.gamepoint.databinding.GameSearchedLayoutBinding;
import it.adriano.tumino.gamepoint.holder.SearchGameHolder;

public class SearchedGamesAdapter extends RecyclerView.Adapter<SearchGameHolder> {
    public static final String TAG = "SearchGamesAdapter";
    private ArrayList<GameSearchResult> searchedGames;

    public SearchedGamesAdapter(ArrayList<GameSearchResult> searchedGames) {
        Log.i(TAG, "Generazione Search Games Adapter");
        this.searchedGames = searchedGames;
    }

    @NonNull
    @NotNull
    @Override
    public SearchGameHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Layout");
        GameSearchedLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.game_searched_layout, parent, false);
        return new SearchGameHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchGameHolder holder, int position) {
        Log.i(TAG, "Riempimento Item");
        GameSearchResult gameSearchResult = searchedGames.get(position);
        holder.bind(gameSearchResult);
        //inserire un click
    }

    @Override
    public int getItemCount() {
        return searchedGames.size();
    }
}
