package it.adriano.tumino.gamepoint.ui.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.FragmentResetPasswordBinding;

public class ResetPasswordFragment extends Fragment {
    private static final String TAG = "ResetPasswordFragment";

    private FragmentResetPasswordBinding binding;
    private FirebaseAuth mAuth;

    private boolean correctEmail;

    public ResetPasswordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        binding.resetButton.setOnClickListener(resetPassword);
        binding.emailReset.addTextChangedListener(emailTextWatcher);

        return binding.getRoot();
    }

    final TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.emailResetLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctEmail = s.toString().matches(getString(R.string.email_regex));
            if (!correctEmail && !s.toString().isEmpty()) {
                binding.emailResetLayout.setError(getString(R.string.wrong_email));
            }
        }
    };

    final View.OnClickListener resetPassword = v -> {
        if (correctEmail) {
            String email = binding.emailReset.getText().toString().trim();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Email correctly sent");
                            Toast.makeText(getContext(), R.string.successful_reset_password, Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(v).navigateUp();
                        } else {
                            Log.e(TAG, "unable to send the email");
                            Toast.makeText(getContext(), R.string.fail_reset_password, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Enter a correct email please", Toast.LENGTH_SHORT).show();
        }
    };
}