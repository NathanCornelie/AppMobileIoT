package com.example.projetamio.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DataBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_DATA_UPDATED = "com.example.projetamio.DATA_UPDATED";
    public static final String ACTION_ERROR = "com.example.projetamio.ERROR";
    public static final String EXTRA_DATA = "com.example.projetamio.LOADING";
    private MyDataUpdateListener dataUpdateListener;

    public DataBroadcastReceiver(MyDataUpdateListener listener) {
        this.dataUpdateListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_DATA_UPDATED)) {
                if (dataUpdateListener != null) {
                    dataUpdateListener.onDataUpdated(intent);
                }
            }else if (intent.getAction().equals(ACTION_ERROR)) {
                if (dataUpdateListener != null) {
                    dataUpdateListener.onError(intent.getStringExtra("error"));
                }
            }
        }
    }
}