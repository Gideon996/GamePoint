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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import it.adriano.tumino.gamepoint.MainActivity;
import it.adriano.tumino.gamepoint.R;

public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;

    private boolean correctEmail;
    private boolean correctPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.emailEditText2);
        email.addTextChangedListener(emailTextWatcher);

        password = view.findViewById(R.id.passwordEditText2);
        password.addTextChangedListener(passwordTextWatcher);

        Button button = view.findViewById(R.id.button3);
        button.setOnClickListener(loginListener);

        TextView textView = view.findViewById(R.id.loginTextView);
        textView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.sign_up_action));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            email.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String emailValidator = requireActivity().getString(R.string.email_regex);
            correctEmail = s.toString().matches(emailValidator);
            if (!correctEmail) email.setError("Inserisci un email valida");
        }
    };

    TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            password.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String passwordValidator = requireActivity().getString(R.string.password_regex);
            correctPassword = s.toString().matches(passwordValidator);
            if (!correctPassword) password.setError("Inserisci una password valida");
        }
    };

    View.OnClickListener loginListener = v -> {
        if (correctEmail && correctPassword) {
            final String email = this.email.getText().toString();
            final String password = this.password.getText().toString();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(requireActivity(), authResult -> {
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Errore di login, riprova", Toast.LENGTH_LONG).show());
        }
    };
}