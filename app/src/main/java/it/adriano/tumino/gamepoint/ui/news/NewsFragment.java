package it.adriano.tumino.gamepoint.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.news.CatchAndShowNews;
import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;
import it.adriano.tumino.gamepoint.news.NewsAdapter;


public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;

    private ListView listView;
    private int currentPage = 1;

    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = binding.newsListView;
        final TextView textView = binding.textNews;
        newsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        newsAdapter = new NewsAdapter(getContext(), newsViewModel.getList(), getActivity());
        listView.setAdapter(newsAdapter);
        View footer = inflater.inflate(R.layout.footer_layout, null);
        listView.addFooterView(footer);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        CatchAndShowNews catchAndShowNews = new CatchAndShowNews(listView, getContext(), getActivity(), newsViewModel, newsAdapter);
        catchAndShowNews.execute(currentPage, currentPage);
        Button visualizzaAltro = getView().findViewById(R.id.visualizzaAltro);
        visualizzaAltro.setOnClickListener(v -> {
            currentPage++;
            CatchAndShowNews update = new CatchAndShowNews(listView, getContext(), getActivity(), newsViewModel, newsAdapter);
            update.execute(currentPage, currentPage);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}