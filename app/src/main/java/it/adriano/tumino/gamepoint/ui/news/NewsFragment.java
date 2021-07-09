package it.adriano.tumino.gamepoint.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import it.adriano.tumino.gamepoint.adapter.recyclerview.NewsAdapter;
import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;
import it.adriano.tumino.gamepoint.backgroundprocesses.CatchNews;

public class NewsFragment extends Fragment implements AsyncResponse<List<News>> {
    public static final String TAG = "NewsFragment";

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "");
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.newsRecycleView;
        shimmerFrameLayout = binding.newsShimmerLayout;

        shimmerFrameLayout.startShimmer();
        int initialPage = 1;
        CatchNews catchNews = new CatchNews(this);
        catchNews.execute(initialPage);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void processFinish(List<News> newsList) {
        int currentPage = 1;
        NewsAdapter newsAdapter = new NewsAdapter(newsList, currentPage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(newsAdapter);

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}