package it.adriano.tumino.gamepoint.ui.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.AuthenticationActivity;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Button logout;
    private StorageReference storageReference;
    private String userID;

    private final ActivityResultLauncher<Intent> uploadImageFromGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Uri imageUri = data.getData();
                    uploadImageToFirebase(imageUri);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.setProfile(user);

        userID = (user != null) ? user.getUid() : null;
        storageReference = FirebaseStorage.getInstance().getReference();

        logout = binding.logOutButton;

        return root;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout.setOnClickListener(v -> AuthUI.getInstance()
                .signOut(view.getContext())
                .addOnCompleteListener(task -> {
                    Log.i(TAG, "User Log out");
                    Toast.makeText(view.getContext(), "User Signed Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(view.getContext(), AuthenticationActivity.class);
                    startActivity(i);
                }));

        binding.impostazioniBottone.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigate_to_settings));

        binding.changeProfileImageButton.setOnClickListener(v -> changeProfileImage());
    }

    public void changeProfileImage() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        uploadImageFromGallery.launch(openGalleryIntent);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("users/" + userID + "/profile.jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> Picasso.get().load(uri).fit().into(binding.profileImage)))
                .addOnFailureListener(e -> Toast.makeText(requireActivity().getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show());
    }
}