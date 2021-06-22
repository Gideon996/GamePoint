package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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
import it.adriano.tumino.gamepoint.databinding.LastSearchLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.LastSearchedGamesHolder;


public class LastSearchedGamesAdapter extends RecyclerView.Adapter<LastSearchedGamesHolder> implements ClickItemList {
    public static final String TAG = "SearchGamesAdapter";
    private final ArrayList<GameSearchResult> lastSearchedGamesList;
    private final View view;
    private ImageButton cancellButton;

    public LastSearchedGamesAdapter(ArrayList<GameSearchResult> lastSearchedGamesList, View view) {
        Log.i(TAG, "Generazione Last Searched Games Adapter");
        this.lastSearchedGamesList = lastSearchedGamesList;
        this.view = view;
    }

    @NonNull
    @NotNull
    @Override
    public LastSearchedGamesHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Layout");
        LastSearchLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.last_search_layout, parent, false);
        cancellButton = binding.cancellImageButton;
        return new LastSearchedGamesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastSearchedGamesHolder holder, int position) {
        Log.i(TAG, "Riempimento Item");
        GameSearchResult gameSearchResult = lastSearchedGamesList.get(position);
        holder.bind(gameSearchResult);
        holder.binding.setGameClicked(this);
        cancellButton.setOnClickListener(v -> {
            Log.i(TAG, "Rimozione del gioco: " + gameSearchResult.getTitle());
            DBManager dbManager = new DBManager(view.getContext(), DataBaseValues.ULITME_RICERCHE.getName());
            boolean result = dbManager.deleteFromNameAndStore(gameSearchResult.getTitle(), gameSearchResult.getStore());
            if (result) {
                lastSearchedGamesList.remove(position);
            } else {
                Log.e(TAG, "Impossibile rimuovere il gioco: " + gameSearchResult.getTitle());
                Toast.makeText(view.getContext(), "Impossibile cancellare l'elemento selezionato", Toast.LENGTH_SHORT).show();
            }

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, lastSearchedGamesList.size());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return lastSearchedGamesList.size();
    }

    @Override
    public void itemClicked(Object game) {
        GameSearchResult gameSearchResult = (GameSearchResult) game;
        Bundle bundle = new Bundle();

        bundle.putParcelable("game", gameSearchResult);
        Navigation.findNavController(view).navigate(R.id.select_action, bundle);
    }
}
