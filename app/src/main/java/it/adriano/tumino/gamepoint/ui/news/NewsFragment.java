package it.adriano.tumino.gamepoint.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
        viewModel.getCurrentNewsPage().setValue(0);

        binding.newsShimmerLayout.startShimmer();

        binding.refreshNewsLayout.setOnRefreshListener(() -> {
            binding.newsShimmerLayout.setVisibility(View.VISIBLE);
            binding.newsShimmerLayout.startShimmer();
            binding.newsRecyclerView.setVisibility(View.GONE);
            searchNews();
            binding.refreshNewsLayout.setRefreshing(false);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.getHasNews().getValue() != null && viewModel.getHasNews().getValue()) {
            setRecyclerValue(viewModel.getNewsList().getValue());
            binding.newsShimmerLayout.stopShimmer();
            binding.newsShimmerLayout.setVisibility(View.GONE);
            binding.newsRecyclerView.setVisibility(View.VISIBLE);
        } else {
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
        setRecyclerValue(newsList);
        viewModel.setNewsList(newsList);
        viewModel.getHasNews().setValue(true);
        binding.newsShimmerLayout.stopShimmer();
        binding.newsShimmerLayout.setVisibility(View.GONE);
        binding.newsRecyclerView.setVisibility(View.VISIBLE);
    }
}