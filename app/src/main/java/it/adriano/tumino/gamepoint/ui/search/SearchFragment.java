package it.adriano.tumino.gamepoint.ui.search;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Objects;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.SearchedGamesAdapter;
import it.adriano.tumino.gamepoint.adapter.recyclerview.LastSearchedGamesAdapter;
import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.backgroundprocesses.searchgame.SearchOnEShop;
import it.adriano.tumino.gamepoint.backgroundprocesses.searchgame.SearchOnMicrosoft;
import it.adriano.tumino.gamepoint.backgroundprocesses.searchgame.SearchOnPSN;
import it.adriano.tumino.gamepoint.backgroundprocesses.searchgame.SearchOnSteam;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;

public class SearchFragment extends Fragment implements AsyncResponse<ArrayList<BasicGameInformation>> {
    private DBManager dbManager;
    private View view;
    private EditText editText;
    private ShimmerFrameLayout shimmerFrameLayout;

    private SearchedGamesAdapter searchedGamesAdapter;
    private final ArrayList<BasicGameInformation> listOfResult = new ArrayList<>();

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Search");
        view = inflater.inflate(R.layout.fragment_search_game, container, false);
        ImageButton searchButton = view.findViewById(R.id.searchGameButton);
        editText = view.findViewById(R.id.searchGameEditText);

        dbManager = new DBManager(getContext(), DBUtils.LAST_RESEARCH_TABLE_TITLE); //ultime ricerche

        setUpRecyclerView();

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                enterKey(editText.getText().toString());
            }
            return false;
        });
        searchButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            enterKey(text);
        });

        return view;
    }

    private void setUpRecyclerView() {
        ArrayList<BasicGameInformation> list = dbManager.getAllElements();
        RecyclerView latestResearchGameRecyclerView = view.findViewById(R.id.latestResearchGameRecyclerView);
        LastSearchedGamesAdapter lastSearchedGamesAdapter = new LastSearchedGamesAdapter(list, view);

        latestResearchGameRecyclerView.setHasFixedSize(true);
        latestResearchGameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        latestResearchGameRecyclerView.setAdapter(lastSearchedGamesAdapter);

        RecyclerView gameSearchResultsRecyclerView = view.findViewById(R.id.gameSearchResultsRecyclerView);
        searchedGamesAdapter = new SearchedGamesAdapter(listOfResult, view);
        gameSearchResultsRecyclerView.setHasFixedSize(true);
        gameSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameSearchResultsRecyclerView.setAdapter(searchedGamesAdapter);

        shimmerFrameLayout = view.findViewById(R.id.searchedGamesShimmerLayout);

    }

    private void catchInformation(String name) {
        //Cercare su steam
        SearchOnSteam searchOnSteam = new SearchOnSteam();
        searchOnSteam.delegate = this;
        searchOnSteam.execute(name);

        //Cercare su Nintendo
        SearchOnEShop searchOnEShop = new SearchOnEShop();
        searchOnEShop.delegate = this;
        searchOnEShop.execute(name);

        //Cercare su PSN
        SearchOnPSN searchOnPSN = new SearchOnPSN();
        searchOnPSN.delegate = this;
        searchOnPSN.execute(name);

        //cercare su Microsoft
        SearchOnMicrosoft searchOnMicrosoft = new SearchOnMicrosoft();
        searchOnMicrosoft.delegate = this;
        searchOnMicrosoft.execute(name);
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void enterKey(String text) {
        if (text.isEmpty()) {
            view.findViewById(R.id.latestResearchGamesLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.gameSearchResultsLayout).setVisibility(View.GONE);
            closeKeyboard();
            Toast.makeText(getContext(), "Ricerca Vuota, inserisci il nome del gioco", Toast.LENGTH_SHORT).show();
        } else {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            listOfResult.clear();

            closeKeyboard();

            view.findViewById(R.id.latestResearchGamesLayout).setVisibility(View.GONE);
            view.findViewById(R.id.gameSearchResultsLayout).setVisibility(View.GONE);

            catchInformation(text);
        }
    }

    @Override
    public void processFinish(ArrayList<BasicGameInformation> result) {
        if (shimmerFrameLayout.isShimmerStarted()) {
            shimmerFrameLayout.stopShimmer();
            view.findViewById(R.id.gameSearchResultsLayout).setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.GONE);
        }
        listOfResult.addAll(result);
        searchedGamesAdapter.notifyDataSetChanged();
    }
}