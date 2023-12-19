package com.example.projetamio.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.projetamio.Models.Data;
import com.example.projetamio.R;
import com.example.projetamio.Utils.DataBroadcastReceiver;
import com.example.projetamio.Utils.DataCallback;
import com.example.projetamio.Utils.Mail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MonitoringService extends Service {
    private static final String CHANNEL_ID = "my_notification_channel";
    private static int debutSemaine, finSemaine, debutWeekend, finWeekend;
    private String email_sendto;
    private boolean isSendMailOn = true;



    Timer timer;
    com.example.projetamio.Services.APIService apiService;
    Context applicationContext;

    public MonitoringService() {

    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MonitoringService", " : Création du service");
        // Create an instance of APIService
        applicationContext = getApplicationContext();

        // Appelez APIService en passant le contexte
        com.example.projetamio.Services.APIService apiService = new APIService();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("MonitoringService", " : TimerTask");

                // Call the getData method of APIService every minute
                apiService.getData(new DataCallback() {

                    @Override
                    public void onDataLoaded(List<Data> dataList) {
                        // Handle loaded data
                        // You can perform any actions here based on the loaded data
                        sendDataToActivity(dataList);
                        for (Data data : dataList) {
                            updateJsonData(dataList);
                            //check here if the light turn on
                            //get the JSON
                            SharedPreferences pref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            String currentJsonString = pref.getString("your_json_key", "[]");
                            List<Data> currentDataList = parseDataListFromJson(currentJsonString);
                            List<Data> filteredData = getFilteredDataOnMote(currentDataList, data.getMote());
                            Data latestData = filteredData.get(0);

                            double lastValue = latestData.getValue();
                            double PreviousValue = filteredData.get(1).getValue();

                            if (lastValue > 200 && PreviousValue < 200) {
                                //notification "lumière allumée"
                                sendNotification(latestData.getLabel() + " s'est allumé");
                                if(isSendMailOn && email_sendto != null){
                                  sendMail();
                                }

                            }
                            if (lastValue < 200 && PreviousValue > 200) {
                                //notification "lumière éteinte"
                                sendNotification(latestData.getLabel() + " s'est éteinte");

                            }
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                        // You can perform any error handling here
                        sendErrorToActivity(errorMessage);
                        Log.d("MonitoringService", " Erreur de chargement des données");
                    }
                });
            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(timerTask, 0, 10000);


    }
    private void sendDataToActivity(List<Data> dataList) {
        Intent intent = new Intent(DataBroadcastReceiver.ACTION_DATA_UPDATED);

        for(int i = 0; i < dataList.size(); i++){
            intent.putExtra("id"+i, dataList.get(i).getMote());
            intent.putExtra("value"+i, dataList.get(i).getValue().toString());
        }
        applicationContext.sendBroadcast(intent);
    }
    private void sendErrorToActivity(String errorMessage) {
        Log.e("", "sendErrorToActivity: nathan tes le best" );
        Intent intent = new Intent(DataBroadcastReceiver.ACTION_ERROR);
        intent.putExtra("error", errorMessage);
        applicationContext.sendBroadcast(intent);
    }
    public static List<Data> getFilteredDataOnMote(List<Data> dataList, String mote) {
        if (dataList == null || dataList.isEmpty()) {
            // Retournez null si la liste est vide ou nulle
            return null;
        }

        List<Data> filteredList = new ArrayList<>();

        // Filtrez la liste pour n'inclure que les Data avec le label spécifié
        for (Data data : dataList) {
            if (mote.equals(data.getMote())) {
                filteredList.add(data);
            }
        }

        if (filteredList.isEmpty()) {
            // Retournez null si aucune Data avec le label spécifié n'est trouvée
            return null;
        }

        // Triez la liste filtrée en fonction des timestamps dans l'ordre décroissant
        Collections.sort(filteredList, (data1, data2) -> Long.compare(data2.getTimestamp(), data1.getTimestamp()));

        // La première Data après le tri est celle avec le timestamp le plus récent
        return filteredList;
    }
    private void sendMail(){
        if(isWeekend()){
            if(debutWeekend < finWeekend){
                if(debutWeekend <= java.time.LocalTime.now().getHour() && java.time.LocalTime.now().getHour() <= finWeekend){
                  new Mail(email_sendto,"Lumière allumée","La lumière est allumée").execute();
                }
            }else{
                if(debutWeekend <= java.time.LocalTime.now().getHour() || java.time.LocalTime.now().getHour() <= finWeekend){
                    new Mail(email_sendto,"Lumière allumée","La lumière est allumée").execute();
                }
            }
        }else{
            if(debutSemaine < finSemaine){
                if(debutSemaine <= java.time.LocalTime.now().getHour() && java.time.LocalTime.now().getHour() <= finSemaine){
                    new Mail(email_sendto,"Lumière allumée","La lumière est allumée").execute();
                }
            }else{
                if(debutSemaine <= java.time.LocalTime.now().getHour() || java.time.LocalTime.now().getHour() <= finSemaine){
                    new Mail(email_sendto,"Lumière allumée","La lumière est allumée").execute();
                }
            }

        }
    }
    private boolean isWeekend(){
        java.util.Calendar c = java.util.Calendar.getInstance();
        int dayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
        return (dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY);
    }

    private void updateJsonData(List<Data> dataList) {
        SharedPreferences pref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String currentJsonString = pref.getString("your_json_key", "[]");

        List<Data> currentDataList = parseDataListFromJson(currentJsonString);
        currentDataList.addAll(dataList);

        String updatedJsonString = convertDataListToJson(currentDataList);
        // Affiche le JSON mis à jour
        Log.d("APIHandler", "JSON mis à jour : " + updatedJsonString);

        // Enregistrez la chaîne JSON mise à jour dans les SharedPreferences
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("your_json_key", updatedJsonString);
        editor.apply();
    }

    private List<Data> parseDataListFromJson(String jsonString) {
        List<Data> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);

                Data data = new Data();
                data.setTimestamp(jsonItem.getLong("timestamp"));
                data.setLabel(jsonItem.getString("label"));
                data.setValue(jsonItem.getDouble("value"));
                data.setMote(jsonItem.getString("mote"));

                dataList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private String convertDataListToJson(List<Data> dataList) {
        JSONArray jsonArray = new JSONArray();

        for (Data data : dataList) {
            JSONObject jsonItem = new JSONObject();

            try {
                jsonItem.put("timestamp", data.getTimestamp());
                jsonItem.put("label", data.getLabel());
                jsonItem.put("value", data.getValue());
                jsonItem.put("mote", data.getMote());

                jsonArray.put(jsonItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArray.toString();
    }

    private void sendNotification(String message) {
        Log.d("MonitoringService", " Création de la notification");
        // Création du gestionnaire de notifications
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Création de la notification avec le style fourni
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("LUMIERE Allumee")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Envoi de la notification
        Log.d("MonitoringService", "Envoi de la notification");
        notificationManager.notify(1, builder.build());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        debutSemaine = intent.getIntExtra("debutSemaine", 19);
        finSemaine = intent.getIntExtra("finSemaine", 23);
        debutWeekend = intent.getIntExtra("debutWeekend", 23);
        finWeekend = intent.getIntExtra("finWeekend", 6);
        email_sendto = intent.getStringExtra("email_sendto");
        isSendMailOn = intent.getBooleanExtra("toggle_sendmail", true);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.timer.cancel();
        Log.d("MonitoringService", ": Arrêt du service");
    }
}