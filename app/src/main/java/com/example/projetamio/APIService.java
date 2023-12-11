package com.example.projetamio;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class APIService extends Service {

    private final IBinder binder = new APIBinder();
    private DataCallback dataCallback;
    private Context context;


    public class APIBinder extends Binder {
        public APIService getService() {
            return APIService.this;
        }
    }

    private String apiUrl = "http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last/";

    Context applicationContext;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        applicationContext = getApplicationContext();
        Log.d("APIService", "Lucie : APIService created");
    }

    public void getData(DataCallback callback) {
        this.dataCallback = callback;
        APIHandler apiHandler = new APIHandler(applicationContext, apiUrl);
        apiHandler.fetchData(new APIHandler.DataCallback() {
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

    private void displayToastError(int responseCode) {
        CharSequence toastMessage = "HTTPS Response Code " + responseCode;
        Toast toastError = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
        toastError.show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getData(dataCallback);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("APIService", "Lucie : destroyed");
        this.stopSelf();
    }
}
