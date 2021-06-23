package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;
import it.adriano.tumino.gamepoint.databinding.GameSearchedLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.SearchGameHolder;

public class SearchedGamesAdapter extends RecyclerView.Adapter<SearchGameHolder> implements ClickItemList {
    public static final String TAG = "SearchGamesAdapter";
    private final ArrayList<GameSearchResult> searchedGames;
    private final View view;

    public SearchedGamesAdapter(ArrayList<GameSearchResult> searchedGames, View view) {
        Log.i(TAG, "Generazione Search Games Adapter");
        this.searchedGames = searchedGames;
        this.view = view;
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
        holder.binding.setGameClicked(this);
    }

    @Override
    public int getItemCount() {
        return searchedGames.size();
    }

    @Override
    public void itemClicked(Object game) {
        GameSearchResult gameSearchResult = (GameSearchResult) game;
        Bundle bundle = new Bundle();

        bundle.putParcelable("game", gameSearchResult);
        Navigation.findNavController(view).navigate(R.id.select_action, bundle);

        DBManager dbManager = new DBManager(view.getContext(), DataBaseValues.ULITME_RICERCHE.getName());
        if(!dbManager.checkIfElementsIsOnDataBase(gameSearchResult.getTitle(), gameSearchResult.getStore())) dbManager.save(gameSearchResult.getTitle(), gameSearchResult.getImageURL(), gameSearchResult.getStore(), gameSearchResult.getUrl(), gameSearchResult.getAppID());
        notifyDataSetChanged();
    }
}
