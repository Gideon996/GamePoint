package it.adriano.tumino.gamepoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "AuthenticationActivity";

    private NavController navController;

    private static final boolean EMULATOR = true;

    static {
        if (EMULATOR) {
            Log.d(TAG, "Initializing the emulator for Firestore and for Authentication");
            FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099);
            FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080);
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            FirebaseFirestore.getInstance().setFirestoreSettings(settings);
            FirebaseStorage.getInstance().useEmulator("10.0.2.2", 9199);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        navController = Navigation.findNavController(this, R.id.nav_host_authentication);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.loginFragment, R.id.registerFragment)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.i(TAG, "Check if the user has already logged in before");
        if (user != null) {
            mAuth.fetchSignInMethodsForEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                if (task.getResult() != null && task.getResult().getSignInMethods() != null) {
                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                    if (!isNewUser) {
                        Log.d(TAG, "Existing user, Autologin");
                        Toast.makeText(this, String.format(getString(R.string.welcome_back), user.getDisplayName()), Toast.LENGTH_SHORT).show();
                        successfulLogin(user);
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }


    private void successfulLogin(FirebaseUser user) {
        Log.i(TAG, "MainActivity Start");
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}