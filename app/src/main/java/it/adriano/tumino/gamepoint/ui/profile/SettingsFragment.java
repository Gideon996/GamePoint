package it.adriano.tumino.gamepoint.ui.profile;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import it.adriano.tumino.gamepoint.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}