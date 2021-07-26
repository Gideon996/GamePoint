package it.adriano.tumino.gamepoint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> hasOffers;
    private final MutableLiveData<List<GameOffers>> offersList;

    public SharedViewModel() {
        hasOffers = new MutableLiveData<>();
        offersList = new MutableLiveData<>();

        hasOffers.setValue(false);
    }

    public MutableLiveData<Boolean> getHasOffers() {
        return hasOffers;
    }

    public MutableLiveData<List<GameOffers>> getOffersList() {
        return offersList;
    }

    public void setHasOffers(boolean value) {
        hasOffers.setValue(value);
    }

    public void setOffersList(List<GameOffers> offersList) {
        this.offersList.setValue(offersList);
    }


}
