package it.adriano.tumino.gamepoint.ui.search;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.RicercaGiochiAdapter;
import it.adriano.tumino.gamepoint.adapter.UltimeRicercheAdapter;
import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.backgroundprocesses.SearchOnEShop;
import it.adriano.tumino.gamepoint.backgroundprocesses.SearchOnMicrosoft;
import it.adriano.tumino.gamepoint.backgroundprocesses.SearchOnPSN;
import it.adriano.tumino.gamepoint.backgroundprocesses.SearchOnSteam;
import it.adriano.tumino.gamepoint.data.FavoriteGames;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;

public class SearchFragment extends Fragment implements AsyncResponse<ArrayList<GameSearchResult>> {
    private DBManager dbManager;
    private View view;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_game, container, false);
        ImageButton searchButton = view.findViewById(R.id.searchButton);
        EditText editText = view.findViewById(R.id.searchEditText);

        //ultime ricerche
        dbManager = new DBManager(getContext(), DataBaseValues.ULITME_RICERCHE.getName());
        setUpRecyclerView(view);

        searchButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(getContext(), "Ricerca Vuota, inserisci il nome del gioco", Toast.LENGTH_SHORT).show();
            } else {
                //chiudo tastiera dopo che ho fatto la ricerca
                listOfResult.clear();
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                //AGGIUNGERE FILTRO CHE RICHIEDE DOVE CERCARE IL GIOCO

                //Cercare Sui vari siti i valori
                catchInformation(text);
                //mostrarli
                //Togliere ultime ricerche
                //Shimmer sul nuovo layout
                //Mostrare contenuto trovato
                //Inserire listener al click
                //Aggiungere il gioco ai preferiti
                //Aprire informazioni [
                //Bundle bundle = new Bundle();
                //bundle.putString("test", "sto provando a passare un valore");
                //Navigation.findNavController(v).navigate(R.id.search_action, bundle);
                //]
            }
        });

        return view;
    }

    private RicercaGiochiAdapter ricercaGiochiAdapter;
    private ArrayList<GameSearchResult> listOfResult = new ArrayList<>();

    private void setUpRecyclerView(View view) {
        ArrayList<FavoriteGames> list = dbManager.getAll();
        RecyclerView recyclerView = view.findViewById(R.id.ultimeRicerche);
        UltimeRicercheAdapter ultimeRicercheAdapter = new UltimeRicercheAdapter(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ultimeRicercheAdapter);

        RecyclerView risultatiLayout = view.findViewById(R.id.risultatiRicerca);
        risultatiLayout.setVisibility(View.VISIBLE);
        ricercaGiochiAdapter = new RicercaGiochiAdapter(listOfResult);
        risultatiLayout.setHasFixedSize(true);
        risultatiLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        risultatiLayout.setAdapter(ricercaGiochiAdapter);
    }

    private String name;
    private void catchInformation(String name) {
        this.name = name;
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


    @Override
    public void processFinish(ArrayList<GameSearchResult> result) {
        if(result != null){
            listOfResult.addAll(result);
            ricercaGiochiAdapter.notifyDataSetChanged();
        }
    }
}