package it.adriano.tumino.gamepoint.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
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