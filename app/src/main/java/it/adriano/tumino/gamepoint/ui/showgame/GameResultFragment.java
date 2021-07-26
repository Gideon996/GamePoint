package it.adriano.tumino.gamepoint.ui.showgame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.Objects;

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

    private BasicGameInformation basicGameInformation;
    private FragmentGameResultBinding binding;
    private final GameResultViewModel viewModel;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private boolean isOnDatabase;
    private String query;

    private final Fragment[] fragments = new Fragment[4];

    private final Bundle information = new Bundle();


    public GameResultFragment() {
        viewModel = new GameResultViewModel();
        viewModel.getHasResult().setValue(false);
        fragments[0] = new DescriptionFragment();
        fragments[1] = new GalleryFragment();
        fragments[2] = new GameSpecificationsFragment();
        fragments[3] = new GameCommentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("game"))
            basicGameInformation = getArguments().getParcelable("game");

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

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        binding = FragmentGameResultBinding.inflate(inflater, container, false);
        query = basicGameInformation.getTitle().replaceAll("\\s+", "") + basicGameInformation.getStore();
        binding.gameResultShimmerLayout.startShimmer();
        binding.tabLayout.addOnTabSelectedListener(this);

        binding.favoriteButton.setEnabled(false);
        checkIfOnDatabase();


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(basicGameInformation.getTitle().toUpperCase());
        if (viewModel.getResult() != null && viewModel.getHasResult().getValue() != null) {
            if (viewModel.getHasResult().getValue())
                setFragmentLayout(fragments[(viewModel.getCurrentFragment().getValue() != null) ? viewModel.getCurrentFragment().getValue() : 0]);
        }

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

            binding.shareButton.setOnClickListener(v -> Utils.shareContent(getContext(), result.getImageHeaderURL(), getString(R.string.share_game_text) + result.getTitle()));
            binding.favoriteButton.setOnClickListener(v -> favoriteRoutines());
        } else {
            binding.noGameResulTextView.setVisibility(View.VISIBLE);
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

    private void checkIfOnDatabase() {
        if (auth.getUid() != null) {
            DocumentReference exist = firestore.collection("Users").document(auth.getUid()).collection("Favorites").document(query);
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
        } else {
            Log.e(TAG, getString(R.string.not_get_user_id));
            Toast.makeText(requireContext(), R.string.not_get_user_id, Toast.LENGTH_SHORT).show();
            isOnDatabase = false;
        }

    }

    private void favoriteRoutines() {
        if (auth.getUid() == null) return;
        DocumentReference reference = firestore.collection("Users").document(auth.getUid()).collection("Favorites").document(query);
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

    private void setFragmentLayout(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}