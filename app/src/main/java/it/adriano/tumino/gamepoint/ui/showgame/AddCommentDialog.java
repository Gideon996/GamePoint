package it.adriano.tumino.gamepoint.ui.showgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.CommentsAdapter;
import it.adriano.tumino.gamepoint.data.Comment;

public class AddCommentDialog extends DialogFragment {

    private final String title;
    private final String store;
    private final RecyclerView recyclerView;
    private final String displayName;
    private final CommentsAdapter adapter;

    public AddCommentDialog(String title, String store, RecyclerView recyclerView, String displayName, CommentsAdapter adapter) {
        this.title = title.replaceAll("\\s+", "");
        this.store = store;
        this.recyclerView = recyclerView;
        this.displayName = displayName;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.add_comment_layout, null);
        builder.setView(view);
        builder.setTitle("Nuovo libro");

        builder.setPositiveButton("Add Comment", (dialog, which) -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            EditText editTextTextPersonName = view.findViewById(R.id.commentDescription);
            if (editTextTextPersonName.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Aggiungi un commento", Toast.LENGTH_SHORT).show();
            } else {
                Comment comment = new Comment(displayName, editTextTextPersonName.getText().toString(), 10, "30/06/2021");
                saveComment(comment);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        return builder.create();
    }

    private void saveComment(Comment comment) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Games").document(title + store)
                .collection("Comments").document()
                .set(comment)
                .addOnSuccessListener(aVoid -> {
                    //adapter.newCommentAdded(comment);
                })
                .addOnFailureListener(exception -> {
                    //Toast.makeText(getContext(), "Errore nel salvataggio", Toast.LENGTH_SHORT).show();
                });
    }
}
