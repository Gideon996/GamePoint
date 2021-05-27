package it.adriano.tumino.gamepoint.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.adriano.tumino.gamepoint.R;

public class SearchFragment extends Fragment {

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_game, container, false);

        Button gotoFragment2 = view.findViewById(R.id.fragment_fragment1_gotofragment2);
        gotoFragment2.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.search_action);
        });

        return view;
    }
}