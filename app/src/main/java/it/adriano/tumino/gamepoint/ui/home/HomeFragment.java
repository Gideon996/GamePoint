package it.adriano.tumino.gamepoint.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.adapter.recyclerview.FavoriteGamesAdapter;
import it.adriano.tumino.gamepoint.adapter.recyclerview.OffersAdapter;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.databinding.FragmentHomeBinding;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.search.SearchOffers;
import it.adriano.tumino.gamepoint.MainSharedViewModel;

public class HomeFragment extends Fragment implements AsyncResponse<List<GameOffers>> {
    public static final String TAG = "HomeFragment";

    private MainSharedViewModel mainSharedViewModel;
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainSharedViewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.favoriteShimmerLayout.startShimmer();
        binding.offersShimmerLayout.startShimmer();

        if (mAuth.getUid() != null) onSnapshotFavoritesGame(mAuth.getUid());

        binding.refreshOffersList.setOnRefreshListener(updateOffers);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainSharedViewModel.getHasOffers()) {
            Log.i(TAG, "List of saved offers, initialization of the list");
            setOffersList(mainSharedViewModel.getOffersList());
            binding.offersShimmerLayout.setVisibility(View.GONE);
            binding.offersTitle.setVisibility(View.VISIBLE);
            binding.offersList.setVisibility(View.VISIBLE);
        } else {
            Log.i(TAG, "No value saved, I start the search");
            binding.offersShimmerLayout.setVisibility(View.VISIBLE);
            binding.offersShimmerLayout.startShimmer();
            binding.offersTitle.setVisibility(View.GONE);
            binding.offersList.setVisibility(View.GONE);
            startSearchOffers();
        }
    }

    public void startSearchOffers() {
        Log.i(TAG, "Start search");
        SearchOffers searchOffers = new SearchOffers();
        searchOffers.delegate = this;
        searchOffers.execute();
    }

    @Override
    public void processFinish(List<GameOffers> result) {
        binding.offersShimmerLayout.stopShimmer();
        binding.offersShimmerLayout.setVisibility(View.GONE);
        binding.offersTitle.setVisibility(View.VISIBLE);
        binding.offersList.setVisibility(View.VISIBLE);
        if (result != null && result.size() > 0) {
            setOffersList(result);
            Log.i(TAG, "Save state of fragment");
            mainSharedViewModel.setHasOffers(true);
            mainSharedViewModel.setOffersList(result);
        } else {
            Log.i(TAG, "No offers");
            binding.offersTitle.setVisibility(View.GONE);
            binding.offersList.setVisibility(View.GONE);
        }
    }

    private void setOffersList(List<GameOffers> offersList) {
        OffersAdapter offersAdapter = new OffersAdapter(offersList);
        binding.offersList.setAdapter(offersAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        binding.offersList.addItemDecoration(dividerItemDecoration);
    }

    private void onSnapshotFavoritesGame(@NonNull String userID) {
        if (!userID.isEmpty()) {
            firebaseFirestore.collection("Users")
                    .document(userID)
                    .collection("Favorites")
                    .addSnapshotListener((value, e) -> {
                        if (e != null) {
                            Log.e(TAG, "Listen failed.", e);
                            return;
                        }

                        List<BasicGameInformation> comments = new ArrayList<>();
                        if (value != null) {
                            for (QueryDocumentSnapshot doc : value) {
                                BasicGameInformation comment = doc.toObject(BasicGameInformation.class);
                                comments.add(comment);
                            }
                        }
                        Log.i(TAG, "I show the favorites obtained");
                        visualizeAllFavoriteGames(comments);
                    });
        }
    }

    private void visualizeAllFavoriteGames(List<BasicGameInformation> list) {
        binding.favoriteShimmerLayout.stopShimmer();
        binding.favoriteShimmerLayout.setVisibility(View.GONE);
        if (list.size() != 0) {
            binding.favoriteGamesList.setVisibility(View.VISIBLE);
            binding.favoriteTitle.setVisibility(View.VISIBLE);
            FavoriteGamesAdapter favoriteGamesAdapter = new FavoriteGamesAdapter(list);
            binding.favoriteGamesList.setAdapter(favoriteGamesAdapter);
        } else {
            Log.i(TAG, "No favorite game");
            binding.favoriteTitle.setVisibility(View.GONE);
            binding.favoriteGamesList.setVisibility(View.GONE);
        }
    }

    final SwipeRefreshLayout.OnRefreshListener updateOffers = () -> {
        binding.offersShimmerLayout.setVisibility(View.VISIBLE);
        binding.offersTitle.setVisibility(View.GONE);
        binding.offersList.setVisibility(View.GONE);
        startSearchOffers();
        binding.refreshOffersList.setRefreshing(false);
    };


}