package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.os.Bundle;
import android.util.Log;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;
import it.adriano.tumino.gamepoint.databinding.GameSearchedLayoutBinding;

public class SearchGameHolder extends RecyclerView.ViewHolder {
    public GameSearchedLayoutBinding binding;

    public SearchGameHolder(GameSearchedLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.game, obj); //nome della variabile da usare
        binding.executePendingBindings();
        binding.searchedGameLayout.setOnClickListener(v -> {
            BasicGameInformation basicGameInformation = (BasicGameInformation) obj;
            Bundle bundle = new Bundle();

            bundle.putParcelable("game", basicGameInformation);
            Log.d("TEST", basicGameInformation.getTitle() + " " + basicGameInformation.getAppID() + " " + basicGameInformation.getUrl());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.select_action, bundle);

            DBManager dbManager = new DBManager(binding.getRoot().getContext(), DBUtils.LAST_RESEARCH_TABLE_TITLE);
            if (!dbManager.checkIfElementsIsOnDataBase(basicGameInformation.getTitle(), basicGameInformation.getStore())) {
                dbManager.save(basicGameInformation);
            }
        });
    }
}
