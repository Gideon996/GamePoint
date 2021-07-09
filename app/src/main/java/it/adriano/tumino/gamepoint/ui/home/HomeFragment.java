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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.adriano.tumino.gamepoint.adapter.recyclerview.FavoriteGamesAdapter;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;
import it.adriano.tumino.gamepoint.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Home Generation");
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //shimmerFrameLayout = binding.shimmerFavoriteLayout;
        TextView noFavoriteGamesTextView = binding.noFavoriteGamesTextView;
        //private ShimmerFrameLayout shimmerFrameLayout;
        RecyclerView recyclerView = binding.favoriteGamesRecycleView;

        //shimmerFrameLayout.startShimmer(); //start shimmer for favorite games

        //Create Circular Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DBManager dbManager = new DBManager(this.getContext(), DBUtils.FAVORITE_TABLE_TITLE);
        List<BasicGameInformation> favoriteGames = dbManager.getAllElements();
        if (favoriteGames.size() != 0) {
            FavoriteGamesAdapter favoriteGamesAdapter = new FavoriteGamesAdapter(favoriteGames);
            recyclerView.setAdapter(favoriteGamesAdapter);
            noFavoriteGamesTextView.setVisibility(View.GONE);
        }
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Start Home View");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}