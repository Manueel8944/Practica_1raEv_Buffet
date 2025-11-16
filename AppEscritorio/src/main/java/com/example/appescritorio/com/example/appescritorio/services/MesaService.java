package com.example.appescritorio.com.example.appescritorio.services;

import com.example.appescritorio.com.example.appescritorio.ApiClient;
import com.example.appescritorio.com.example.appescritorio.clases.Mesa;
import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MesaService {

    private static final OkHttpClient client = ApiClient.getClient();
    private static final Gson gson = new Gson();

    public static List<Mesa> getMesas() throws IOException {
        Request request = new Request.Builder()
                .url(ApiClient.getBaseUrl() + "/mesas")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Error al obtener mesas: " + response);
            }

            String json = response.body().string();

            Type listType = new TypeToken<List<Mesa>>() {}.getType();

            return gson.fromJson(json, listType);
        }
    }

    public static Mesa updateMesa(int id, Mesa mesa) throws IOException {

        String json = gson.toJson(mesa);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(ApiClient.getBaseUrl() + "/mesas/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Error al actualizar mesa: " + response);
            }

            return gson.fromJson(response.body().string(), Mesa.class);
        }
    }
}
