package it.adriano.tumino.gamepoint;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Arrays;
import java.util.List;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "AuthenticationActivity";

    private static final boolean EMULATOR = true;

    private FirebaseAuth mAuth;

    //lista dei metodi che vogliamo siano mostrati all'interno del login
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(), //Accesso con email e password personali
            new AuthUI.IdpConfig.GoogleBuilder().build());  //Accesso con account Google

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult( //nuovo metodo da firebase
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        if (EMULATOR) { //se uso l'emulatore imposto il localhost
            Log.d(TAG, "Initializing the emulator for Firestore and for Authentication");
            FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099);
            FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080);
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            FirebaseFirestore.getInstance().setFirestoreSettings(settings);
        }

        mAuth = FirebaseAuth.getInstance(); //prendo l'instanza di login
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.i(TAG, "Check if the user has already logged in before");
        if (user != null) { //L'applicazione ha salvato un account
            //L'app potrebbe avere un account salvato, ma non presente nel Database (cancellato manualmente)
            mAuth.fetchSignInMethodsForEmail(user.getEmail()).addOnCompleteListener(task -> {
                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                if (isNewUser) {
                    Log.d(TAG, "User not present in the database");
                    createSignInIntent();
                } else {
                    Log.d(TAG, "Existing user, Autologin");
                    Toast.makeText(this, "Welcome Back " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    successfulLogin(user);
                }
            });
        } else {
            Log.i(TAG, "New user, login interface initialization");
            createSignInIntent();
        }
    }

    private void createSignInIntent() {
        Log.d(TAG, "Login interface initialization");
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme) //impostare il tema
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Log.i(TAG, "Successful registration");
            FirebaseUser user = mAuth.getCurrentUser();
            Toast.makeText(this, "Welcom " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            successfulLogin(user);
        } else {
            Log.e(TAG, "User registration error");
            Toast.makeText(this, "SignIn error...", Toast.LENGTH_SHORT).show();
        }
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