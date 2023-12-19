package com.example.projetamio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.projetamio.R;

public class SettingsActivity extends AppCompatActivity {
    private static final String SHAREDPREFS = "MyPrefs";
    private static final String TOGGLE_ONBOOT = "toggle_state";
    private static final String TOGLLE_SENDMAIL = "toggle_sendMail";
    private static final String BEGIN_WEEK_SLIDER_STATE = "bws_state";
    private static final String END_WEEK_SLIDER_STATE = "ews_state";
    private static final String BEGIN_WEEKEND_SLIDER_STATE = "bwes_state";
    private static final String END_WEEKEND_SLIDER_STATE = "ewes_state";
    private static final String EMAIL_SENDTO = "email_sendto";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
       if(getActionBar()!=null) getSupportActionBar().setTitle("Paramètres");

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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {



        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat switchPreferenceCompatBoot = findPreference(TOGGLE_ONBOOT);
            SwitchPreferenceCompat switchPreferenceCompatEMail = findPreference(TOGLLE_SENDMAIL);
           if(switchPreferenceCompatBoot != null) switchPreferenceCompatBoot.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(TOGGLE_ONBOOT, (boolean) newValue);
                    editor.apply();
                    return true;
                }
            });
            switchPreferenceCompatEMail.setOnPreferenceChangeListener(new androidx.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(TOGLLE_SENDMAIL, (boolean) newValue);
                    editor.apply();
                    return true;
                }
            });
            EditTextPreference editTextPreference = findPreference("email");
            editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(EMAIL_SENDTO, newValue.toString());
                    editor.apply();
                    editTextPreference.setSummary(newValue.toString());
                    return true;
                }
            });
            SeekBarPreference seekBarStartWeek = findPreference("beginweekSeekBar");

            seekBarStartWeek.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(BEGIN_WEEK_SLIDER_STATE, (int) newValue);
                    editor.apply();
                    return true;
                }
            });
            SeekBarPreference seekBarEndWeek = findPreference("endweekSeekBar");
            seekBarEndWeek.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(END_WEEK_SLIDER_STATE, (int) newValue);
                    editor.apply();
                    return true;
                }
            });
            SeekBarPreference seekBarStartWeekend = findPreference("beginweekendSeekBar");
            seekBarStartWeekend.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(BEGIN_WEEKEND_SLIDER_STATE, (int) newValue);
                    editor.apply();
                    return true;
                }
            });
            SeekBarPreference seekBarEndWeekend = findPreference("endweekendSeekBar");
            seekBarEndWeekend.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(END_WEEKEND_SLIDER_STATE, (int) newValue);
                    editor.apply();
                    return true;
                }
            });

        }
    }
}
