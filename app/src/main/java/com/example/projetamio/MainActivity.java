package com.example.projetamio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.projetamio.DataCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    APIService mService;
    boolean mBound = false;
    private static final String CHANNEL_ID = "my_notification_channel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "Lucie : Création de l'activité");
        setContentView(R.layout.activity_main);

        // Crée le canal de notification
        createNotificationChannel();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        findViewById(R.id.tb1).setOnClickListener(new HandleClickToggleButton());

        ToggleButton tb2 = findViewById(R.id.tb2);
        boolean isOn = sharedPreferences.getBoolean("toggle_state", false);
        tb2.setChecked(isOn);
        if(isOn){
            Intent intent = new Intent(MainActivity.this, MonitoringService.class);
            startService(intent);
        }
        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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






        CheckBox checkBox = findViewById(R.id.cb1);
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
        });

        findViewById(R.id.bt1).setOnClickListener(new HandleClickButton());

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private class HandleClickToggleButton implements View.OnClickListener {
        public void onClick(View arg0) {
            ToggleButton btn = (ToggleButton) arg0; //cast view to a button
            // update the TextView text
            if (btn.isChecked()) {
                ((TextView) findViewById(R.id.tv4)).setText("en cours");
                Intent intent = new Intent(MainActivity.this, MainService.class);
                startService(intent);
            } else {
                ((TextView) findViewById(R.id.tv4)).setText("arrêté");
                Intent intent = new Intent(MainActivity.this, MainService.class);
                stopService(intent);
            }
        }
    }


    private class GetDataTask extends AsyncTask<Void, Void, List<Data>> {

        @Override
        protected List<Data> doInBackground(Void... params) {
            try {
                mService.getData(new DataCallback() {
                    @Override
                    public void onDataLoaded(List<Data> dataList) {
                        // Le reste du code à exécuter après la récupération des données
                        Log.d("MainActivity", "Lucie : data_list size = " + dataList.size());
                        List<String> highValueDataNames = new ArrayList<>();
                        for (Data data : dataList) {
                            Timestamp tmp = new Timestamp(System.currentTimeMillis());
                            if (data.getTimestamp() > tmp.getTime()) {
                                tmp.setTime(data.getTimestamp());
                            }
                            ((TextView) findViewById(R.id.tv5)).setText(tmp.toString());

                            Log.d("MainActivity", "Lucie : " + data.getValue());
                            if(data.getValue() > 200) {
                                //list of every mote that are on (value >  220)
                                highValueDataNames.add(data.getMote());
                            }
                        }
                        // Construire une chaîne avec les noms des données
                        StringBuilder resultStringBuilder = new StringBuilder();

                        for (String name : highValueDataNames) {
                            resultStringBuilder.append(name).append("\n");
                        }

                        // Afficher la chaîne dans un TextView (remplacez R.id.textView avec l'ID réel de votre TextView)
                        TextView resultTextView = findViewById(R.id.tv6);
                        resultTextView.setText(resultStringBuilder.toString());
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Gérer l'erreur ici
                        Log.e("MainActivity", "Lucie : " + errorMessage);
                    }
                });
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Data> data_list) {
            // Peut être vide, car le traitement est effectué dans le callback
        }
    }

    private class HandleClickButton implements View.OnClickListener {
        public void onClick(View arg0) {
            // Exécutez la tâche asynchrone pour récupérer les données
            new GetDataTask().execute();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, APIService.class);
        Log.d("MainActivity", "Lucie : bindService before");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d("MainActivity", "Lucie : bindService after");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "Lucie : Arrêt de l'activité");
        unbindService(connection);
        mBound = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to APIService, cast the IBinder and get APIService instance.
            Log.d("MainActivity", "Lucie : onServiceConnected");
            APIService.APIBinder binder = (APIService.APIBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
