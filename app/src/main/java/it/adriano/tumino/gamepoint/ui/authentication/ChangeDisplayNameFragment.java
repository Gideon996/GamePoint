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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.FragmentChangeDisplayNameBinding;

public class ChangeDisplayNameFragment extends Fragment {
    private final static String TAG = "ChangeDisplayNameFragment";

    private FragmentChangeDisplayNameBinding binding;
    private boolean correctNickName;

    public ChangeDisplayNameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChangeDisplayNameBinding.inflate(inflater, container, false);
        binding.changeNickNameButton.setOnClickListener(changeClick);
        binding.changeNickName.addTextChangedListener(nicknameTextWatcher);

        return binding.getRoot();
    }

    final TextWatcher nicknameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            binding.changeNickNameLayout.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            correctNickName = !s.toString().isEmpty() && s.toString().trim().length() >= 4;
            if (!correctNickName)
                binding.changeNickNameLayout.setError(getString(R.string.error_nickname));
        }
    };

    final View.OnClickListener changeClick = v -> {
        if (correctNickName) {
            final String newNickName = binding.changeNickName.getText().toString().trim();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newNickName)
                    .build();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                Toast.makeText(getContext(), R.string.correct_change_nickname, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            }).addOnFailureListener(requireActivity(), e -> {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getContext(), R.string.error_change_nickname, Toast.LENGTH_LONG).show();
            });
        } else {
            Toast.makeText(getContext(), R.string.error_nickname, Toast.LENGTH_SHORT).show();
        }
    };
}