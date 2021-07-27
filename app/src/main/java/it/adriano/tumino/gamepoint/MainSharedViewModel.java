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

    private final MutableLiveData<Integer> currentNewsPage;
    private final MutableLiveData<String> titleSearched;


    public MainSharedViewModel() {
        hasOffers = new MutableLiveData<>();
        hasNews = new MutableLiveData<>();
        hasResearch = new MutableLiveData<>();

        offersList = new MutableLiveData<>();
        newsList = new MutableLiveData<>();
        searchedList = new MutableLiveData<>();

        currentNewsPage = new MutableLiveData<>();
        titleSearched = new MutableLiveData<>();

        hasOffers.setValue(false);
        hasNews.setValue(false);
        hasResearch.setValue(false);

        offersList.setValue(new ArrayList<>());
        newsList.setValue(new ArrayList<>());

        currentNewsPage.setValue(0);
        titleSearched.setValue("");
    }

    public boolean getHasOffers() {
        return hasOffers.getValue();
    }

    public boolean getHasNews() {
        return hasNews.getValue();
    }

    public boolean getHasResearch() {
        return hasResearch.getValue();
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
        return currentNewsPage.getValue();
    }

    public String getTitle() {
        return titleSearched.getValue();
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
        list.addAll(offersList);
        this.offersList.setValue(list);
    }

    public void setNewsList(List<News> newsList) {
        List<News> list = this.newsList.getValue();
        list.addAll(newsList);
        this.newsList.setValue(list);
    }

    public void setSearchedList(List<BasicGameInformation> list) {
        searchedList.setValue(list);
    }

    public int nextPage() {
        int currentPage = currentNewsPage.getValue();
        currentPage = currentPage + 1;
        currentNewsPage.setValue(currentPage);
        return currentPage;
    }

    public void setTitle(String title) {
        titleSearched.setValue(title);
    }

}
