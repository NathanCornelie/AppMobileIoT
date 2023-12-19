package com.example.projetamio.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetamio.R;
import com.example.projetamio.Services.APIService;
import com.example.projetamio.Services.MonitoringService;
import com.example.projetamio.Utils.DataBroadcastReceiver;
import com.example.projetamio.Utils.MyDataUpdateListener;
import com.example.projetamio.Utils.Notification;

public class MainActivity extends AppCompatActivity implements MyDataUpdateListener {

    private static final String TAG = "MainActivity";
    private static final String SHAREDPREFS = "MyPrefs";
    private static final String TOGGLE_STATE = "toggle_state";
    private static final String BEGIN_WEEK_SLIDER_STATE = "bws_state";
    private static final String END_WEEK_SLIDER_STATE = "ews_state";
    private static final String BEGIN_WEEKEND_SLIDER_STATE = "bwes_state";
    private static final String END_WEEKEND_SLIDER_STATE = "ewes_state";
    private static final String EDITTEXT_STATE = "edit_text_state";
    private static final String MONITORING_TOGGLE_STATE = "monitoring_toggle_state";
    public TextView tvCapteur1, tvCapteur2, tvCapteur3, tvCapteur4, tvValue1, tvValue2, tvValue3, tvValue4;
    public ImageView ivCapteur1, ivCapteur2, ivCapteur3, ivCapteur4;
    com.example.projetamio.Services.APIService mService;
    boolean mBound = false;
    SharedPreferences sharedPreferences;
    private ImageButton MonitoringButton;
    private boolean MonitoringButtonState = false;
    private DataBroadcastReceiver receiver;
    private ImageButton SettingsButton;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to APIService, cast the IBinder and get APIService instance.
            APIService.LocalBinder binder = (APIService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onDataUpdated(Intent intent) {
        Log.e(TAG, "broadcast data");
        for (int i = 0; i < 4; i++) {
            DisplayValue(i, intent.getStringExtra("id" + i), intent.getStringExtra("value" + i));
        }
    }

    @Override
    public void onError(String errorMessage) {

        Log.e(TAG, "broadcast error");
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


    private void DisplayValue(int i, String id, String value) {
        switch (i) {
            case 0:
                tvCapteur1.setText(id);
                tvValue1.setText(value);
                if (new Double(value) > 200)
                    ivCapteur1.setImageResource(R.drawable.light_on);
                else
                    ivCapteur1.setImageResource(R.drawable.light_off);
                break;
            case 1:
                tvCapteur2.setText(id);
                tvValue2.setText(value);
                if (Double.parseDouble(value) > 200)
                    ivCapteur2.setImageResource(R.drawable.light_on);
                else
                    ivCapteur2.setImageResource(R.drawable.light_off);
                break;
            case 2:
                tvCapteur3.setText(id);
                tvValue3.setText(value);
                if ( Double.parseDouble(value) > 200)
                    ivCapteur3.setImageResource(R.drawable.light_on);
                else
                    ivCapteur3.setImageResource(R.drawable.light_off);
                break;
            case 3:
                tvCapteur4.setText(id);
                tvValue4.setText(value);
                if (Double.parseDouble(value) > 200)
                    ivCapteur4.setImageResource(R.drawable.light_on);
                else
                    ivCapteur4.setImageResource(R.drawable.light_off);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mService = new APIService();

        // Crée le canal de notification
        Notification notification = new Notification(this);
        notification.createNotificationChannel();

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "default");
        if (name.equals("default")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", "Lucie");
            editor.apply();
        }

        MonitoringButton = findViewById(R.id.MonitoringButton);

        SettingsButton = findViewById(R.id.SettingsButton);
        tvCapteur2 = findViewById(R.id.tvCapteur2);
        tvCapteur1 = findViewById(R.id.tvCapteur1);
        tvCapteur3 = findViewById(R.id.tvCapteur3);
        tvCapteur4 = findViewById(R.id.tvCapteur4);
        tvValue1 = findViewById(R.id.tvValue1);
        tvValue2 = findViewById(R.id.tvValue2);
        tvValue3 = findViewById(R.id.tvValue3);
        tvValue4 = findViewById(R.id.tvValue4);
        ivCapteur1 = findViewById(R.id.imageView1);
        ivCapteur2 = findViewById(R.id.imageView2);
        ivCapteur3 = findViewById(R.id.imageView3);
        ivCapteur4 = findViewById(R.id.imageView4);

        SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, com.example.projetamio.activities.SettingsActivity.class);
                startActivity(intent);
            }
        });
        receiver = new DataBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter(DataBroadcastReceiver.ACTION_DATA_UPDATED);
        registerReceiver(receiver, intentFilter);


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Log.e(TAG, sharedPreferences.getString("email_sendto", "lol"));


        MonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonitoringService.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (MonitoringButtonState) {
                    editor.putBoolean(MONITORING_TOGGLE_STATE, false);
                    MonitoringButton.setBackgroundColor(Color.parseColor("#FFF44336"));
                    stopService(intent);
                    MonitoringButtonState = false;
                } else {
                    MonitoringButton.setBackgroundColor(Color.parseColor("#FF4CAF50"));

                    editor.putBoolean(MONITORING_TOGGLE_STATE, true);
                    intent.putExtra("debutSemaine", sharedPreferences.getInt(BEGIN_WEEK_SLIDER_STATE, 19));
                    intent.putExtra("finSemaine", sharedPreferences.getInt(END_WEEK_SLIDER_STATE, 23));
                    intent.putExtra("debutWeekend", sharedPreferences.getInt(BEGIN_WEEKEND_SLIDER_STATE, 23));
                    intent.putExtra("finWeekend", sharedPreferences.getInt(END_WEEKEND_SLIDER_STATE, 6));
                    intent.putExtra("email_sendto", sharedPreferences.getString("email_sendto", "default"));

                    startService(intent);
                    MonitoringButtonState = true;
                }
            }
        });


        findViewById(R.id.APIButton).setOnClickListener(new HandleClickImageBtn());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, com.example.projetamio.Services.APIService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private class HandleClickImageBtn implements View.OnClickListener {
        public void onClick(View arg0) {
            Intent intent = new Intent(MainActivity.this, com.example.projetamio.Services.APIService.class);
            Toast.makeText(MainActivity.this, "Waiting for data", Toast.LENGTH_SHORT).show();
            startService(intent);

        }
    }


}
