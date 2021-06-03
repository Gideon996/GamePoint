package it.adriano.tumino.gamepoint.ui.search;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.FavoriteAdapter;
import it.adriano.tumino.gamepoint.adapter.NewsAdapter;
import it.adriano.tumino.gamepoint.adapter.UltimeRicercheAdapter;
import it.adriano.tumino.gamepoint.data.FavoriteGames;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;
import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;
import it.adriano.tumino.gamepoint.ui.news.NewsViewModel;

public class SearchFragment extends Fragment {
    private DBManager dbManager;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_game, container, false);
        ImageButton searchButton = view.findViewById(R.id.searchButton);
        EditText editText = view.findViewById(R.id.searchEditText);

        //ultime ricerche
        dbManager = new DBManager(getContext(), DataBaseValues.ULITME_RICERCHE.getName());
        setUpRecyclerView(view);

        searchButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if(text.isEmpty()){
                Toast.makeText(getContext(), "Ricerca Vuota, inserisci il nome del gioco", Toast.LENGTH_SHORT).show();
            }else{
                //chiudo tastiera dopo che ho fatto la ricerca
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                //Cercare Sui vari siti i valori
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

    

    private void setUpRecyclerView(View view){
        ArrayList<FavoriteGames> list = dbManager.getAll();
        RecyclerView recyclerView = view.findViewById(R.id.ultimeRicerche);
        UltimeRicercheAdapter ultimeRicercheAdapter = new UltimeRicercheAdapter(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ultimeRicercheAdapter);
    }
}