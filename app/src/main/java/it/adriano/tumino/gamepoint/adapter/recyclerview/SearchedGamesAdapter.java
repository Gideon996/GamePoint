package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.databinding.GameSearchedLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.SearchGameHolder;

public class SearchedGamesAdapter extends RecyclerView.Adapter<SearchGameHolder> {
    private final List<BasicGameInformation> searchedGames;

    public SearchedGamesAdapter(List<BasicGameInformation> searchedGames) {
        this.searchedGames = searchedGames;
    }

    @NotNull
    @Override
    public SearchGameHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        GameSearchedLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.game_searched_layout, parent, false);
        return new SearchGameHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull SearchGameHolder holder, int position) {
        BasicGameInformation basicGameInformation = searchedGames.get(position);
        holder.bind(basicGameInformation);
    }

    @Override
    public int getItemCount() {
        return searchedGames.size();
    }

}
