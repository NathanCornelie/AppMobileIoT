package com.example.projetamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    public MainService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
   public void onCreate(){
        super.onCreate();
        Log.d("MainActivity","nathan : Création de l'activité");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("TIMER","running");
            }

        };
        Timer timer  = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,60000);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("MAINSERVICE" , "nathan start service ");

        return START_STICKY;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("MAINSERVICE","destroyed");
        this.stopSelf();
    }
}