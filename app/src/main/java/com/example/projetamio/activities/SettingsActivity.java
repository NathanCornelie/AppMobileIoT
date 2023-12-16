package com.example.projetamio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.projetamio.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String SHAREDPREFS = "MyPrefs";
    private static final String TOGGLE_STATE = "toggle_state";
    private static final String CHECKBOX_STATE = "checkbox_state";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        boolean toggleState = sharedPreferences.getBoolean(TOGGLE_STATE, false);
        boolean checkboxState = sharedPreferences.getBoolean(CHECKBOX_STATE, false);
        if(toggleState){
            //TODO : faire le truc de démarage on boot
        }else{
            Log.e("", "onCreate: state changed" );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(TOGGLE_STATE, true);
            editor.apply();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(home);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat switchPreferenceCompat = findPreference("toggle_state");
            switchPreferenceCompat.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(TOGGLE_STATE, (boolean) newValue);
                    editor.apply();
                    return true;
                }
            });
        }


    }
}