package it.adriano.tumino.gamepoint.ui.showgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.handler.MicrosoftHandler;
import it.adriano.tumino.gamepoint.processes.handler.NintendoHandler;
import it.adriano.tumino.gamepoint.processes.handler.PlayStationHandler;
import it.adriano.tumino.gamepoint.processes.handler.SteamHandler;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.databinding.FragmentGameResultBinding;
import it.adriano.tumino.gamepoint.ui.showgame.comment.GameCommentsFragment;
import it.adriano.tumino.gamepoint.ui.showgame.gallery.GalleryFragment;
import it.adriano.tumino.gamepoint.utils.Utils;

public class GameResultFragment extends Fragment implements AsyncResponse<StoreGame>, TabLayout.OnTabSelectedListener {
    public static final String TAG = "GameResultFragment";

    private static final String USER = "Users";
    private static final String FAVORITES = "Favorites";

    private BasicGameInformation basicGameInformation;
    private FragmentGameResultBinding binding;
    private final GameResultViewModel viewModel;

    private final FirebaseAuth auth;
    private final FirebaseFirestore firebaseFirestore;
    private boolean isOnDatabase;
    private String query;

    private final Fragment[] fragments = new Fragment[4];

    private final Bundle information = new Bundle();

    public GameResultFragment() {
        viewModel = new GameResultViewModel();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        viewModel.getHasResult().setValue(false);

        fragments[0] = new DescriptionFragment();
        fragments[1] = new GalleryFragment();
        fragments[2] = new GameSpecificationsFragment();
        fragments[3] = new GameCommentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("game")) {
            basicGameInformation = getArguments().getParcelable("game");
        }

        boolean isEmpty = basicGameInformation == null || basicGameInformation.isEmpty();
        viewModel.getIsEmpty().setValue(isEmpty);

        if (!isEmpty) {
            viewModel.getHasResult().setValue(true);
            information.putString("store", basicGameInformation.getStore());
            switch (basicGameInformation.getStore()) {
                case "STEAM":
                    SteamHandler.catchGame(basicGameInformation.getAppID(), this);
                    break;
                case "MCS":
                    MicrosoftHandler.catchGame(basicGameInformation.getUrl(), this);
                    break;
                case "PSN":
                    PlayStationHandler.catchGame(basicGameInformation.getUrl(), this);
                    break;
                case "ESHOP":
                    NintendoHandler.catchGame(basicGameInformation.getUrl(), basicGameInformation.getPrice(), this);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameResultBinding.inflate(inflater, container, false);
        if (viewModel.getIsEmpty().getValue() != null && viewModel.getIsEmpty().getValue()) {
            binding.gameResultShimmerLayout.setVisibility(View.GONE);
            binding.gameResultLayout.setVisibility(View.GONE);
            binding.noGameResultTextView.setVisibility(View.VISIBLE);
        } else {
            query = basicGameInformation.getTitle().replaceAll("\\s+", "") + basicGameInformation.getStore();
            binding.gameResultShimmerLayout.startShimmer();
            binding.tabLayout.addOnTabSelectedListener(this);

            binding.favoriteButton.setEnabled(false);
            if (auth.getUid() != null) {
                checkIfOnDatabase(auth.getUid());
            } else {
                isOnDatabase = false;
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasResultToShow()) {
            setFragmentLayout(fragments[(viewModel.getCurrentFragment().getValue() != null) ? viewModel.getCurrentFragment().getValue() : 0]);
        }
    }

    private boolean hasResultToShow() {
        return viewModel.getIsEmpty().getValue() != null &&
                viewModel.getIsEmpty().getValue() &&
                viewModel.getHasResult().getValue() != null &&
                viewModel.getHasResult().getValue();
    }

    @Override
    public void processFinish(StoreGame result) {
        binding.gameResultShimmerLayout.stopShimmer();
        binding.gameResultShimmerLayout.setVisibility(View.GONE);

        if (result != null) {
            viewModel.getHasResult().setValue(true);
            viewModel.getResult().setValue(result);

            binding.gameResultLayout.setVisibility(View.VISIBLE);
            binding.setGame(result);
            information.putParcelable("game", result);
            for (Fragment fragment : fragments) fragment.setArguments(information);

            viewModel.getCurrentFragment().setValue(0);
            setFragmentLayout(fragments[0]);

            if (result.getPrice().toLowerCase().equals("free") || result.getPrice().toLowerCase().equals("unavailable")) {
                binding.priceGameTextView.setText(String.format(getString(R.string.price), result.getPrice(), ""));
            } else {
                binding.priceGameTextView.setText(String.format(getString(R.string.price), result.getPrice(), getString(R.string.price_symbol)));
            }

            binding.shareButton.setOnClickListener(v -> Utils.shareContent(getContext(), result.getImageHeaderURL(), getString(R.string.share_game_text) + result.getTitle()));
            binding.favoriteButton.setOnClickListener(v -> favoriteRoutines());
        } else {
            viewModel.getHasResult().setValue(false);
            binding.noGameResultTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewModel.getCurrentFragment().setValue(tab.getPosition());
        setFragmentLayout(fragments[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setFragmentLayout(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void checkIfOnDatabase(@NonNull String userID) {
        DocumentReference exist = firebaseFirestore.collection(USER)
                .document(userID).collection(FAVORITES).document(query);
        exist.get().addOnCompleteListener(task -> {
            binding.favoriteButton.setEnabled(true);
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                isOnDatabase = false;
                if (document != null) isOnDatabase = document.exists();
                changeColor();
            } else {
                isOnDatabase = false;
            }
        });

    }

    private void favoriteRoutines() {
        if (auth.getUid() == null) return;
        DocumentReference reference = firebaseFirestore.collection(USER)
                .document(auth.getUid()).collection(FAVORITES).document(query);
        if (isOnDatabase) {
            reference.delete()
                    .addOnSuccessListener(aVoid -> {
                        isOnDatabase = false;
                        Toast.makeText(requireContext(), R.string.remove_favorite, Toast.LENGTH_SHORT).show();
                        changeColor();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(requireContext(), R.string.error_favorite_delete, Toast.LENGTH_SHORT).show();
                    });
        } else {
            reference.set(basicGameInformation)
                    .addOnSuccessListener(aVoid -> {
                        isOnDatabase = true;
                        changeColor();
                        Toast.makeText(requireContext(), R.string.favorite_add, Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        Log.e(TAG, exception.getMessage());
                        isOnDatabase = false;
                        Toast.makeText(requireContext(), R.string.error_add_favorite, Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void changeColor() {
        binding.favoriteButton.setColorFilter(getResources().getColor((isOnDatabase) ? R.color.favorite_color : R.color.black, getResources().newTheme()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*Since I create so many objects I prefer to call the garbage
        collector to delete the ones that are no longer useful*/
        System.gc();
    }
}