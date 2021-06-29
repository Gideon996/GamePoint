package it.adriano.tumino.gamepoint.ui.showgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.Comment;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.data.storegame.Game;
import it.adriano.tumino.gamepoint.databinding.FragmentGameCommentsBinding;
import it.adriano.tumino.gamepoint.databinding.FragmentNewsBinding;

public class GameCommentsFragment extends Fragment {

    private FragmentGameCommentsBinding binding;
    private TextView textView;
    private RecyclerView recyclerView;

    private Game gameSearchResult;
    private String store;

    public GameCommentsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey("game"))
                gameSearchResult = getArguments().getParcelable("game");
            if (getArguments().containsKey("store")) store = getArguments().getString("store");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameCommentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textView3;
        recyclerView = binding.recyclerView;
        getComments(gameSearchResult.getTitle(), store);

        return root;
    }

    private void getComments(String gameTitle, String store) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Games")
                .document(gameTitle + store)
                .collection("Comments")
                .get()
                .addOnCompleteListener(task -> { //processo asincrono su un altro thread
                    if (task.isSuccessful()) {
                        Log.d("TEST", "SUCCESSO");
                        ArrayList<Comment> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Comment comment = document.toObject(Comment.class);
                            Log.d("TEST", comment.getAutore() + comment.getDescrizione());
                            list.add(comment);
                        }
                        visualizzeAllComments(list);
                    } else { //se non ci sono dati abbiamo un insuccesso
                        Log.d("TEST", "INSUCCESSO");
                        recyclerView.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void visualizzeAllComments(List<Comment> list) {
        //impostare il tutto
    }


}