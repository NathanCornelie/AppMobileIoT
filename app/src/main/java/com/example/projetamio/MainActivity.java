package com.example.projetamio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button OnButton;
    private Button OffButton;
    private TextView TV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentService = new Intent(this,MainService.class);
       this.initButtons(intentService);
    }

    private void initButtons(Intent intent) {
        this.OnButton = (Button) findViewById(R.id.btn1);
        this.OffButton = (Button) findViewById(R.id.btn2);
        this.TV2 = (TextView) findViewById(R.id.TV2);
        initOnButton(intent);
        initOffButton(intent);

    }

    private void initOnButton(Intent intent) {
        OnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             TV2.setText("Running");
                OnButton.setBackgroundColor(Color.CYAN);
                OffButton.setBackgroundColor(Color.rgb(20,20,20));
                startService(intent);
            }
        });
    }
    private void initOffButton(Intent intent){
        OffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             TV2.setText("Stopped");
                OffButton.setBackgroundColor(Color.RED);
                OnButton.setBackgroundColor(Color.rgb(20,20,20));
                stopService(intent);
            }
        });
    }
}