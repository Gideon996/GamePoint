package it.adriano.tumino.gamepoint.ui.search;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.adriano.tumino.gamepoint.MainSharedViewModel;
import it.adriano.tumino.gamepoint.adapter.recyclerview.SearchedGamesAdapter;
import it.adriano.tumino.gamepoint.adapter.recyclerview.LastSearchedGamesAdapter;
import it.adriano.tumino.gamepoint.databinding.FragmentSearchGameBinding;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.search.SearchGames;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DBUtils;

public class SearchFragment extends Fragment implements AsyncResponse<List<BasicGameInformation>> {
    private DBManager dbManager;
    private FragmentSearchGameBinding binding;
    private MainSharedViewModel viewModel;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Search");

        binding = FragmentSearchGameBinding.inflate(inflater, container, false);

        dbManager = new DBManager(getContext(), DBUtils.LAST_RESEARCH_TABLE_TITLE);

        binding.searchGameEditText.addTextChangedListener(gameTitleWatcher);

        binding.searchGameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                enterKey(binding.searchGameEditText.getText().toString());
            }
            return false;
        });
        binding.searchGameButton.setOnClickListener(v -> {
            String text = binding.searchGameEditText.getText().toString();
            enterKey(text);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.getHasResearch()) {
            binding.searchedGamesShimmerLayout.setVisibility(View.GONE);
            binding.latestResearchGamesLayout.setVisibility(View.GONE);
            binding.gameSearchResultsLayout.setVisibility(View.VISIBLE);

            binding.searchGameEditText.setText(viewModel.getTitle());

            setUpResearchView(viewModel.getSearchedList());
        } else {
            binding.searchedGamesShimmerLayout.setVisibility(View.GONE);
            binding.latestResearchGamesLayout.setVisibility(View.VISIBLE);
            binding.gameSearchResultsLayout.setVisibility(View.GONE);
            setUpLastSearch();
        }
    }

    private void setUpLastSearch() {
        ArrayList<BasicGameInformation> list = dbManager.getAllElements();
        LastSearchedGamesAdapter lastSearchedGamesAdapter = new LastSearchedGamesAdapter(list);
        binding.latestResearchGameRecyclerView.setHasFixedSize(true);
        binding.latestResearchGameRecyclerView.setAdapter(lastSearchedGamesAdapter);
    }

    private void setUpResearchView(List<BasicGameInformation> list) {
        SearchedGamesAdapter searchedGamesAdapter = new SearchedGamesAdapter(list);
        binding.gameSearchResultsRecyclerView.setHasFixedSize(true);
        binding.gameSearchResultsRecyclerView.setAdapter(searchedGamesAdapter);
    }

    private void enterKey(String text) {
        if (text.isEmpty()) {
            binding.latestResearchGamesLayout.setVisibility(View.VISIBLE);
            binding.gameSearchResultsLayout.setVisibility(View.GONE);
            closeKeyboard();
            Toast.makeText(getContext(), "Enter the title of the game you want to search...", Toast.LENGTH_SHORT).show();
        } else {
            binding.searchedGamesShimmerLayout.setVisibility(View.VISIBLE);
            binding.searchedGamesShimmerLayout.startShimmer();
            closeKeyboard();
            binding.latestResearchGamesLayout.setVisibility(View.GONE);
            binding.gameSearchResultsLayout.setVisibility(View.GONE);

            catchInformation(text);
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.searchGameEditText.getWindowToken(), 0);
    }

    private void catchInformation(String name) {
        binding.searchedGamesShimmerLayout.setVisibility(View.VISIBLE);
        binding.searchedGamesShimmerLayout.startShimmer();
        viewModel.setTitle(name);
        SearchGames searchGames = new SearchGames(name, getContext());
        searchGames.delegate = this;
        searchGames.execute();
    }

    @Override
    public void processFinish(List<BasicGameInformation> result) {
        binding.searchedGamesShimmerLayout.stopShimmer();
        binding.searchedGamesShimmerLayout.setVisibility(View.GONE);

        if (result.size() == 0) {
            binding.anyResultTextView.setVisibility(View.VISIBLE);
        } else {
            viewModel.setHasResearch(true);
            viewModel.setSearchedList(result);
            SearchedGamesAdapter searchedGamesAdapter = new SearchedGamesAdapter(result);
            binding.gameSearchResultsRecyclerView.setHasFixedSize(true);
            binding.gameSearchResultsRecyclerView.setAdapter(searchedGamesAdapter);

            binding.gameSearchResultsLayout.setVisibility(View.VISIBLE);
        }
    }

    TextWatcher gameTitleWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().isEmpty()) {
                binding.latestResearchGamesLayout.setVisibility(View.VISIBLE);
                setUpLastSearch();
                binding.gameSearchResultsLayout.setVisibility(View.GONE);
                viewModel.setHasResearch(false);
            }
        }
    };
}