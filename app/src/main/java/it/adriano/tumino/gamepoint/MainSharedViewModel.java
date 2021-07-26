package it.adriano.tumino.gamepoint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.data.News;

public class MainSharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> hasOffers;
    private final MutableLiveData<Boolean> hasNews;
    private final MutableLiveData<List<GameOffers>> offersList;
    private final MutableLiveData<List<News>> newsList;
    private final MutableLiveData<Integer> currentNewsPage;


    public MainSharedViewModel() {
        hasOffers = new MutableLiveData<>();
        hasNews = new MutableLiveData<>();
        offersList = new MutableLiveData<>();
        newsList = new MutableLiveData<>();
        currentNewsPage = new MutableLiveData<>();


        offersList.setValue(new ArrayList<>());
        newsList.setValue(new ArrayList<>());
        hasOffers.setValue(false);
        hasNews.setValue(false);
        currentNewsPage.setValue(0);
    }

    public MutableLiveData<Boolean> getHasOffers() {
        return hasOffers;
    }

    public MutableLiveData<Boolean> getHasNews() {
        return hasNews;
    }

    public MutableLiveData<List<GameOffers>> getOffersList() {
        return offersList;
    }

    public MutableLiveData<List<News>> getNewsList() {
        return newsList;
    }

    public MutableLiveData<Integer> getCurrentNewsPage() {
        return currentNewsPage;
    }

    public void setHasOffers(boolean value) {
        hasOffers.setValue(value);
    }

    public void setHasNews(boolean value) {
        hasNews.setValue(value);
    }

    public void setOffersList(List<GameOffers> offersList) {
        List<GameOffers> list = this.offersList.getValue();
        list.addAll(offersList);
        this.offersList.setValue(list);
    }

    public void setNewsList(List<News> newsList) {
        List<News> list = this.newsList.getValue();
        list.addAll(newsList);
        this.newsList.setValue(list);
    }

    public int nextPage() {
        int currentPage = currentNewsPage.getValue();
        currentPage = currentPage + 1;
        currentNewsPage.setValue(currentPage);
        return currentPage;
    }

}
