package it.adriano.tumino.gamepoint.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.data.News;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ArrayList<News> list;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private int lastPage;

    public NewsViewModel() {
        mText = new MutableLiveData<>();
        list = new ArrayList<>();
        mText.setValue("Ultime Notizie");
        lastPage = 1;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setList(List<News> newElements){
        list.addAll(newElements);
    }

    public ArrayList<News> getList(){
        return list;
    }

    public ShimmerFrameLayout getShimmerFrameLayout() {
        return shimmerFrameLayout;
    }

    public void setShimmerFrameLayout(ShimmerFrameLayout shimmerFrameLayout) {
        this.shimmerFrameLayout = shimmerFrameLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public void incrementPage(){
        lastPage++;
    }
}