package it.adriano.tumino.gamepoint.ui.showgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.FavoriteGamesAdapter;
import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.backgroundprocesses.catchgame.CatchNintendoGame;
import it.adriano.tumino.gamepoint.backgroundprocesses.catchgame.CatchMicrosoftGame;
import it.adriano.tumino.gamepoint.backgroundprocesses.catchgame.CatchPlayStationGame;
import it.adriano.tumino.gamepoint.backgroundprocesses.catchgame.CatchSteamGame;
import it.adriano.tumino.gamepoint.data.storegame.Game;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;
import it.adriano.tumino.gamepoint.databinding.FragmentGameResultBinding;
import it.adriano.tumino.gamepoint.utils.TaskRunner;
import it.adriano.tumino.gamepoint.utils.Utils;

public class GameResultFragment extends Fragment implements AsyncResponse<Game>, TabLayout.OnTabSelectedListener {
    public static final String TAG = "GameResultFragment";

    private static final String BASE_TEXT = "Guarda che bel gioco ho trovato: ";

    private ImageButton sharedButton;
    private ImageButton favoriteButton;
    private final Fragment[] fragments = new Fragment[4];
    private String logoName;
    private GameSearchResult gameSearchResult;
    private DBManager favoriteDBManager;
    private boolean presenteNelDB;

    private FragmentGameResultBinding binding;

    private TaskRunner<Void, Game> game;
    private final Bundle information = new Bundle();

    private FavoriteGamesAdapter adapter = null;

    private TabLayout tabLayout;


    public GameResultFragment() {
        Log.i(TAG, "Inizializzazione dei fragments");
        fragments[0] = new DescriptionFragment();
        fragments[1] = new GalleryFragment();
        fragments[2] = new GameSpecificationsFragment();
        fragments[3] = new GameCommentsFragment();
    }

    public GameResultFragment(FavoriteGamesAdapter adapter) {
        Log.i(TAG, "Inizializzazione dei fragments e configurazione dell'adapter");
        fragments[0] = new DescriptionFragment();
        fragments[1] = new GalleryFragment();
        fragments[2] = new GameSpecificationsFragment();
        fragments[3] = new GameCommentsFragment();

        this.adapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("game"))
            gameSearchResult = getArguments().getParcelable("game");

        Log.i(TAG, "Prelevate le informazioni per ottenere le informazioni");
        information.putString("store", gameSearchResult.getStore());

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(gameSearchResult.getTitle().toUpperCase()); //Setto il titolo del fragment
        Log.i(TAG, "Inizializzo il cathcer di" + gameSearchResult.getStore());
        switch (gameSearchResult.getStore()) {
            case "STEAM":
                game = new CatchSteamGame(gameSearchResult.getAppID());
                logoName = "logo_steam.png";
                ((CatchSteamGame) game).delegate = this;
                break;
            case "MCS":
                game = new CatchMicrosoftGame(gameSearchResult.getUrl());
                logoName = "logo_xbox.png";
                ((CatchMicrosoftGame) game).delegate = this;
                break;
            case "PSN":
                game = new CatchPlayStationGame(gameSearchResult.getUrl());
                logoName = "logo_psn.png";
                ((CatchPlayStationGame) game).delegate = this;
                break;
            case "ESHOP":
                game = new CatchNintendoGame(gameSearchResult.getUrl(), gameSearchResult.getPrice());
                logoName = "logo_eshop2.png";
                ((CatchNintendoGame) game).delegate = this;
                break;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameResultBinding.inflate(inflater, container, false);
        //if (gameSearchResult == null) Navigation.findNavController(view).navigate(R.id.no_game_action); //doesn't works, idk why
        Log.i(TAG, "Inizio il processo di background");
        game.execute();

        favoriteDBManager = new DBManager(binding.getRoot().getContext(), DataBaseValues.FAVORITE_TABLE.getName());

        tabLayout = binding.tabLayout; //setto le impostazioni per il tabLayout
        tabLayout.addOnTabSelectedListener(this); //imposto il listener

        sharedButton = binding.shareButton;
        favoriteButton = binding.favoriteButton;
        presenteNelDB = favoriteDBManager.checkIfElementsIsOnDataBase(gameSearchResult.getTitle(), gameSearchResult.getStore());
        if (presenteNelDB) {
            favoriteButton.setColorFilter(Color.rgb(255, 69, 0));
        }

        return binding.getRoot();
    }

    private void setFragmentLayout(Fragment fragment) { //imposto che cosa mostrare nel tab layout
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //avvio la tansizione per cosa inserire
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }


    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "Inizio a riempire l'header layout");
        ImageView logo = view.findViewById(R.id.storeIconView);
        Drawable d;
        try {
            d = Drawable.createFromStream(requireContext().getAssets().open(logoName), null);
        } catch (IOException exception) {
            d = new ColorDrawable(Color.CYAN);
        }
        logo.setImageDrawable(d);
    }

    @Override
    public void processFinish(Game result) {
        if (result != null) {
            binding.setGame(result);
            information.putParcelable("game", result);
            for (Fragment fragment : fragments) fragment.setArguments(information);
            setFragmentLayout(fragments[0]); //imposto il layout da visualizzare

            sharedButton.setOnClickListener(v -> shareButton(result.getImageHeaderUrl(), result.getTitle()));

            favoriteButton.setOnClickListener(v -> favoriteRoutines());
        }
    }

    private void favoriteRoutines() {
        if (presenteNelDB) {
            favoriteDBManager.deleteFromNameAndStore(gameSearchResult.getTitle(), gameSearchResult.getStore());
            if (adapter != null) adapter.removeFavoriteGame(gameSearchResult);
            Toast.makeText(this.getContext(), "Gioco rimosso dai preferiti", Toast.LENGTH_SHORT).show();
            favoriteButton.setColorFilter(Color.BLACK);
            presenteNelDB = false;
        } else {
            favoriteDBManager.save(gameSearchResult.getTitle(), gameSearchResult.getImageURL(), gameSearchResult.getStore(), gameSearchResult.getUrl(), gameSearchResult.getAppID());
            if (adapter != null) adapter.addFavoriteGame(gameSearchResult);
            Toast.makeText(this.getContext(), "Gioco aggiunto ai preferiti", Toast.LENGTH_SHORT).show();
            favoriteButton.setColorFilter(Color.rgb(255, 69, 0));
            presenteNelDB = true;
        }
    }

    private void shareButton(String imageUrl, String text) {
        Picasso.get().load(imageUrl).into(new Target() { //carico l'immagine in cache
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND); //Inizializzo Intent da fare
                i.setType("image/*"); //setto il tipo del contenuto
                i.putExtra(Intent.EXTRA_STREAM, Utils.getLocalBitmapUri(requireContext(), bitmap)); //Inserisco l'immagine
                i.putExtra(Intent.EXTRA_TEXT, BASE_TEXT + text); //Inserisco il testo per l'immagine
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //Uso i permessi
                startActivity(Intent.createChooser(i, "Share Image")); //Inizio l'activity
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { //Nel caso non riesco a prelevare l'immagine
                Log.e(TAG, "Impossibile prelevare l'immagine");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { //per prelevare l'immagine
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) { //Listener per capire che Layout mostrare
        setFragmentLayout(fragments[tab.getPosition()]); //imposto il frammento
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}