package com.example.appescritorio.com.example.appescritorio;

import okhttp3.*;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:3000";

    private static final OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
