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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import it.adriano.tumino.gamepoint.MainActivity;
import it.adriano.tumino.gamepoint.R;

public class RegisterFragment extends Fragment {

    //private final String emailValidator = requireActivity().getString(R.string.email_regex);
    //private final String passwordValidator = requireActivity().getString(R.string.password_regex);

    private EditText email;
    private EditText password;
    private EditText name;
    private EditText surname;

    private boolean correctEmail;
    private boolean correctPassword;
    private boolean correctName;
    private boolean correctSurname;

    String a = "The password must contain at least 6 characters, one uppercase character, one lowercase character and at least one number";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        email = view.findViewById(R.id.emailEditText);
        email.addTextChangedListener(emailTextWatcher);

        password = view.findViewById(R.id.passwordEditText);
        password.addTextChangedListener(passwordTextWatcher);

        name = view.findViewById(R.id.nameEditText);
        name.addTextChangedListener(nameTextWatcher);

        surname = view.findViewById(R.id.surnameEditText);
        surname.addTextChangedListener(surnameTextWatcher);

        Button registerButton = view.findViewById(R.id.button2);
        registerButton.setOnClickListener(registrationAccount);

        TextView login = view.findViewById(R.id.goToSignUp);
        login.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.sign_in_action));

        return view;
    }

    View.OnClickListener registrationAccount = v -> {
        if (correctEmail && correctPassword && correctName && correctSurname) {
            final String email = this.email.getText().toString();
            final String password = this.password.getText().toString();
            final String name = this.name.getText().toString();
            final String surname = this.surname.getText().toString();

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(requireActivity(), authResult -> {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("email", email);
                        data.put("name", name);
                        data.put("surname", surname);
                        String user_id = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        data.put("uid", user_id);
                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference();
                        current_user_db.child("users").child(user_id).setValue(data);

                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();

                    }).addOnFailureListener(requireActivity(), e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(getContext(), "Inserisci i campi correttamente", Toast.LENGTH_SHORT).show();
        }
    };

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

    TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            name.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctName = !s.toString().isEmpty();
            if (!correctName) name.setError("Inserisci il tuo nome");
        }
    };

    TextWatcher surnameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            surname.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctSurname = !s.toString().isEmpty();
            if (!correctSurname) surname.setError("Inserisci il tuo cognome");
        }
    };
}