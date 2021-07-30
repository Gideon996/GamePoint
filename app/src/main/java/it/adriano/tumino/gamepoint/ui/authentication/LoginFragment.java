package it.adriano.tumino.gamepoint.ui.authentication;

import android.content.Intent;
import android.os.Bundle;

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

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.MainActivity;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;

    private boolean correctEmail;
    private boolean correctPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.loginEmail.addTextChangedListener(emailTextWatcher);

        binding.loginPassword.addTextChangedListener(passwordTextWatcher);

        binding.loginButton.setOnClickListener(loginListener);

        binding.goToSignUp.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.sign_up_action));

        binding.forgotPassword.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.reset_password_action));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    final TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.emailLoginLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctEmail = s.toString().matches(getString(R.string.email_regex));
            if (!correctEmail && !s.toString().isEmpty()) {
                binding.emailLoginLayout.setError(getString(R.string.wrong_email));
            }
        }
    };

    final TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.passwordLoginLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctPassword = s.toString().matches(getString(R.string.password_regex));
            if (!correctPassword && !s.toString().isEmpty()) {
                binding.passwordLoginLayout.setError(getString(R.string.wrong_password));
            }
        }
    };

    final View.OnClickListener loginListener = v -> {
        if (correctEmail && correctPassword) {
            final String email = binding.loginEmail.getText().toString();
            final String password = binding.loginPassword.getText().toString();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(requireActivity(), authResult -> {
                Log.i(TAG, "Login successful");
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }).addOnFailureListener(e -> {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getContext(), R.string.login_error, Toast.LENGTH_LONG).show();
            });
        }
    };
}