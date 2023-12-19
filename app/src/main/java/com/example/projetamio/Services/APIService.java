package com.example.projetamio.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.projetamio.Models.Data;
import com.example.projetamio.Utils.APIHandler;
import com.example.projetamio.Utils.DataCallback;
import com.example.projetamio.Utils.GetDataTask;

import java.util.ArrayList;
import java.util.List;

public class APIService extends Service {
    private final IBinder binder = new LocalBinder();
    public List<Data> capteurData = new ArrayList<>();
    Context applicationContext;
    private DataCallback dataCallback;
    private String apiUrl = "http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last/";
    private Context context;
    private String TAG = "API SERVICE";
    private BroadcastReceiver broadcastReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("context"))
            applicationContext = (Context) intent.getParcelableExtra("context");
        new GetDataTask(this, applicationContext).execute();
        this.onDestroy();
        Log.e(TAG, "onStartCommand: Service started");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        applicationContext = getApplicationContext();
        Log.d(TAG, " APIService created");

    }

    public void getData(DataCallback callback) {
        this.dataCallback = callback;
        APIHandler apiHandler = new APIHandler(applicationContext, apiUrl);
        capteurData = apiHandler.fetchData(new APIHandler.DataCallback() {
            @Override
            public void onDataLoaded(List<Data> dataList) {
                dataCallback.onDataLoaded(dataList);
            }

            @Override
            public void onError(String errorMessage) {
                dataCallback.onError(errorMessage);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " destroyed");
        this.stopSelf();
    }

    public class LocalBinder extends Binder {
        public APIService getService() {
            // Return this instance of LocalService so clients can call public methods
            return APIService.this;
        }
    }


}