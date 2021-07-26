package it.adriano.tumino.gamepoint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isAuthenticate;
    private final MutableLiveData<FirebaseAuth> authentication;

    public AuthenticationViewModel() {
        isAuthenticate = new MutableLiveData<>();
        authentication = new MutableLiveData<>();
        isAuthenticate.setValue(false);
    }

    public MutableLiveData<Boolean> getIsAuthenticate() {
        return isAuthenticate;
    }

    public MutableLiveData<FirebaseAuth> getAuthentication() {
        return authentication;
    }

    public void setUser(FirebaseAuth mAuth) {
        authentication.setValue(mAuth);
    }

    public void setIsAuthenticate(boolean value) {
        isAuthenticate.setValue(value);
    }
}
