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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import it.adriano.tumino.gamepoint.MainSharedViewModel;
import it.adriano.tumino.gamepoint.adapter.recyclerview.NewsAdapter;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;
import it.adriano.tumino.gamepoint.processes.search.SearchNews;

public class NewsFragment extends Fragment implements AsyncResponse<List<News>> {
    public static final String TAG = "NewsFragment";

    private FragmentNewsBinding binding;
    private MainSharedViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);

        binding.newsShimmerLayout.startShimmer();

        binding.refreshNewsLayout.setOnRefreshListener(updateNews);

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.getHasNews()) {
            Log.i(TAG, "Restore saved news");
            setRecyclerValue(viewModel.getNewsList());
            binding.newsShimmerLayout.stopShimmer();
            binding.newsShimmerLayout.setVisibility(View.GONE);
            binding.newsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            Log.i(TAG, "Starting search news");
            searchNews();
        }
    }

    private void searchNews() {
        SearchNews searchNews = new SearchNews(this);
        searchNews.execute(viewModel.nextPage());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecyclerValue(List<News> newsList) {
        NewsAdapter newsAdapter = new NewsAdapter(newsList, requireActivity());
        binding.newsRecyclerView.setHasFixedSize(true);
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.newsRecyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void processFinish(List<News> newsList) {
        Log.i(TAG, "shows news obtained");
        setRecyclerValue(newsList);
        viewModel.setNewsList(newsList);
        viewModel.setHasNews(true);
        binding.newsShimmerLayout.stopShimmer();
        binding.newsShimmerLayout.setVisibility(View.GONE);
        binding.newsRecyclerView.setVisibility(View.VISIBLE);
    }

    final SwipeRefreshLayout.OnRefreshListener updateNews = () -> {
        Log.i(TAG, "Starting refresh news");
        binding.newsShimmerLayout.setVisibility(View.VISIBLE);
        binding.newsShimmerLayout.startShimmer();
        binding.newsRecyclerView.setVisibility(View.GONE);
        searchNews();
        binding.refreshNewsLayout.setRefreshing(false);
    };
}