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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.news.CatchAndShowNews;
import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;
import it.adriano.tumino.gamepoint.news.NewsAdapter;
import it.adriano.tumino.gamepoint.news.NewsAdapterRecycle;


public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;

    private int currentPage = 1;

    private RecyclerView recyclerView;
    private NewsAdapterRecycle newsAdapterRecycle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNews;
        newsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        /*View footer = inflater.inflate(R.layout.footer_layout, null);
        listView.addFooterView(footer);*/

        recyclerView = binding.recycleView;
        newsAdapterRecycle = new NewsAdapterRecycle(newsViewModel.getList(), getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(newsAdapterRecycle);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        CatchAndShowNews nuovo = new CatchAndShowNews(newsViewModel, newsAdapterRecycle);
        nuovo.execute(currentPage, currentPage);

        /*Button visualizzaAltro = getView().findViewById(R.id.visualizzaAltro);
        visualizzaAltro.setOnClickListener(v -> {
            currentPage++;
            CatchAndShowNews update = new CatchAndShowNews(listView, getContext(), getActivity(), newsViewModel, newsAdapter);
            update.execute(currentPage, currentPage);
        });*/


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}