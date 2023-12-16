package com.example.projetamio.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.projetamio.Services.MainService;
import com.example.projetamio.Services.MonitoringService;

public class MyBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBootBroadcastReceiver", "lucie : Démarrage du service au démarrage du téléphone");
        if (context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getBoolean("checkbox_state", false)) {
            Intent serviceIntent = new Intent(context, MainService.class);
            context.startService(serviceIntent);
        }
        if (context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getBoolean("toggle_state", false)) {
            Intent serviceIntent = new Intent(context, MonitoringService.class);
            context.startService(serviceIntent);
        }
    }
}
