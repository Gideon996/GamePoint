package it.adriano.tumino.gamepoint.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.AuthenticationActivity;
import it.adriano.tumino.gamepoint.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Button logout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView textView = binding.displayName;
        textView.setText(user.getDisplayName());

        if (user.getPhotoUrl() != null)
            Picasso.get().load(user.getPhotoUrl()).into(binding.profileImage);

        logout = binding.logOutButton;

        return root;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout.setOnClickListener(v -> AuthUI.getInstance()
                .signOut(view.getContext())
                .addOnCompleteListener(task -> {
                    Toast.makeText(view.getContext(), "User Signed Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(view.getContext(), AuthenticationActivity.class);
                    startActivity(i);
                }));

    }
}