package it.adriano.tumino.gamepoint;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        if (user != null) { //L'applicazione ha salvato un account
            //L'app potrebbe avere un account salvato, ma non presente nel Database (cancellato manualmente)
            mAuth.fetchSignInMethodsForEmail(user.getEmail()).addOnCompleteListener(task -> {
                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                if(isNewUser){
                    createSignInIntent();
                }else {
                    Toast.makeText(this, "Bentornato " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    successfulLogin(user);
                }

            });
        } else {
            createSignInIntent();
        }
    }

    private void createSignInIntent() {
        //creo l'intent da visualizzare e lo lancio

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)  //impostare l'icona
                .setTheme(R.style.Theme_AppCompat) //impostare il tema
                .build();
        signInLauncher.launch(signInIntent);
    }

    // [START auth_fui_result]
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = mAuth.getCurrentUser();
            Toast.makeText(this, "Benvenuto " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            successfulLogin(user);
        } else {
            Toast.makeText(this, "Errore durante il login...", Toast.LENGTH_SHORT).show();
        }
    }

    private void successfulLogin(FirebaseUser user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent, bundle);
        finish();
    }
}