package it.adriano.tumino.gamepoint.ui.showgame;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.catchgame.CatchNintendoGame;
import it.adriano.tumino.gamepoint.processes.catchgame.CatchMicrosoftGame;
import it.adriano.tumino.gamepoint.processes.catchgame.CatchPlayStationGame;
import it.adriano.tumino.gamepoint.processes.catchgame.CatchSteamGame;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;
import it.adriano.tumino.gamepoint.databinding.FragmentGameResultBinding;
import it.adriano.tumino.gamepoint.processes.TaskRunner;
import it.adriano.tumino.gamepoint.ui.showgame.comment.GameCommentsFragment;
import it.adriano.tumino.gamepoint.ui.showgame.gallery.GalleryFragment;
import it.adriano.tumino.gamepoint.utils.Utils;

public class GameResultFragment extends Fragment implements AsyncResponse<StoreGame>, TabLayout.OnTabSelectedListener {
    public static final String TAG = "GameResultFragment";

    private BasicGameInformation basicGameInformation;
    private FragmentGameResultBinding binding;
    private final GameResultViewModel viewModel;

    private DBManager favoriteDBManager;
    private boolean isOnDatabase;
    private final Fragment[] fragments = new Fragment[4];

    private TaskRunner<Void, StoreGame> game;
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

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(basicGameInformation.getTitle().toUpperCase());
        switch (basicGameInformation.getStore()) {
            case "STEAM":
                game = new CatchSteamGame(basicGameInformation.getAppID());
                ((CatchSteamGame) game).delegate = this;
                break;
            case "MCS":
                game = new CatchMicrosoftGame(basicGameInformation.getUrl());
                ((CatchMicrosoftGame) game).delegate = this;
                break;
            case "PSN":
                game = new CatchPlayStationGame(basicGameInformation.getUrl());
                ((CatchPlayStationGame) game).delegate = this;
                break;
            case "ESHOP":
                game = new CatchNintendoGame(basicGameInformation.getUrl(), basicGameInformation.getPrice());
                ((CatchNintendoGame) game).delegate = this;
                break;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameResultBinding.inflate(inflater, container, false);
        game.execute();
        favoriteDBManager = new DBManager(binding.getRoot().getContext(), DBUtils.FAVORITE_TABLE_TITLE);
        binding.gameResultShimmerLayout.startShimmer();
        binding.tabLayout.addOnTabSelectedListener(this);
        isOnDatabase = favoriteDBManager.checkIfElementsIsOnDataBase(basicGameInformation.getTitle(), basicGameInformation.getStore());

        binding.favoriteButton.setColorFilter((isOnDatabase) ? Color.rgb(255,69,0) : Color.BLACK);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.getHasResult().getValue())
            setFragmentLayout(fragments[viewModel.getCurrentFragment().getValue()]);
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

            binding.shareButton.setOnClickListener(v -> Utils.shareContent(getContext(), result.getImageHeaderURL(), R.string.share_game_text + result.getTitle()));
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

    private void favoriteRoutines() {
        if (isOnDatabase) {
            favoriteDBManager.deleteWithNameAndStore(basicGameInformation.getTitle(), basicGameInformation.getStore());
            Toast.makeText(this.getContext(), R.string.added_on_favorite, Toast.LENGTH_SHORT).show();
            binding.favoriteButton.setColorFilter(R.color.black);
            isOnDatabase = false;
        } else {
            favoriteDBManager.save(basicGameInformation);
            Toast.makeText(this.getContext(), R.string.removed_from_favorite, Toast.LENGTH_SHORT).show();
            binding.favoriteButton.setColorFilter(Color.rgb(255,69,0));
            isOnDatabase = true;
        }
    }

    private void setFragmentLayout(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}