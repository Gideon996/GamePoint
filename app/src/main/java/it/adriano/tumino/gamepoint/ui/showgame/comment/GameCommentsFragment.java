package it.adriano.tumino.gamepoint.ui.showgame.comment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.adapter.recyclerview.CommentsAdapter;
import it.adriano.tumino.gamepoint.data.Comment;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.databinding.FragmentGameCommentsBinding;

public class GameCommentsFragment extends Fragment {

    private LinearLayout linearLayout;
    private RecyclerView recyclerView;

    private StoreGame storeGameSearchResult;
    FloatingActionButton button;

    public GameCommentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey("game"))
                storeGameSearchResult = getArguments().getParcelable("game");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentGameCommentsBinding binding = FragmentGameCommentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.commentsRecyclerView;
        linearLayout = binding.noCommentsLinearLayout;
        onSnapshotComments(storeGameSearchResult.getTitle(), storeGameSearchResult.getStore());

        button = binding.addingCommentButton;


        return root;
    }

    private void onSnapshotComments(String gameTitle, String store) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String title = gameTitle.replaceAll("\\s+", "");

        firestore.collection("Games")
                .document(title + store)
                .collection("Comments").addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w("TEST", "Listen failed.", e);
                return;
            }

            List<Comment> comments = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Comment comment = doc.toObject(Comment.class);
                    comments.add(comment);
                }
            }
            visualizeAllComments(comments);
        });
    }

    private void visualizeAllComments(List<Comment> list) {
        if (list.size() != 0) {
            linearLayout.setVisibility(View.GONE);
        }

        CommentsAdapter commentsAdapter = new CommentsAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentsAdapter);

        button.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            AddCommentDialog addCommentDialog = new AddCommentDialog(storeGameSearchResult, auth.getCurrentUser());
            addCommentDialog.show(getChildFragmentManager(), "Add Comment");
        });
    }


}