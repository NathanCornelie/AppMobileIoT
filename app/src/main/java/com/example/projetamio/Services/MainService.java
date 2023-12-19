package com.example.projetamio.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;

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
        Log.d("MainService", " Création du service");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MainService", "Démarrage du service");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.timer.cancel();
        Log.d("MainService", " Arrêt du service");
    }
}