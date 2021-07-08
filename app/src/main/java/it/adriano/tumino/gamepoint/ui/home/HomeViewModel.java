package it.adriano.tumino.gamepoint.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<BasicGameInformation>> gameSearchResultLiveData;

    public HomeViewModel() {
        gameSearchResultLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<BasicGameInformation>> getGameSearchResultLiveData() {
        return gameSearchResultLiveData;
    }
}