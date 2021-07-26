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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.FavoriteGamesAdapter;
import it.adriano.tumino.gamepoint.adapter.recyclerview.OffersAdapter;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.databinding.FragmentHomeBinding;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.search.SearchOffers;
import it.adriano.tumino.gamepoint.SharedViewModel;

public class HomeFragment extends Fragment implements AsyncResponse<List<GameOffers>> {
    public static final String TAG = "HomeFragment";

    private SharedViewModel sharedViewModel;
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (mAuth.getUid() != null) onSnapshotFavoritesGame(mAuth.getUid());

        binding.refreshOffersList.setOnRefreshListener(() -> {
            startSearchOffers();
            binding.refreshOffersList.setRefreshing(false);
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getHasOffers().getValue() != null && sharedViewModel.getHasOffers().getValue()) {
            Log.i(TAG, getString(R.string.has_state_of_home_fragment));
            setOffersList(sharedViewModel.getOffersList().getValue());
        } else {
            Log.i(TAG, getString(R.string.no_state_of_home_fragment));
            startSearchOffers();
        }
    }

    public void startSearchOffers(){
        SearchOffers searchOffers = new SearchOffers(getContext());
        searchOffers.delegate = this;
        searchOffers.execute();
    }

    @Override
    public void processFinish(List<GameOffers> result) {
        if (result != null && result.size() > 0) {
            setOffersList(result);
            Log.d(TAG, getString(R.string.save_state));
            sharedViewModel.setHasOffers(true);
            sharedViewModel.setOffersList(result);
        } else {
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
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<BasicGameInformation> comments = new ArrayList<>();
                        if (value != null) {
                            for (QueryDocumentSnapshot doc : value) {
                                BasicGameInformation comment = doc.toObject(BasicGameInformation.class);
                                comments.add(comment);
                            }
                        }

                        visualizeAllFavoriteGames(comments);
                    });
        }
    }

    private void visualizeAllFavoriteGames(List<BasicGameInformation> list) {
        if (list.size() != 0) {
            FavoriteGamesAdapter favoriteGamesAdapter = new FavoriteGamesAdapter(list);
            binding.favoriteGamesList.setAdapter(favoriteGamesAdapter);
        } else {
            binding.favoriteTitle.setVisibility(View.GONE);
            binding.favoriteGamesList.setVisibility(View.GONE);
        }
    }


}