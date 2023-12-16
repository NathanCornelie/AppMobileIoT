package com.example.projetamio.Utils;

import android.content.Context;
import android.content.Intent;
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

                    Log.d("MainActivity", "Lucie : data_list size = " + dataList.size());

                    Intent intent = new Intent("data");
                    intent.putExtra("label1", dataList.get(0).getLabel());
                    intent.putExtra("value1", dataList.get(0).getValue().toString());
                    intent.putExtra("label2", dataList.get(1).getLabel());
                    intent.putExtra("value2", dataList.get(1).getValue().toString());
                    mService.sendBroadcast(intent);

                    List<String> highValueDataNames = new ArrayList<>();

                    for (Data data : dataList) {
                        Timestamp tmp = new Timestamp(System.currentTimeMillis());
                        if (data.getTimestamp() > tmp.getTime()) {
                            tmp.setTime(data.getTimestamp());
                        }

                        Log.d("MainActivity", "Lucie : " + data.getLabel()+ " " + data.getValue() + " " );
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

                    // Afficher la chaîne dans un TextView (remplacez R.id.textView avec l'ID réel de votre TextView)
                    /*TextView resultTextView = findViewById(R.id.tv6);
                    resultTextView.setText(resultStringBuilder.toString());*/
                }

                @Override
                public void onError(String errorMessage) {
                    // Gérer l'erreur ici
                    Log.e("MainActivity", "Lucie : " + errorMessage);
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Data> data_list) {
        // Peut être vide, car le traitement est effectué dans le callback
    }
}
