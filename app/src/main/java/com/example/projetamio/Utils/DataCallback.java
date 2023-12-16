package com.example.projetamio.Utils;

import com.example.projetamio.Models.Data;

import java.util.List;

public interface DataCallback {
    void onDataLoaded(List<Data> dataList);
    void onError(String errorMessage);
}
