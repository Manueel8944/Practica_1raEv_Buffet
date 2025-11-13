package com.example.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.clases.Mesa;
import com.example.app.conexion.ApiService;
import com.example.app.conexion.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button botonM1, botonM2, botonM3, botonM4, botonM5;

    Button botonRefrescar;

    ArrayList<Button> botonesArray = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        botonRefrescar = (Button) findViewById(R.id.botonRefrescar);

        botonM1 = (Button) findViewById(R.id.botonM1);
        botonM2 = (Button) findViewById(R.id.botonM2);
        botonM3 = (Button) findViewById(R.id.botonM3);
        botonM4 = (Button) findViewById(R.id.botonM4);
        botonM5 = (Button) findViewById(R.id.botonM5);

        botonesArray.add(botonM1);
        botonesArray.add(botonM2);
        botonesArray.add(botonM3);
        botonesArray.add(botonM4);
        botonesArray.add(botonM5);

        colorMesas();

        botonM1.setOnClickListener(v -> verMesaEscogida(0,botonM1));
        botonM2.setOnClickListener(v -> verMesaEscogida(1,botonM2));
        botonM3.setOnClickListener(v -> verMesaEscogida(2,botonM3));
        botonM4.setOnClickListener(v -> verMesaEscogida(3,botonM4));
        botonM5.setOnClickListener(v -> verMesaEscogida(4,botonM5));

        botonRefrescar.setOnClickListener(v -> colorMesas());
    }

    public void verMesaEscogida(int numMesa,Button botonCambiar) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Mesa>> call = apiService.getMesa();

        call.enqueue(new Callback<List<Mesa>>() {
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mesa> mesas = response.body();

                    Mesa mesaElegida = mesas.get(numMesa);
                    cambiarValorMesa(mesaElegida, numMesa+1, botonCambiar);

                } else {
                    Toast.makeText(MainActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cambiarValorMesa(Mesa mesaElegida, int idMesa, Button botonCambiar){
        if (mesaElegida.isEstadoMesa()){
            mesaElegida.setEstadoMesa(false);
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<Mesa> call = apiService.updateMesa(idMesa, mesaElegida);

            call.enqueue(new Callback<Mesa>() {
                @Override
                public void onResponse(Call<Mesa> call, Response<Mesa> response) {
                    if (response.isSuccessful()) {

                        botonCambiar.setBackgroundColor(Color.parseColor("#EF4D4D"));

                        Intent intent = new Intent(MainActivity.this, CartaActivity.class);
                        intent.putExtra("idMesa", idMesa);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this, "Error al actualizar mesa", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Mesa> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(MainActivity.this, "La mesa esta ocupada", Toast.LENGTH_SHORT).show();
        }

    }

    public void colorMesas(){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Mesa>> call = apiService.getMesa();

        call.enqueue(new Callback<List<Mesa>>() {
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mesa> mesas = response.body();

                    int contador = 0;
                    for (Mesa m: mesas){
                        if (!m.isEstadoMesa()){
                            botonesArray.get(contador).setBackgroundColor(Color.parseColor("#EF4D4D"));
                        }

                        contador++;
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}