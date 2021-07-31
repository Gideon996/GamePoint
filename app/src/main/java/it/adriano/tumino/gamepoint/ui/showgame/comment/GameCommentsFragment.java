package it.adriano.tumino.gamepoint.ui.showgame.comment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private final static String TAG = "GameCommentsFragment";

    private FirebaseFirestore firebaseFirestore;
    private FragmentGameCommentsBinding binding;

    private StoreGame storeGameSearchResult;

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
        binding = FragmentGameCommentsBinding.inflate(inflater, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        onSnapshotComments(storeGameSearchResult.getTitle(), storeGameSearchResult.getStore());
        binding.addingCommentButton.setOnClickListener(addComment);
        return binding.getRoot();
    }

    private void onSnapshotComments(String gameTitle, String store) {
        String title = gameTitle.replaceAll("\\s+", "");

        firebaseFirestore.collection("Games")
                .document(title + store)
                .collection("Comments")
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
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
        Log.i(TAG, "Show all comments, if there are");
        if (list.size() != 0) {
            binding.noCommentsLinearLayout.setVisibility(View.GONE);
            CommentsAdapter commentsAdapter = new CommentsAdapter(list);
            binding.commentsRecyclerView.setHasFixedSize(true);
            binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.commentsRecyclerView.setAdapter(commentsAdapter);
        }
    }

    final View.OnClickListener addComment = v -> {
        Log.i(TAG, "Open dialog to add new comment");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        AddCommentDialog addCommentDialog = new AddCommentDialog(storeGameSearchResult, auth.getCurrentUser());
        addCommentDialog.show(getChildFragmentManager(), "Add Comment");
    };
}