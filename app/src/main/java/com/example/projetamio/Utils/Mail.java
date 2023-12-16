package com.example.projetamio.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Mail extends AsyncTask<Void, Void, String>{
    private String server = "aspmx.l.google.com";
    private int port = 25;
    private String from = "nathan.cornelie@telecomnancy.net";
    private String TAG = "MAIL";
    private String to;
    private String subject;
    private String body;

    public Mail(String to, String subject, String body){
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    protected String doInBackground(Void... params) {
        try {
            Socket client = new Socket(server, port);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));

            writer.write("HELO amio.com\r\n");
            writer.flush();
            String line = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Log.d("Mail", line);

            writer.write("MAIL FROM: <" + from + ">\r\n");
            writer.flush();
            line = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Log.d("Mail", line);

            writer.write("RCPT TO: <" + to + ">\r\n");
            writer.flush();
            line = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Log.d("Mail", line);

            writer.write("DATA\r\n");
            writer.flush();
            line = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Log.d("Mail", line);

            writer.write("Subject: " + subject + "\r\n");
            writer.write("From: " + from + "\r\n");
            writer.write("To: " + to + "\r\n");
            writer.write("Message-ID: <" + System.currentTimeMillis() + "@amio.com>\r\n");
            writer.write("\r\n");
            writer.write(body + "\r\n");
            writer.write(".\r\n");
            writer.write("QUIT\r\n");

            writer.flush();

            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            Log.d("Mail", lines.toString());

            client.close();
        } catch (IOException e) {
            Log.e("Mail", "Error: " + e.getMessage());
        }

        return "";
    }
}

