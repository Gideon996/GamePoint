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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.SearchedGamesAdapter;
import it.adriano.tumino.gamepoint.adapter.recyclerview.LastSearchedGamesAdapter;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.search.SearchGames;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;

public class SearchFragment extends Fragment implements AsyncResponse<List<BasicGameInformation>> {
    private DBManager dbManager;
    private View view;
    private EditText editText;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView anyResultTextView;

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
        //change title
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Search");
        //setup the view
        view = inflater.inflate(R.layout.fragment_search_game, container, false);
        ImageButton searchButton = view.findViewById(R.id.searchGameButton);
        editText = view.findViewById(R.id.searchGameEditText);
        anyResultTextView = view.findViewById(R.id.anyResultTextView);

        dbManager = new DBManager(getContext(), DBUtils.LAST_RESEARCH_TABLE_TITLE); //get db manager

        setUpRecyclerView(); //setup recyclerview layout

        editText.setOnEditorActionListener((v, actionId, event) -> { //setup change listener for the search
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                enterKey(editText.getText().toString());
            }
            return false;
        });

        searchButton.setOnClickListener(v -> { //search button
            String text = editText.getText().toString();
            enterKey(text);
        });

        return view;
    }

    private void setUpRecyclerView() {
        ArrayList<BasicGameInformation> list = dbManager.getAllElements();
        RecyclerView latestResearchGameRecyclerView = view.findViewById(R.id.latestResearchGameRecyclerView);
        LastSearchedGamesAdapter lastSearchedGamesAdapter = new LastSearchedGamesAdapter(list);
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

    private void enterKey(String text) {
        if (text.isEmpty()) {
            view.findViewById(R.id.latestResearchGamesLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.gameSearchResultsLayout).setVisibility(View.GONE);
            closeKeyboard();
            Toast.makeText(getContext(), "Enter the title of the game you want to search...", Toast.LENGTH_SHORT).show();
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

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void catchInformation(String name) {
        SearchGames searchGames = new SearchGames(name, getContext());
        searchGames.delegate = this;
        searchGames.execute();
    }

    @Override
    public void processFinish(List<BasicGameInformation> result) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

        if (result.size() == 0) {
            anyResultTextView.setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.gameSearchResultsLayout).setVisibility(View.VISIBLE);
            listOfResult.addAll(result);
            searchedGamesAdapter.notifyDataSetChanged();
        }
    }
}