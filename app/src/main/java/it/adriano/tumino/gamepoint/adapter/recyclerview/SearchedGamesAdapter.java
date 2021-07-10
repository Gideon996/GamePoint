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
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;
import it.adriano.tumino.gamepoint.databinding.GameSearchedLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.SearchGameHolder;

public class SearchedGamesAdapter extends RecyclerView.Adapter<SearchGameHolder> implements ClickItemList {
    public static final String TAG = "SearchGamesAdapter";
    private final ArrayList<BasicGameInformation> searchedGames;
    private final View view;

    public SearchedGamesAdapter(ArrayList<BasicGameInformation> searchedGames, View view) {
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
        BasicGameInformation basicGameInformation = searchedGames.get(position);
        holder.bind(basicGameInformation);
        holder.binding.setGameClicked(this);
    }

    @Override
    public int getItemCount() {
        return searchedGames.size();
    }

    @Override
    public void itemClicked(Object game) {
        BasicGameInformation basicGameInformation = (BasicGameInformation) game;
        Bundle bundle = new Bundle();

        bundle.putParcelable("game", basicGameInformation);
        Log.d("TEST", basicGameInformation.getTitle() + " " + basicGameInformation.getAppID() + " " + basicGameInformation.getUrl());
        Navigation.findNavController(view).navigate(R.id.select_action, bundle);

        DBManager dbManager = new DBManager(view.getContext(), DBUtils.LAST_RESEARCH_TABLE_TITLE);
        if(!dbManager.checkIfElementsIsOnDataBase(basicGameInformation.getTitle(), basicGameInformation.getStore())) dbManager.save(basicGameInformation);
        notifyDataSetChanged();
    }
}
