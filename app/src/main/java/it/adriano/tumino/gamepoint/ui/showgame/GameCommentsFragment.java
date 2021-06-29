package it.adriano.tumino.gamepoint.ui.showgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.adriano.tumino.gamepoint.adapter.recyclerview.CommentsAdapter;
import it.adriano.tumino.gamepoint.data.Comment;
import it.adriano.tumino.gamepoint.data.storegame.Game;
import it.adriano.tumino.gamepoint.databinding.FragmentGameCommentsBinding;

public class GameCommentsFragment extends Fragment {

    private LinearLayout linearLayout;
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        it.adriano.tumino.gamepoint.databinding.FragmentGameCommentsBinding binding = FragmentGameCommentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;
        linearLayout = binding.linearLayoutEmpty;
        getComments(gameSearchResult.getTitle(), store);

        FloatingActionButton button = binding.addingCommentButton;
        button.setOnClickListener(v -> {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("autore", "Adriano Tumino");
            hashMap.put("descrizione", "Che bel gioco, popo bello, molto bello, decisaemnte troppo bello, talmente bello che diventa brutto");
            hashMap.put("rating", 10);
            hashMap.put("data", "29/06/2021");
            String title = gameSearchResult.getTitle().replaceAll("\\s+", "");
            firestore.collection("Games").document(title + store)
                    .collection("Comments").document()
                    .set(hashMap)
                    .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Salvato con Successo", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(exception -> Toast.makeText(v.getContext(), "Errore nel salvataggio", Toast.LENGTH_SHORT).show());
        });

        return root;
    }

    private void getComments(String gameTitle, String store) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String title = gameTitle.replaceAll("\\s+", "");

        firestore.collection("Games")
                .document(title + store)
                .collection("Comments")
                .get()
                .addOnCompleteListener(task -> { //processo asincrono su un altro thread
                    if (task.isSuccessful()) {
                        ArrayList<Comment> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HashMap<String, Object> element = (HashMap<String, Object>) document.getData();
                            String autore, descrizione, data;
                            autore = (String) element.get("autore");
                            descrizione = (String) element.get("descrizione");
                            data = (String) element.get("data");
                            int rating = 10;
                            list.add(new Comment("", autore, descrizione, rating, data));
                        }
                        visualizzeAllComments(list);
                    } else { //se non ci sono dati abbiamo un insuccesso
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void visualizzeAllComments(List<Comment> list) {
        recyclerView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        CommentsAdapter commentsAdapter = new CommentsAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentsAdapter);
    }


}