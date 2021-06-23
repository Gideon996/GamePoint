package it.adriano.tumino.gamepoint.ui.home;

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

import java.util.List;

import it.adriano.tumino.gamepoint.adapter.recyclerview.FavoriteGamesAdapter;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;
import it.adriano.tumino.gamepoint.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ShimmerFrameLayout shimmerFrameLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Generazione vista Home");
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        shimmerFrameLayout = binding.shimmerFavoriteLayout;
        TextView nessunElemento = binding.nessunElementoNelDB;
        shimmerFrameLayout.startShimmer();
        homeViewModel.setShimmerFrameLayout(shimmerFrameLayout);

        RecyclerView recyclerView = binding.favoriteGamesRecycleView;

        //Center Item in RecycleView
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);

        //Create Circular Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.scrollToPosition(Integer.MAX_VALUE / 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Finalize RecycleView inizialization
        recyclerView.setHasFixedSize(true);
        DBManager dbManager = new DBManager(this.getContext(), DataBaseValues.FAVORITE_TABLE.getName());
        List<GameSearchResult> favoriteGames = dbManager.getAll();
        if (favoriteGames.size() == 0) {
            nessunElemento.setVisibility(View.VISIBLE);
        } else {
            nessunElemento.setVisibility(View.GONE);
            FavoriteGamesAdapter favoriteGamesAdapter = new FavoriteGamesAdapter(favoriteGames);
            recyclerView.setAdapter(favoriteGamesAdapter);
        }
        homeViewModel.setRecyclerView(recyclerView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Start della vista Home");
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        homeViewModel.getRecyclerView().setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}