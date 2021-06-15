package it.adriano.tumino.gamepoint.ui.showgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.backgroundprocesses.CatchGame;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.data.GameShow;
import it.adriano.tumino.gamepoint.utils.TaskRunner;
import it.adriano.tumino.gamepoint.utils.Utils;

public class GameResultFragment extends Fragment implements AsyncResponse<GameShow>, TabLayout.OnTabSelectedListener {
    private GameSearchResult gameSearchResult;
    private static final String BASE_TEXT = "Garda che bel gioco ho trovato: ";

    public GameResultFragment() {
    }

    private TaskRunner<Integer, String> game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("game"))
            gameSearchResult = getArguments().getParcelable("game");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(gameSearchResult.getTitle().toUpperCase()); //Setto il titolo del fragment
        switch (gameSearchResult.getStore()) {
            case "PSN":
                game = new CatchGame();
                ((CatchGame) game).delegate = this;
                break;
            case "MCS":
                game = new CatchGame();
                ((CatchGame) game).delegate = this;
                break;
            case "STEAM":
                game = new CatchGame();
                ((CatchGame) game).delegate = this;
                break;
            case "ESHOP":
                game = new CatchGame();
                ((CatchGame) game).delegate = this;
                break;
            default:
                //nessuno di questo
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_result, container, false);

        //if (gameSearchResult == null) Navigation.findNavController(view).navigate(R.id.no_game_action); //doesn't works, idk why

        TabLayout tabLayout = view.findViewById(R.id.tabLayout); //setto le impostazioni per il tabLayout
        Fragment descrizione = new DescriptionFragment(); //imposto la vista di default
        setFragmentLayout(descrizione); //imposto il layout da visualizzare
        tabLayout.addOnTabSelectedListener(this); //imposto il listener

        //game.execute(0);
        return view;
    }

    private void setFragmentLayout(Fragment fragment) { //imposto che cosa mostrare nel tab layout
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); //avvio la tansizione per cosa inserire
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton sharedButton = view.findViewById(R.id.shareButton); //setto il bottone condividi
        sharedButton.setOnClickListener(v -> { //listener per la condivisione
            shareButton("", "TESTO");
        });
    }

    private void shareButton(String url, String text) {
        Picasso.get().load(url).into(new Target() { //carico l'immagine in cache
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND); //Inizializzo Intent da fare
                i.setType("image/*"); //setto il tipo del contenuto
                i.putExtra(Intent.EXTRA_STREAM, Utils.getLocalBitmapUri(getContext(), bitmap)); //Inserisco l'immagine
                i.putExtra(Intent.EXTRA_TEXT, BASE_TEXT + text); //Inserisco il testo per l'immagine
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //Uso i permessi
                startActivity(Intent.createChooser(i, "Share Image")); //Inizio l'activity
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { //Nel caso non riesco a prelevare l'immagine
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { //per prelevare l'immagine
            }
        });
    }


    @Override
    public void processFinish(GameShow result) {
        //aggiorno l'interfaccia grafica
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) { //Listener per capire che Layout mostrare
        Fragment fragment = null;
        switch (tab.getPosition()) {
            case 0: //Tab Descrizione
                fragment = new DescriptionFragment();
                break;
            case 1: //Tab Galleria
                fragment = new GalleryFragment();
                break;
            case 2: //Tab Specifiche
                fragment = new GameSpecificationsFragment();
                break;
            case 3: //Tab Commenti
                fragment = new GameCommentsFragment();
                break;
        }
        setFragmentLayout(fragment); //imposto il frammento
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}