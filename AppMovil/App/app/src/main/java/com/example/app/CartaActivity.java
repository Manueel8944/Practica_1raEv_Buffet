package com.example.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.clases.ComidaPedido;
import com.example.app.clases.Mesa;
import com.example.app.clases.Pedido;
import com.example.app.conexion.ApiService;
import com.example.app.conexion.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartaActivity extends AppCompatActivity {

    Button botonRefrescar;
    Button botonPagar, botonPedir;
    Button botonMas1, botonMas2,botonMas3,botonMas4,botonMas5,botonMas6;
    Button botonMenos1, botonMenos2,botonMenos3,botonMenos4,botonMenos5,botonMenos6;
    TextView contadorComida1,contadorComida2,contadorComida3,contadorComida4,contadorComida5,contadorComida6;

    int idMesa;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.carta), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idMesa = getIntent().getIntExtra("idMesa", -1);

        botonRefrescar = (Button) findViewById(R.id.botonRefrescar);

        botonPagar = (Button) findViewById(R.id.botonPagar);
        botonPedir = (Button) findViewById(R.id.botonPedir);

        botonMas1 = (Button) findViewById(R.id.botonMas1);
        botonMas2 = (Button) findViewById(R.id.botonMas2);
        botonMas3 = (Button) findViewById(R.id.botonMas3);
        botonMas4 = (Button) findViewById(R.id.botonMas4);
        botonMas5 = (Button) findViewById(R.id.botonMas5);
        botonMas6 = (Button) findViewById(R.id.botonMas6);

        botonMenos1 = (Button) findViewById(R.id.botonMenos1);
        botonMenos2 = (Button) findViewById(R.id.botonMenos2);
        botonMenos3 = (Button) findViewById(R.id.botonMenos3);
        botonMenos4 = (Button) findViewById(R.id.botonMenos4);
        botonMenos5 = (Button) findViewById(R.id.botonMenos5);
        botonMenos6 = (Button) findViewById(R.id.botonMenos6);

        contadorComida1 = (TextView) findViewById(R.id.contadorComida1);
        contadorComida2 = (TextView) findViewById(R.id.contadorComida2);
        contadorComida3 = (TextView) findViewById(R.id.contadorComida3);
        contadorComida4 = (TextView) findViewById(R.id.contadorComida4);
        contadorComida5 = (TextView) findViewById(R.id.contadorComida5);
        contadorComida6 = (TextView) findViewById(R.id.contadorComida6);

        cambiarColorPedir();

        botonMas1.setOnClickListener(v -> masComida(botonMas1,contadorComida1));
        botonMas2.setOnClickListener(v -> masComida(botonMas2,contadorComida2));
        botonMas3.setOnClickListener(v -> masComida(botonMas3,contadorComida3));
        botonMas4.setOnClickListener(v -> masComida(botonMas4,contadorComida4));
        botonMas5.setOnClickListener(v -> masComida(botonMas5,contadorComida5));
        botonMas6.setOnClickListener(v -> masComida(botonMas6,contadorComida6));

        botonMenos1.setOnClickListener(v -> menosComida(botonMenos1, contadorComida1));
        botonMenos2.setOnClickListener(v -> menosComida(botonMenos2, contadorComida2));
        botonMenos3.setOnClickListener(v -> menosComida(botonMenos3, contadorComida3));
        botonMenos4.setOnClickListener(v -> menosComida(botonMenos4, contadorComida4));
        botonMenos5.setOnClickListener(v -> menosComida(botonMenos5, contadorComida5));
        botonMenos6.setOnClickListener(v -> menosComida(botonMenos6, contadorComida6));

        botonPedir.setOnClickListener(v -> verMesaElegida());
        botonPagar.setOnClickListener(v -> irACuenta());

        botonRefrescar.setOnClickListener(v -> cambiarColorPedir());

    }

    public void masComida(Button botonMas, TextView contadorComida){
        int  textoInicial = Integer.parseInt(contadorComida.getText().toString());
        textoInicial++;
        contadorComida.setText(String.valueOf(textoInicial));
    }

    public void menosComida(Button boton, TextView contadorComida){
        int  textoInicial = Integer.parseInt(contadorComida.getText().toString());

        if (textoInicial>0){
            textoInicial--;
            contadorComida.setText(String.valueOf(textoInicial));
        }
    }

    public void pedirComida(){
        int  cantidadComida1 = Integer.parseInt(contadorComida1.getText().toString());
        int  cantidadComida2 = Integer.parseInt(contadorComida2.getText().toString());
        int  cantidadComida3 = Integer.parseInt(contadorComida3.getText().toString());
        int  cantidadComida4 = Integer.parseInt(contadorComida4.getText().toString());
        int  cantidadComida5 = Integer.parseInt(contadorComida5.getText().toString());
        int  cantidadComida6 = Integer.parseInt(contadorComida6.getText().toString());

        ComidaPedido comidaPedido1 = new ComidaPedido(1, cantidadComida1);
        ComidaPedido comidaPedido2 = new ComidaPedido(2, cantidadComida2);
        ComidaPedido comidaPedido3 = new ComidaPedido(3, cantidadComida3);
        ComidaPedido comidaPedido4 = new ComidaPedido(4, cantidadComida4);
        ComidaPedido comidaPedido5 = new ComidaPedido(5, cantidadComida5);
        ComidaPedido comidaPedido6 = new ComidaPedido(6, cantidadComida6);

        ArrayList<ComidaPedido> comidaPedidosArray = new ArrayList<>();

        comidaPedidosArray.add(comidaPedido1);
        comidaPedidosArray.add(comidaPedido2);
        comidaPedidosArray.add(comidaPedido3);
        comidaPedidosArray.add(comidaPedido4);
        comidaPedidosArray.add(comidaPedido5);
        comidaPedidosArray.add(comidaPedido6);

        comidaPedidosArray.removeIf(cp -> cp.getCantidad() == 0);

        Pedido pedido = new Pedido(idMesa,comidaPedidosArray);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Pedido> call = apiService.createPedido(pedido);

        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if(response.isSuccessful()) {
                    contadorComida1.setText("0");
                    contadorComida2.setText("0");
                    contadorComida3.setText("0");
                    contadorComida4.setText("0");
                    contadorComida5.setText("0");
                    contadorComida6.setText("0");

                    Toast.makeText(CartaActivity.this, "Pedido realizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartaActivity.this, "Error al pedir", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(CartaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void verMesaElegida(){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Mesa>> call = apiService.getMesa();

        call.enqueue(new Callback<List<Mesa>>() {
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mesa> mesas = response.body();
                    Mesa mesaElegida = mesas.get(idMesa-1);
                    cambiarValorPedido(mesaElegida);

                } else {
                    Toast.makeText(CartaActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Toast.makeText(CartaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cambiarValorPedido(Mesa mesaElegida){
        if (!mesaElegida.isEstadoPedido()){
            mesaElegida.setEstadoPedido(true);
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<Mesa> call = apiService.updateMesa(idMesa, mesaElegida);

            call.enqueue(new Callback<Mesa>() {
                @Override
                public void onResponse(Call<Mesa> call, Response<Mesa> response) {
                    if (response.isSuccessful()) {

                        botonPedir.setBackgroundColor(Color.parseColor("#B5E0FF"));
                        pedirComida();
                    } else {
                        Toast.makeText(CartaActivity.this, "Error al actualizar mesa", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Mesa> call, Throwable t) {
                    Toast.makeText(CartaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(CartaActivity.this, "Aun no le sirvieron su ultimo pedido", Toast.LENGTH_SHORT).show();
        }
    }

    public void cambiarColorPedir(){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Mesa>> call = apiService.getMesa();

        call.enqueue(new Callback<List<Mesa>>() {
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mesa> mesas = response.body();
                    Mesa mesaElegida = mesas.get(idMesa-1);

                        if (mesaElegida.isEstadoMesa()){
                            botonPedir.setBackgroundColor(Color.parseColor("#B5E0FF"));
                        }

                        else {
                            botonPedir.setBackgroundColor(Color.parseColor("#42ABF6"));
                        }

                } else {
                    Toast.makeText(CartaActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Toast.makeText(CartaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void irACuenta(){

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Mesa>> call = apiService.getMesa();

        call.enqueue(new Callback<List<Mesa>>() {
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mesa> mesas = response.body();
                    Mesa mesaElegida = mesas.get(idMesa-1);
                    if (!mesaElegida.isEstadoPedido()){
                        Intent intent = new Intent(CartaActivity.this, CuentaActivity.class);
                        intent.putExtra("idMesa", idMesa);
                        startActivity(intent);
                        finish(); // importante para que se refresquen las paginas al volver a entrar
                    }

                    else {
                        Toast.makeText(CartaActivity.this, "Aun hay un pedido sin servir", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CartaActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Toast.makeText(CartaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
