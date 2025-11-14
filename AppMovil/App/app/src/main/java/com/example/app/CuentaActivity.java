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
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> pedidos = response.body();
                    List<Pedido> pedidosMesa = new ArrayList<>();

                    for (Pedido p : pedidos) {
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

        List<ComidaCuenta> carta = new ArrayList<>();
        carta.add(new ComidaCuenta(1, "Pulpo", 18.5, 0));
        carta.add(new ComidaCuenta(2, "Filete", 22, 0));
        carta.add(new ComidaCuenta(3, "Langostinos", 16.75, 0));
        carta.add(new ComidaCuenta(4, "Sopa", 9.9, 0));
        carta.add(new ComidaCuenta(5, "Agua", 2.0, 0));
        carta.add(new ComidaCuenta(6, "Cerveza", 4.5, 0));

        List<ComidaCuenta> comidasConsolidadas = new ArrayList<>();

        for (Pedido pedido : pedidosMesa) {
            for (ComidaPedido cp : pedido.getComidas()) {
                for (ComidaCuenta cc : carta) {
                    if (cp.getIdComida() == cc.getIdComida()) {

                        boolean encontrada = false;

                        for (ComidaCuenta cCons : comidasConsolidadas) {
                            if (cCons.getIdComida() == cc.getIdComida()) {

                                cCons.setCantidad(cCons.getCantidad() + cp.getCantidad());
                                encontrada = true;
                                break;
                            }
                        }

                        if (!encontrada) {
                            comidasConsolidadas.add(new ComidaCuenta(cc.getIdComida(), cc.getNombre(), cc.getPrecio(), cp.getCantidad()));
                        }
                    }
                }
            }
        }

        double total = 0;

        for (ComidaCuenta cp : comidasConsolidadas) {
            TextView tv = new TextView(this);
            tv.setText(cp.getCantidad() + " x " + cp.getNombre() + " - " + (cp.getPrecio() * cp.getCantidad()) + "€");
            tv.setTextSize(18);
            tv.setPadding(10, 20, 10, 20);
            tv.setTypeface(Typeface.MONOSPACE);

            linearLayoutPedidos.addView(tv);

            total += cp.getPrecio() * cp.getCantidad();
        }

        TextView textoTotal = findViewById(R.id.textoTotal);
        textoTotal.setText(String.format(String.valueOf(total)));
    }

    public void pagar() {
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

    public void cambiarValorMesa(Mesa mesaElegida) {
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

    public void verPedidos(){
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

        for (Pedido p: listaPedidos){

            if(idMesa == p.getIdMesa().intValue()){ // Cuidado que no se puede comparar number con Int entonces hay que transformarlo
                Call<Void> call = apiService.deletePedido((Integer) p.get_Id());

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
    }

    public void irMain(){
        Intent intent = new Intent(CuentaActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // importante para que se refresquen las paginas al volver a entrar
    }



}

