package it.adriano.tumino.gamepoint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.data.News;

public class MainSharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> hasOffers;
    private final MutableLiveData<Boolean> hasNews;
    private final MutableLiveData<Boolean> hasResearch;

    private final MutableLiveData<List<GameOffers>> offersList;
    private final MutableLiveData<List<News>> newsList;
    private final MutableLiveData<List<BasicGameInformation>> searchedList;
    private final MutableLiveData<String> searchedTitle;

    private final MutableLiveData<Integer> currentNewsPage;

    public MainSharedViewModel() {
        hasOffers = new MutableLiveData<>();
        hasNews = new MutableLiveData<>();
        hasResearch = new MutableLiveData<>();

        offersList = new MutableLiveData<>();
        newsList = new MutableLiveData<>();
        searchedList = new MutableLiveData<>();
        searchedTitle = new MutableLiveData<>();

        currentNewsPage = new MutableLiveData<>();

        hasOffers.setValue(false);
        hasNews.setValue(false);
        hasResearch.setValue(false);

        offersList.setValue(new ArrayList<>());
        newsList.setValue(new ArrayList<>());

        currentNewsPage.setValue(0);
    }

    public boolean getHasOffers() {
        if (hasOffers.getValue() != null) return hasOffers.getValue();

        return false;
    }

    public boolean getHasNews() {
        if (hasNews.getValue() != null) return hasNews.getValue();

        return false;
    }

    public boolean getHasResearch() {
        if (hasResearch.getValue() != null) return hasResearch.getValue();

        return false;
    }

    public List<GameOffers> getOffersList() {
        return offersList.getValue();
    }

    public List<News> getNewsList() {
        return newsList.getValue();
    }

    public List<BasicGameInformation> getSearchedList() {
        return searchedList.getValue();
    }

    public int getCurrentPage() {
        if (currentNewsPage.getValue() != null) return currentNewsPage.getValue();
        return 0;
    }

    public String getSearchTitle() {
        return searchedTitle.getValue();
    }

    public void setHasOffers(boolean value) {
        hasOffers.setValue(value);
    }

    public void setHasNews(boolean value) {
        hasNews.setValue(value);
    }

    public void setHasResearch(boolean value) {
        hasResearch.setValue(value);
    }

    public void setOffersList(List<GameOffers> offersList) {
        List<GameOffers> list = this.offersList.getValue();
        if (list != null) {
            list.addAll(offersList);
            this.offersList.setValue(list);
        } else {
            this.offersList.setValue(new ArrayList<>());
        }
    }

    public void setNewsList(List<News> newsList) {
        List<News> list = this.newsList.getValue();
        if (list != null) {
            list.addAll(newsList);
            this.newsList.setValue(list);
        } else {
            this.newsList.setValue(new ArrayList<>());
        }
    }

    public void setSearchedList(List<BasicGameInformation> list) {
        searchedList.setValue(list);
    }

    public void setSearchedTitle(String title) {
        searchedTitle.setValue(title);
    }

    public int nextPage() {
        int currentPage = 0;
        if (currentNewsPage.getValue() != null) currentPage = currentNewsPage.getValue();
        currentPage = currentPage + 1;
        currentNewsPage.setValue(currentPage);
        return currentPage;
    }

}
