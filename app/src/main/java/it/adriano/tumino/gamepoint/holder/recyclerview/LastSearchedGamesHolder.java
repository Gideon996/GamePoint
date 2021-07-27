package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.os.Bundle;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.LastSearchedGamesAdapter;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;
import it.adriano.tumino.gamepoint.databinding.LastSearchLayoutBinding;

public class LastSearchedGamesHolder extends RecyclerView.ViewHolder {

    private final LastSearchLayoutBinding binding;
    private final LastSearchedGamesAdapter adapter;
    public LastSearchedGamesHolder(LastSearchLayoutBinding binding, LastSearchedGamesAdapter adapter) {
        super(binding.getRoot());
        this.binding = binding;
        this.adapter = adapter;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.lastGames, obj);
        binding.executePendingBindings();
        binding.lastGameLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("game", (BasicGameInformation) obj);

            Navigation.findNavController(binding.getRoot()).navigate(R.id.navigate_to_searched, bundle);
        });

        binding.cancellImageButton.setOnClickListener(v -> {
            BasicGameInformation basicGameInformation = (BasicGameInformation) obj;
            int position = getAdapterPosition();
            DBManager dbManager = new DBManager(binding.cancellImageButton.getContext(), DBUtils.LAST_RESEARCH_TABLE_TITLE);
            boolean result = dbManager.deleteWithNameAndStore(basicGameInformation.getTitle(), basicGameInformation.getStore());
            if (result) {
                adapter.deleteItem(position);
                Toast.makeText(binding.cancellImageButton.getContext(), R.string.game_deleted, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
