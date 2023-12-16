package com.example.projetamio.Utils;

import android.util.JsonReader;
import android.util.Log;

import com.example.projetamio.Models.CapteurData;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class JsonParser {
    private String TAG = "JSON PARSER";
    public List<CapteurData> parseJson(InputStream in) throws  IOException{

        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            return readDataArray(reader);
        } finally {
                reader.close();
        }
    }
    public List<CapteurData> readDataArray(JsonReader reader) throws IOException{
        List<CapteurData> dataArray = new ArrayList<CapteurData>();

            reader.beginObject();
            if(reader.hasNext() && reader.nextName().equals( "data")){
               reader.beginArray();
               while (reader.hasNext()){
                   dataArray.add(readData(reader));
               }
            }else Log.i(TAG, "readDataArray: PAS COOL ");

        return dataArray;
    }
    public CapteurData readData(JsonReader reader) throws  IOException{
        Long timestamp = 0L;
        String mote = "";
        String label = "";
        Double value = 0.0;
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("timestamp")) timestamp = reader.nextLong();
            else if (name.equals("value")) value = reader.nextDouble();
            else if (name.equals("mote")) mote = reader.nextString();
            else if (name.equals("label")) label = reader.nextString();
            else reader.skipValue();
        }

        reader.endObject();
        return  new CapteurData(timestamp,label,value,mote);
    }


}

