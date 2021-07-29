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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import it.adriano.tumino.gamepoint.MainActivity;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private FragmentRegisterBinding binding;

    private boolean correctEmail;
    private boolean correctPassword;
    private boolean correctName;
    private boolean correctSurname;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.emailRegister.addTextChangedListener(emailTextWatcher);

        binding.passwordRegister.addTextChangedListener(passwordTextWatcher);

        binding.nameRegister.addTextChangedListener(nameTextWatcher);

        binding.surnameRegister.addTextChangedListener(surnameTextWatcher);

        binding.signupButton.setOnClickListener(registrationAccount);

        binding.goToSignIn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.sign_in_action));

        return binding.getRoot();
    }

    View.OnClickListener registrationAccount = v -> {
        if (correctEmail && correctPassword && correctName && correctSurname) {
            final String email = binding.emailRegister.getText().toString();
            final String password = binding.passwordRegister.getText().toString();
            final String name = binding.nameRegister.getText().toString();
            final String surname = binding.surnameRegister.getText().toString();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.fetchSignInMethodsForEmail(Objects.requireNonNull(email))
                    .addOnCompleteListener(task -> {
                        if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                            if (!isNewUser) {
                                Toast.makeText(requireContext(), "This email is already associated with a user", Toast.LENGTH_SHORT).show();
                            } else {
                                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(requireActivity(), authResult -> {
                                    Log.i(TAG, getString(R.string.user_created));
                                    HashMap<String, String> otherInformation = new HashMap<>();
                                    otherInformation.put("email", email);
                                    otherInformation.put("name", name);
                                    otherInformation.put("surname", surname);

                                    assert auth.getCurrentUser() != null;
                                    String userId = auth.getCurrentUser().getUid();
                                    otherInformation.put("uid", userId);

                                    DatabaseReference userInDB = FirebaseDatabase.getInstance().getReference();
                                    userInDB.child("users").child(userId).setValue(otherInformation);

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name + " " + surname)
                                            .build();
                                    FirebaseUser user = auth.getCurrentUser();
                                    assert user != null;
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(task1 -> Log.i(TAG, "update display Name"));
                                    Log.i(TAG, getString(R.string.update_information));

                                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                                    startActivity(intent);
                                    requireActivity().finish();
                                }).addOnFailureListener(requireActivity(), e -> {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), R.string.error_registration, Toast.LENGTH_SHORT).show();
        }
    };

    TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.emailRegisterLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctEmail = s.toString().matches(getString(R.string.email_regex));
            if (!correctEmail && !s.toString().isEmpty())
                binding.emailRegisterLayout.setError(getString(R.string.wrong_email));
        }
    };

    TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.passwordRegisterLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctPassword = s.toString().matches(getString(R.string.password_regex));
            if (!correctPassword && !s.toString().isEmpty()) {
                String stringBuilder = getString(R.string.wrong_password) + "\n" + getString(R.string.password_elements);
                binding.passwordRegisterLayout.setError(stringBuilder);
            }

        }
    };

    TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.nameRegisterLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctName = !s.toString().isEmpty();
            if (!correctName) binding.nameRegisterLayout.setError(getString(R.string.wrong_name));
        }
    };

    TextWatcher surnameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.surnameRegisterLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctSurname = !s.toString().isEmpty();
            if (!correctSurname)
                binding.surnameRegisterLayout.setError(getString(R.string.wrong_surname));
        }
    };
}