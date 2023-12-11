package com.example.projetamio;

import java.util.List;

public interface DataCallback {
    void onDataLoaded(List<Data> dataList);
    void onError(String errorMessage);
}
