package it.adriano.tumino.gamepoint.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.news.GameNews;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ArrayList<GameNews> list;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;

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
}