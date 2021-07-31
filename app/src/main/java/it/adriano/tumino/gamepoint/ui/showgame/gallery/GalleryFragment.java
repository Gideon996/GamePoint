package it.adriano.tumino.gamepoint.ui.showgame.gallery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.GalleryAdapter;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;

public class GalleryFragment extends Fragment {
    private final static String TAG = "GalleryFragment";
    private StoreGame storeGame;

    public GalleryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle information = getArguments();
            storeGame = information.getParcelable("game");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        Log.i(TAG, "creation gallery view");
        RecyclerView recyclerView = view.findViewById(R.id.galleryListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new GalleryAdapter(getContext(), storeGame.getScreenshotsUrl()));
        return view;
    }
}