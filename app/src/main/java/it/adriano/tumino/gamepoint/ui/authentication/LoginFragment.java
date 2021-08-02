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
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

        checkLogin();

        binding.loginEmail.addTextChangedListener(emailTextWatcher);

        binding.loginPassword.addTextChangedListener(passwordTextWatcher);

        binding.loginButton.setOnClickListener(loginListener);

        binding.goToSignUp.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.sign_up_action));

        binding.forgotPassword.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.reset_password_action));

        return binding.getRoot();
    }

    private void checkLogin(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.i(TAG, "Check if the user has already logged in before");
        if (user != null) {
            mAuth.fetchSignInMethodsForEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                    if (!isNewUser) {
                        Log.d(TAG, "Existing user, Autologin");
                        Toast.makeText(getContext(), String.format(getString(R.string.welcome_back), user.getDisplayName()), Toast.LENGTH_SHORT).show();
                        successfulLogin(user);
                    }else{
                        setUpLogin();
                    }
                }else{
                    setUpLogin();
                }
            });
        }else{
            setUpLogin();
        }
    }

    private void setUpLogin(){
        binding.loginLayout.setVisibility(View.VISIBLE);
        binding.appNameText.setVisibility(View.GONE);
    }

    private void successfulLogin(FirebaseUser user) {
        Log.i(TAG, "MainActivity Start");
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        requireActivity().finish();
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