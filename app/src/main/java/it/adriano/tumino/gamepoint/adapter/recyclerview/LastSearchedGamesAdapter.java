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
import it.adriano.tumino.gamepoint.databinding.LastSearchLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.LastSearchedGamesHolder;


public class LastSearchedGamesAdapter extends RecyclerView.Adapter<LastSearchedGamesHolder> {
    private final List<BasicGameInformation> lastSearchedGamesList;

    public LastSearchedGamesAdapter(List<BasicGameInformation> lastSearchedGamesList) {
        this.lastSearchedGamesList = lastSearchedGamesList;
    }

    @NonNull
    @NotNull
    @Override
    public LastSearchedGamesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LastSearchLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.last_search_layout, parent, false);
        return new LastSearchedGamesHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastSearchedGamesHolder holder, int position) {
        BasicGameInformation basicGameInformation = lastSearchedGamesList.get(position);
        holder.bind(basicGameInformation);
    }

    @Override
    public int getItemCount() {
        return lastSearchedGamesList.size();
    }

    public void deleteItem(int position) {
        lastSearchedGamesList.remove(position);
        notifyItemRemoved(position);
    }
}
