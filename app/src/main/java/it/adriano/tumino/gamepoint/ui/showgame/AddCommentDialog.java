package it.adriano.tumino.gamepoint.ui.showgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.Comment;

public class AddCommentDialog extends DialogFragment {

    private final String title;
    private final String store;
    private final String displayName;
    private EditText editText;

    public AddCommentDialog(String title, String store, String displayName) {
        this.title = title.replaceAll("\\s+", "");
        this.store = store;
        this.displayName = displayName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.add_comment_layout, null);
        builder.setView(view);
        builder.setTitle("Add Comment");
        editText = view.findViewById(R.id.commentDescription);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        builder.setPositiveButton("Add Comment", (dialog, which) -> {
            Comment comment = new Comment(displayName, editText.getText().toString(), ratingBar.getRating(), "30/06/2021");
            saveComment(comment, view);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    private void saveComment(Comment comment, View view) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Games").document(title + store)
                .collection("Comments").document()
                .set(comment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(view.getContext(), "Commento Aggiunto correttamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(view.getContext(), "Errore: Riprovare fra qualche secondo", Toast.LENGTH_SHORT).show();
                });
    }
}
