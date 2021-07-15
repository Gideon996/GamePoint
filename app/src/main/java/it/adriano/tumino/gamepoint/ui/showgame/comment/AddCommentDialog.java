package it.adriano.tumino.gamepoint.ui.showgame.comment;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String format = sdf.format(Calendar.getInstance().getTime());
            Comment comment = new Comment(displayName, editText.getText().toString(), ratingBar.getRating(), format);
            saveComment(comment, view);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();

        assert dialog != null;
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
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!s.toString().trim().isEmpty());
            }
        });
    }

    private void saveComment(Comment comment, View view) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Games").document(title + store)
                .collection("Comments").document()
                .set(comment)
                .addOnSuccessListener(aVoid -> Toast.makeText(view.getContext(), "Commento Aggiunto correttamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(exception -> Toast.makeText(view.getContext(), "Errore: Riprovare fra qualche secondo", Toast.LENGTH_SHORT).show());
    }
}
