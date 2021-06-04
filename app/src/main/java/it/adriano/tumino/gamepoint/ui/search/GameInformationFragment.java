package it.adriano.tumino.gamepoint.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.jsoup.internal.StringUtil;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.ScreenshotAdapter;
import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.backgroundprocesses.CatchGame;
import it.adriano.tumino.gamepoint.data.GameShow;

public class GameInformationFragment extends Fragment implements AsyncResponse<GameShow>, TabLayout.OnTabSelectedListener {
    private GameShow result;

    public GameInformationFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getArguments().getString("test"); //valori passati da bundle
        View view = inflater.inflate(R.layout.fragment_game_information, container, false);
        CatchGame catchGame = new CatchGame();
        catchGame.delegate = this;
        catchGame.execute(0);
        return view;
    }

    @Override
    public void processFinish(GameShow result) {
        this.result = result;
        fillTextView(result);
        fillScreenShotView(result);

        Picasso.get().load(result.getImageURL()).into((ImageView) getView().findViewById(R.id.imageHeader));

        TabLayout tabLayout = getView().findViewById(R.id.tabLayout);
        Fragment fragment = OtherInformationFragment.newInstance("Descrizione", "Description", result);
        setFragmentLayout(fragment);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void setFragmentLayout(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void fillTextView(GameShow gameShow) {
        TextView textView = getView().findViewById(R.id.gameTitleTextView);
        textView.setText(gameShow.getTitle());

        textView = getView().findViewById(R.id.releaseDataTextView);
        textView.setText(gameShow.getDate());

        textView = getView().findViewById(R.id.metacriticVote);
        textView.setText(String.valueOf(gameShow.getScoreMetacritic()));

        textView = getView().findViewById(R.id.listTagTextView);
        textView.setText(StringUtil.join(gameShow.getCategories(), ", "));

        textView = getView().findViewById(R.id.priceTextView);
        textView.setText(gameShow.getPrice());

        textView = getView().findViewById(R.id.languagesTextView);
        textView.setText(gameShow.getLanguages());
    }

    private void fillScreenShotView(GameShow gameShow) {
        RecyclerView recyclerView = getView().findViewById(R.id.screenshotShower);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.scrollToPosition(Integer.MAX_VALUE / 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        ScreenshotAdapter screenshotAdapter = new ScreenshotAdapter(gameShow.getScreenshots());
        recyclerView.setAdapter(screenshotAdapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Fragment fragment = null;
        switch (tab.getPosition()) {
            case 0:
                fragment = OtherInformationFragment.newInstance("Descrizione", "Description", result);
                break;
            case 1:
                fragment = OtherInformationFragment.newInstance("Specifiche", "Computer", result);
                break;
            case 2:
                fragment = OtherInformationFragment.newInstance("Commenti", "comment", result);
                break;
        }
        setFragmentLayout(fragment);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}