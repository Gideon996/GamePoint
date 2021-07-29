package it.adriano.tumino.gamepoint.ui.showgame.comment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.Comment;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;

public class AddCommentDialog extends DialogFragment {

    private final StoreGame storeGame;
    private final FirebaseUser user;
    private EditText editText;

    public AddCommentDialog(StoreGame storeGame, FirebaseUser user) {
        this.storeGame = storeGame;
        this.user = user;
    }

    @NonNull
    @SuppressLint("SimpleDateFormat")
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.add_comment_layout, null);
        builder.setView(view);
        builder.setTitle("Add Comment");

        editText = view.findViewById(R.id.commentDescription);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        builder.setPositiveButton("Add Comment", (dialog, which) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String format = sdf.format(Calendar.getInstance().getTime());
            Comment comment = new Comment(user.getDisplayName(), user.getUid(), editText.getText().toString(), ratingBar.getRating(), format);
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
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String title = storeGame.getTitle().replaceAll("\\s+", "");
        firebaseFirestore.collection("Games").document(title + storeGame.getStore())
                .collection("Comments").document()
                .set(comment)
                .addOnSuccessListener(aVoid -> Toast.makeText(view.getContext(), R.string.comment_added, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(exception -> Toast.makeText(view.getContext(), R.string.error_comment, Toast.LENGTH_SHORT).show());
    }
}
