package it.adriano.tumino.gamepoint.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import it.adriano.tumino.gamepoint.news.CatchAndShowNews;
import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;
import it.adriano.tumino.gamepoint.news.NewsAdapterRecycle;


public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;
    private NewsAdapterRecycle newsAdapterRecycle;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNews;
        newsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        RecyclerView recyclerView = binding.recycleView;
        shimmerFrameLayout = binding.shimmerLayout;

        newsViewModel.setRecyclerView(recyclerView);
        newsViewModel.setShimmerFrameLayout(shimmerFrameLayout);

        shimmerFrameLayout.startShimmer();

        int currentPage = 1;
        newsAdapterRecycle = new NewsAdapterRecycle(newsViewModel.getList(), getActivity(), currentPage, newsViewModel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(newsAdapterRecycle);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        int initialPage = 1;
        CatchAndShowNews nuovo = new CatchAndShowNews(newsViewModel, newsAdapterRecycle);
        nuovo.execute(initialPage, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}