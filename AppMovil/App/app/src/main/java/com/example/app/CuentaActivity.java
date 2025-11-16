package com.example.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.clases.ComidaCuenta;
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

public class CuentaActivity extends AppCompatActivity {

    int idMesa;

    Button botonConfirmar;
    TextView textoTotal;
    LinearLayout linearLayoutPedidos;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuenta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cuenta), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idMesa = getIntent().getIntExtra("idMesa", -1);

        botonConfirmar = (Button) findViewById(R.id.botonConfirmar);
        textoTotal = (TextView) findViewById(R.id.textoTotal);
        linearLayoutPedidos = (LinearLayout) findViewById(R.id.linearLayoutPedidos);

        listaPedidos();

        botonConfirmar.setOnClickListener(v -> pagar());


    }

    public void listaPedidos() {

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Pedido>> call = apiService.getPedido();

        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) { // Esta funcion es para ver cuales son los pedidos de la mesa elegida especificamente
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> pedidos = response.body();
                    List<Pedido> pedidosMesa = new ArrayList<>();

                    for (Pedido p : pedidos) { // Hay que ver todos los pedidos que haya y meter en un array los pedidos de la mesa elegida
                        if (p.getIdMesa().intValue() == idMesa) {
                            pedidosMesa.add(p);
                        }
                    }

                    mostrarPedidos(pedidosMesa);


                } else {
                    Toast.makeText(CuentaActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(CuentaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void mostrarPedidos(List<Pedido> pedidosMesa) {
        linearLayoutPedidos.removeAllViews();

        List<ComidaCuenta> carta = new ArrayList<>(); // Aqui lo que hice es una nueva clase para poner las comidas y registrar las cantidades pedidas de cada comida
        carta.add(new ComidaCuenta(1, "Pulpo", 18.5, 0));
        carta.add(new ComidaCuenta(2, "Filete", 22, 0));
        carta.add(new ComidaCuenta(3, "Langostinos", 16.75, 0));
        carta.add(new ComidaCuenta(4, "Sopa", 9.9, 0));
        carta.add(new ComidaCuenta(5, "Agua", 2.0, 0));
        carta.add(new ComidaCuenta(6, "Cerveza", 4.5, 0));

        List<ComidaCuenta> comidasConsolidadas = new ArrayList<>(); // Array para meter las comidas que se pidan y sus cantidades

        for (Pedido pedido : pedidosMesa) { // Recorri todos los pedidos de la mesa, todas las comidas de cada pedido, y recorrer toda la carta
            for (ComidaPedido cp : pedido.getComidas()) {
                for (ComidaCuenta cc : carta) {
                    if (cp.getIdComida() == cc.getIdComida()) { // vemos cuando el id comida de la ComidaPedido sea igual al id de la comida de la carta

                        boolean encontrada = false; // Variable para saber si encontramos la comida ya en el array de las comidas para mostrar en la factura

                        for (ComidaCuenta cCons : comidasConsolidadas) { // Recorremos las comidas que se van a plasmar en la factura y si ya esta se añade la cantidad pedida a la cantidad de la comida en la factura
                            if (cCons.getIdComida() == cc.getIdComida()) {

                                cCons.setCantidad(cCons.getCantidad() + cp.getCantidad());
                                encontrada = true;
                                break;
                            }
                        }

                        if (!encontrada) { // Si no se crea la comida con su cantidad
                            comidasConsolidadas.add(new ComidaCuenta(cc.getIdComida(), cc.getNombre(), cc.getPrecio(), cp.getCantidad()));
                        }
                    }
                }
            }
        }

        double total = 0; // Es lo que va a costar en total la cuenta

        for (ComidaCuenta cp : comidasConsolidadas) { // Se recorre el array de las comidas que se van a ver en la factura y se crean textViews con la cantidad nombre precio etc..
            TextView tv = new TextView(this);
            tv.setText(cp.getCantidad() + " x " + cp.getNombre() + " - " + (cp.getPrecio() * cp.getCantidad()) + "€");
            tv.setTextSize(18);
            tv.setPadding(10, 20, 10, 20);
            tv.setTypeface(Typeface.MONOSPACE);

            linearLayoutPedidos.addView(tv);

            total += cp.getPrecio() * cp.getCantidad(); // Se añade al total
        }

        textoTotal.setText(String.format(String.valueOf(total))); // Se pone el total, hay que transformarlo a String si no no se puede poner
    }

    public void pagar() { // Aqui al darle a confirmar el pago coje la mesa correspondiente y la manda como parametro a otra funcion
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Mesa>> call = apiService.getMesa();

        call.enqueue(new Callback<List<Mesa>>() {
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mesa> mesas = response.body();

                    Mesa mesaElegida = mesas.get(idMesa - 1);

                    cambiarValorMesa(mesaElegida);

                } else {
                    Toast.makeText(CuentaActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Toast.makeText(CuentaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cambiarValorMesa(Mesa mesaElegida) { // Se cambia el valor de la mesa a true para liberarla y se inicia otra funcion
        mesaElegida.setEstadoMesa(true);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Mesa> call = apiService.updateMesa(idMesa, mesaElegida);

        call.enqueue(new Callback<Mesa>() {
            @Override
            public void onResponse(Call<Mesa> call, Response<Mesa> response) {
                if (response.isSuccessful()) {

                    verPedidos();

                } else {
                    Toast.makeText(CuentaActivity.this, "Error al actualizar mesa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mesa> call, Throwable t) {
                Toast.makeText(CuentaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void verPedidos(){ // Funcion para ver todos los pedidos y mandarlos a otra funcion
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Pedido>> call = apiService.getPedido();

        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> pedidos = response.body();

                    borrarPedidos((ArrayList<Pedido>) pedidos);

                } else {
                    Toast.makeText(CuentaActivity.this, "Error al encontrar la mesa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(CuentaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void borrarPedidos(ArrayList<Pedido> listaPedidos){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        for (Pedido p: listaPedidos){ // Recorremos todos los pedidos

            if(idMesa == p.getIdMesa().intValue()){ // Vemos que el pedido sea de la mesa correspondiente, Cuidado que no se puede comparar number con Int entonces hay que transformarlo

                Call<Void> call = apiService.deletePedido(p.get_id()); // Se borran los pedidos de la mesa usada

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {

                        } else {
                            Toast.makeText(CuentaActivity.this, "Error eliminando pedido", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CuentaActivity.this, "Error de conexión" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        Intent intent = new Intent(CuentaActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // importante para que se refresquen las paginas al volver a entrar
    }
}

