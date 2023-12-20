package com.example.projetamio.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.projetamio.Models.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GetDataTask extends AsyncTask<Void, Void, List<Data>> {
    com.example.projetamio.Services.APIService mService;
    Context context;

    public GetDataTask(com.example.projetamio.Services.APIService mService, Context conext) {
        this.mService = mService;
        this.context = conext;
    }

    @Override
    protected List<Data> doInBackground(Void... params) {
        try {
            mService.getData(new DataCallback() {
                @Override
                public void onDataLoaded(List<Data> dataList) {
                    // Le reste du code à exécuter après la récupération des données

                    Log.d("MainActivity", " : data_list size = " + dataList.size());

                    sendDataToActivity(dataList);

                    List<String> highValueDataNames = new ArrayList<>();

                    for (Data data : dataList) {
                        Timestamp tmp = new Timestamp(System.currentTimeMillis());
                        if (data.getTimestamp() > tmp.getTime()) {
                            tmp.setTime(data.getTimestamp());
                        }

                        Log.d("MainActivity", ": " + data.getLabel()+ " " + data.getValue() + " " );
                        if (data.getValue() > 200) {
                            //list of every mote that are on (value >  220)
                            highValueDataNames.add(data.getMote());
                        }
                    }
                    // Construire une chaîne avec les noms des données
                    StringBuilder resultStringBuilder = new StringBuilder();

                    for (String name : highValueDataNames) {
                        resultStringBuilder.append(name).append("\n");
                    }


                }

                @Override
                public void onError(String errorMessage) {
                    // Gérer l'erreur ici
                    Log.e("MainActivity", ": " + errorMessage);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
    private void sendDataToActivity(List<Data> dataList) {
        Intent intent = new Intent(DataBroadcastReceiver.ACTION_DATA_UPDATED);

        for(int i = 0; i < dataList.size(); i++){
            intent.putExtra("id"+i, dataList.get(i).getMote());
            intent.putExtra("value"+i, dataList.get(i).getValue().toString());
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String to = sharedPreferences.getString("email_sendto", "default");

        Log.e("TAG", "sendDataToActivity: " + to );
        context.sendBroadcast(intent);
    }
    @Override
    protected void onPostExecute(List<Data> data_list) {
        // Peut être vide, car le traitement est effectué dans le callback
    }
}
