package it.adriano.tumino.gamepoint.ui.showgame;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.adriano.tumino.gamepoint.data.storegame.StoreGame;

public class GameResultViewModel extends ViewModel {

    private MutableLiveData<Integer> currentFragment;
    private MutableLiveData<StoreGame> result;
    private MutableLiveData<Boolean> hasResult;
    private MutableLiveData<Boolean> isEmpty;

    public GameResultViewModel() {
        currentFragment = new MutableLiveData<>();
        result = new MutableLiveData<>();
        hasResult = new MutableLiveData<>();
        isEmpty = new MutableLiveData<>();

        hasResult.setValue(false);
        isEmpty.setValue(false);
        currentFragment.setValue(0);
    }

    public MutableLiveData<Integer> getCurrentFragment() {
        if (currentFragment == null) {
            currentFragment = new MutableLiveData<>();
        }
        return currentFragment;
    }

    public MutableLiveData<StoreGame> getResult() {
        if (result == null) {
            result = new MutableLiveData<>();
        }
        return result;
    }

    public MutableLiveData<Boolean> getHasResult(){
        if (hasResult == null) {
            hasResult = new MutableLiveData<>();
        }
        return hasResult;
    }

    public MutableLiveData<Boolean> getIsEmpty() {
        if (isEmpty == null) {
            isEmpty = new MutableLiveData<>();
        }
        return isEmpty;
    }
}
