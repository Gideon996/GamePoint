package it.adriano.tumino.gamepoint.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;
import it.adriano.tumino.gamepoint.backgroundprocesses.CatchNews;
import it.adriano.tumino.gamepoint.adapter.NewsAdapter;


public class NewsFragment extends Fragment {
    public static final String TAG = "NewsFragment";

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;
    private NewsAdapter newsAdapter;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Generazione vista News");
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textView = binding.textNews;
        newsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        RecyclerView recyclerView = binding.recycleView;

        shimmerFrameLayout = binding.shimmerLayout;

        newsViewModel.setRecyclerView(recyclerView);
        newsViewModel.setShimmerFrameLayout(shimmerFrameLayout);

        shimmerFrameLayout.startShimmer();

        int currentPage = 1;
        newsAdapter = new NewsAdapter(newsViewModel.getList(), getActivity(), currentPage, newsViewModel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(newsAdapter);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Start vista News");
        int initialPage = 1;
        CatchNews catchNews = new CatchNews(newsViewModel, newsAdapter);
        catchNews.execute(initialPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}