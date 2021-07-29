package it.adriano.tumino.gamepoint.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.FragmentChangePasswordBinding;

public class ChangePasswordFragment extends Fragment {
    private boolean correctEmail;
    private boolean correctOldPassword;
    private boolean correctNewPassword;

    private FragmentChangePasswordBinding binding;

    public ChangePasswordFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);

        binding.changePasswordEmail.addTextChangedListener(emailTextWatcher);
        binding.oldPassword.addTextChangedListener(oldPasswordTextWatcher);
        binding.newPassword.addTextChangedListener(newPasswordTextWatcher);

        binding.changeButton.setOnClickListener(changeClick);

        return binding.getRoot();
    }

    final TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.emailChangePasswordLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctEmail = s.toString().matches(getString(R.string.email_regex));
            if (!correctEmail && !s.toString().isEmpty()) {
                binding.emailChangePasswordLayout.setError(getString(R.string.wrong_email));
            }
        }
    };

    final TextWatcher oldPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.oldPasswordLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctOldPassword = s.toString().matches(getString(R.string.password_regex));
            if (!correctOldPassword && !s.toString().isEmpty()) {
                binding.oldPasswordLayout.setError(getString(R.string.wrong_password));
            }
        }
    };

    final TextWatcher newPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.newPasswordLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctNewPassword = s.toString().matches(getString(R.string.password_regex));
            if (!correctNewPassword && !s.toString().isEmpty()) {
                binding.newPasswordLayout.setError(getString(R.string.wrong_password));
            }
        }
    };

    final View.OnClickListener changeClick = v -> {
        if (correctEmail && correctOldPassword && correctNewPassword) {
            final String email = binding.changePasswordEmail.getText().toString();
            final String oldPassword = binding.oldPassword.getText().toString();
            final String newPassword = binding.newPassword.getText().toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, oldPassword);

            assert user != null;
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(v.getContext(), R.string.password_changed, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(v.getContext(), R.string.error_change_password, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(v.getContext(), R.string.error_change_password, Toast.LENGTH_SHORT).show();
                        }
                    });

            Navigation.findNavController(v).navigateUp();

        }
    };
}