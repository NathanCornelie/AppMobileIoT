package com.example.projetamio.Services;

import android.app.Service;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.projetamio.Models.CapteurData;
import com.example.projetamio.Utils.JsonParser;
import com.example.projetamio.Utils.Mail;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailService extends Service {
    private static Context context;
    public EmailService() {

    }
    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
        Log.d("EmailService","nathan : Création de l'activité");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("EmailService" , "nathan start service ");
        //sendEmail("cornelietutoun@gmail.com","test","test");
        Mail mail = new Mail("nathan.cornelie@telecomnancy.net","test","hello");
        mail.execute();

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void sendEmail(String recipient, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Vérifiez si l'appareil dispose d'une application capable de gérer l'intention
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            Toast.makeText(context, "Ouverture de l'application de messagerie.", Toast.LENGTH_SHORT).show();

        } else {
            // Gérez le cas où aucune application capable de gérer l'intention n'est installée sur l'appareil
            Toast.makeText(context, "Aucune application de messagerie n'est installée.", Toast.LENGTH_SHORT).show();
        }
    }

}