package it.adriano.tumino.gamepoint.ui.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
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
        View view = binding.getRoot();

        binding.loginEmail.addTextChangedListener(emailTextWatcher);

        binding.loginPassword.addTextChangedListener(passwordTextWatcher);

        binding.loginButton.setOnClickListener(loginListener);

        binding.goToSignUp.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.sign_up_action));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.emailLoginLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String emailValidator = getString(R.string.email_regex);
            correctEmail = s.toString().matches(emailValidator);
            if (!correctEmail) binding.emailLoginLayout.setError(getString(R.string.wrong_email));
        }
    };

    TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.passwordLoginLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String passwordValidator = getString(R.string.password_regex);
            correctPassword = s.toString().matches(passwordValidator);
            if (!correctPassword)
                binding.passwordLoginLayout.setError(getString(R.string.wrong_password));
        }
    };

    View.OnClickListener loginListener = v -> {
        if (correctEmail && correctPassword) {
            final String email = binding.loginEmail.getText().toString();
            final String password = binding.loginPassword.getText().toString();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(requireActivity(), authResult -> {
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }).addOnFailureListener(e -> Toast.makeText(getContext(), R.string.login_error, Toast.LENGTH_LONG).show());
        }
    };

}