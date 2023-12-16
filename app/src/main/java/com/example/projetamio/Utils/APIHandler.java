package com.example.projetamio.Utils;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;

import com.example.projetamio.Models.Data;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class APIHandler {

    public interface DataCallback {
        void onDataLoaded(List<Data> dataList);
        void onError(String errorMessage);
    }

    private String apiUrl;

    private Context context;

    public APIHandler( Context context,String apiUrl) {
        this.context = context;
        this.apiUrl = apiUrl;
    }

    public List<Data> fetchData(DataCallback callback) {
        AtomicReference<List<Data>> resp = new AtomicReference<>(new ArrayList<>());
        try {
            URL url = new URL(apiUrl);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executorService.execute(() -> {
                try {
                   resp.set(fetchDataFromUrl(url, handler, callback));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            Log.w("APIHandler", "Bad URL", e);
        }
        return resp.get();
    }
    public List<Data> data = new ArrayList<>();
    private List<Data> fetchDataFromUrl(URL url, Handler handler, DataCallback callback) throws IOException {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200) {
                List<Data> dataList = parseData(in);
                data = dataList;
                handler.post(() -> {
                    callback.onDataLoaded(dataList);
                });
            } else {
                handler.post(() -> callback.onError("HTTP Response Code " + responseCode));
            }
        } catch (IOException e) {
            handler.post(() -> callback.onError(e.getMessage()));
        }
        return data;
    }




    private List<Data> parseData(InputStream in) throws IOException {
        List<Data> dataList = new ArrayList<>();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        try {
            reader.beginObject();
            if (reader.hasNext() && reader.nextName().equals("data")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    dataList.add(readData(reader));

                }
                reader.endArray();
            }
            reader.endObject();
        } finally {
            reader.close();
        }

        return dataList;
    }

    private Data readData(JsonReader reader) throws IOException {
        Data data = new Data();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "timestamp":
                    data.setTimestamp(reader.nextLong());
                    break;
                case "label":
                    data.setLabel(reader.nextString());
                    break;
                case "value":
                    data.setValue(reader.nextDouble());
                    break;
                case "mote":
                    data.setMote(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return data;
    }
}
