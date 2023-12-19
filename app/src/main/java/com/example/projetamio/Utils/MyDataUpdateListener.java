package com.example.projetamio.Utils;

import android.content.Intent;

public interface MyDataUpdateListener {
    void onDataUpdated(Intent intent);
    void onError(String errorMessage);
}
