package com.example.projetamio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.projetamio.R;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String SHAREDPREFS = "MyPrefs";
    private static final String TOGGLE_STATE = "toggle_state";

    private static final String BEGIN_WEEK_SLIDER_STATE = "bws_state";
    private static final String END_WEEK_SLIDER_STATE = "ews_state";
    private static final String BEGIN_WEEKEND_SLIDER_STATE = "bwes_state";
    private static final String END_WEEKEND_SLIDER_STATE = "ewes_state";

    private static final String EDITTEXT_STATE = "edit_text_state";
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
        setupSeekBarAndTextView(BEGIN_WEEK_SLIDER_STATE, R.id.beginweekSeekBar, R.id.beginweekTextView);
        setupSeekBarAndTextView(END_WEEK_SLIDER_STATE, R.id.endweekSeekBar, R.id.endweekTextView);
        setupSeekBarAndTextView(BEGIN_WEEKEND_SLIDER_STATE, R.id.beginweekendSeekBar, R.id.beginweekendTextView);
        setupSeekBarAndTextView(END_WEEKEND_SLIDER_STATE, R.id.endweekendSeekBar, R.id.endweekendTextView);

        EditText editText = findViewById(R.id.emailEditText);
        editText.setText(sharedPreferences.getString(EDITTEXT_STATE, ""));

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(EDITTEXT_STATE, editText.getText().toString());
                    editor.apply();
                }
            }
        });
    }

    private void setupSeekBarAndTextView(String sharedPreferencesKey, int seekBarId, int textViewId) {
        int savedProgress = sharedPreferences.getInt(sharedPreferencesKey, 0);
        SeekBar seekBar = findViewById(seekBarId);
        seekBar.setProgress(savedProgress);
        TextView textView = findViewById(textViewId);
        textView.setText(textView.getText().toString().split(":")[0] + ": " + savedProgress+"h");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

                if (seekBar.getId() == R.id.beginweekSeekBar) {
                    int endWeekProgress = sharedPreferences.getInt(END_WEEK_SLIDER_STATE, 0);

                    if (progressChangedValue >= endWeekProgress) {
                        progressChangedValue = endWeekProgress;
                        seekBar.setProgress(progressChangedValue);
                    }
                } else if (seekBar.getId() == R.id.endweekSeekBar) {
                    int beginWeekProgress = sharedPreferences.getInt(BEGIN_WEEK_SLIDER_STATE, 0);

                    if (progressChangedValue <= beginWeekProgress) {
                        progressChangedValue = beginWeekProgress;
                        seekBar.setProgress(progressChangedValue);
                    }
                } else if (seekBar.getId() == R.id.beginweekendSeekBar) {
                    int endWeekendProgress = sharedPreferences.getInt(END_WEEKEND_SLIDER_STATE, 0);

                    if (progressChangedValue >= endWeekendProgress) {
                        progressChangedValue = endWeekendProgress;
                        seekBar.setProgress(progressChangedValue);
                    }
                } else if (seekBar.getId() == R.id.endweekendSeekBar) {
                    int beginWeekendProgress = sharedPreferences.getInt(BEGIN_WEEKEND_SLIDER_STATE, 0);

                    if (progressChangedValue <= beginWeekendProgress) {
                        progressChangedValue = beginWeekendProgress;
                        seekBar.setProgress(progressChangedValue);
                    }
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(sharedPreferencesKey, progressChangedValue);
                editor.apply();

                Log.e("", "onProgressChanged: " + progressChangedValue);
                textView.setText(textView.getText().toString().split(":")[0] + ": " + progressChangedValue+"h");
            }




            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("", "onStartTrackingTouch: " + progressChangedValue);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("", "onStopTrackingTouch: " + progressChangedValue);
            }
        });
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
    }}
