package com.example.projetamio.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    Timer timer;
    public MainService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MainService", "Lucie : Création du service");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("MainService", "Lucie : TimerTask");
            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(timerTask, 0, 1000);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MainService", "Lucie : Démarrage du service");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.timer.cancel();
        Log.d("MainService", "Lucie : Arrêt du service");
    }
}