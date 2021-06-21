package it.adriano.tumino.gamepoint.ui.showgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.GalleryAdapter;
import it.adriano.tumino.gamepoint.data.storegame.Game;


public class GalleryFragment extends Fragment {

    public GalleryFragment() {
        // Required empty public constructor
    }

    private Bundle information;
    private String store;
    private Game game;
    //private GameOld gameOld;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            information = getArguments();
            store = information.getString("store");
            //gameOld = information.getParcelable("game");
            game = information.getParcelable("game");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.galleryListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setAdapter(new GalleryAdapter(getContext(), gameOld.getScreenshots()));
        recyclerView.setAdapter(new GalleryAdapter(getContext(), game.getScreenshotsUrl()));
        return view;
    }
}