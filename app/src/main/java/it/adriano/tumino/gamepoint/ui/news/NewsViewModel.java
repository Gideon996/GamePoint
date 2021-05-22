package it.adriano.tumino.gamepoint.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.news.GameNews;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ArrayList<GameNews> list;

    public NewsViewModel() {
        mText = new MutableLiveData<>();
        list = new ArrayList<>();
        mText.setValue("Ultime Notizie");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setList(List<GameNews> newElements){
        list.addAll(newElements);
    }

    public ArrayList<GameNews> getList(){
        return list;
    }
}