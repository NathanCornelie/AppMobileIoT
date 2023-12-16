package com.example.projetamio.activities;

import static com.example.projetamio.Utils.Notification.sendNotification;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetamio.R;
import com.example.projetamio.Services.APIService;
import com.example.projetamio.Services.MonitoringService;
import com.example.projetamio.Utils.GetDataTask;
import com.example.projetamio.Utils.Notification;

public class MainActivity extends AppCompatActivity {

    com.example.projetamio.Services.APIService mService;
    boolean mBound = false;
    Context context = this;
     SharedPreferences sharedPreferences;
    public TextView tv4,tv6,tv5,tv7;
    private static final String SHAREDPREFS = "MyPrefs";
    private static final String TOGGLE_STATE = "toggle_state";
    private static final String CHECKBOX_STATE = "checkbox_state";
    private Button SettingsButton;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to APIService, cast the IBinder and get APIService instance.
            APIService.LocalBinder binder = (APIService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String label1 = intent.getStringExtra("label1");
            String value1 = intent.getStringExtra("value1");
            String label2 = intent.getStringExtra("label2");
            String value2 = intent.getStringExtra("value2");
            tv4.setText(label1);
            tv5.setText(value1);
            tv6.setText(label2);
            tv7.setText(value2);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "Lucie : Création de l'activité");
        setContentView(R.layout.activity_main);
        this.mService = new APIService();

        // Crée le canal de notification
        Notification notification = new Notification(this);
        notification.createNotificationChannel();

         sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
         String name = sharedPreferences.getString("name", "default");
         if(name.equals("default")){
             SharedPreferences.Editor editor = sharedPreferences.edit();
             editor.putString("name", "Lucie");
             editor.apply();
         }

        /*findViewById(R.id.tb1).setOnClickListener(new HandleClickToggleButton());*/

        ToggleButton MonitoringToggleBtn = findViewById(R.id.MonitoringButton);
        boolean isOn = sharedPreferences.getBoolean("toggle_state", false);
        MonitoringToggleBtn.setChecked(isOn);
        if (isOn) {
            Intent intent = new Intent(MainActivity.this, MonitoringService.class);
            startService(intent);
        }
        SettingsButton = findViewById(R.id.SettingsButton);
        tv6 = findViewById(R.id.textView6);
        tv4 = findViewById(R.id.textView4);
        tv5 = findViewById(R.id.textView5);
        tv7 = findViewById(R.id.textView7);


        SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle_state", MonitoringToggleBtn.isChecked());
                editor.apply();
                Intent intent = new Intent(MainActivity.this, com.example.projetamio.activities.SettingsActivity.class);
                startActivity(intent);
            }
        });
        registerReceiver(receiver, new IntentFilter("data"));
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        MonitoringToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enregistrez l'état actuel du ToggleButton dans SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle_state", isChecked);
                editor.apply();

                if (isChecked) {
                    // Le ToggleButton est activé (ON), lancez le service
                    Intent intent = new Intent(MainActivity.this, MonitoringService.class);
                    startService(intent);
                } else {
                    // Le ToggleButton est désactivé (OFF), arrêtez le service
                    Intent intent = new Intent(MainActivity.this, MonitoringService.class);
                    stopService(intent);
                }
            }
        });

   /*     CheckBox checkBox = findViewById(R.id.cb1);
        boolean isChecked = sharedPreferences.getBoolean("checkbox_state", false);


        checkBox.setChecked(isChecked);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enregistrez l'état actuel de la checkbox dans SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("checkbox_state", isChecked);
                editor.apply();
            }
        });*/

        findViewById(R.id.APIButton).setOnClickListener(new HandleClickToggleButton());
        findViewById(R.id.EmailButton).setOnClickListener(new EmailToggleBtnAction());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, com.example.projetamio.Services.APIService.class);
        Log.d("MainActivity", "Lucie : bindService before");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d("MainActivity", "Lucie : bindService after");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "Lucie : Arrêt de l'activité");


    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    private class HandleClickToggleButton implements View.OnClickListener {
        public void onClick(View arg0) {
            ToggleButton btn = (ToggleButton) arg0; //cast view to a button
            // update the TextView text
            if (btn.isChecked()) {

                Intent intent = new Intent(MainActivity.this, com.example.projetamio.Services.APIService.class);
                startService(intent);
            } else {

                Intent intent = new Intent(MainActivity.this, com.example.projetamio.Services.APIService.class);
                stopService(intent);
            }
        }
    }

    private class EmailToggleBtnAction implements View.OnClickListener {
        public void onClick(View arg0) {
            ToggleButton btn = (ToggleButton) arg0; //cast view to a button
            // update the TextView text
            if (btn.isChecked()) {
                Intent intent = new Intent(MainActivity.this, com.example.projetamio.Services.EmailService.class);
                startService(intent);
                sendNotification(context, "Bonjour");

            } else {
                Intent intent = new Intent(MainActivity.this, com.example.projetamio.Services.EmailService.class);
                stopService(intent);
            }
        }
    }

    private class HandleClickButton implements View.OnClickListener {
        public void onClick(View arg0) {
            // Exécutez la tâche asynchrone pour récupérer les données
            new GetDataTask(mService, context).execute();
        }
    }

}
