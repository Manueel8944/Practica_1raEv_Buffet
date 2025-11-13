package com.example.app;

import android.annotation.SuppressLint;
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
    }

    public void listaPedidos(){

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
        LinearLayout layoutPedidos = findViewById(R.id.linearLayoutPedidos);
        layoutPedidos.removeAllViews(); // limpiar antes de añadir

        List<ComidaPedido> comidasConsolidadas = new ArrayList<>();

        for (Pedido pedido : pedidosMesa) {
            for (ComidaPedido cp : pedido.getComidas()) {
                boolean encontrada = false;

                for (ComidaPedido c : comidasConsolidadas) {
                    if (c.getIdComida() == cp.getIdComida()) {
                        c.setCantidad(c.getCantidad() + cp.getCantidad());
                        encontrada = true;
                        break;
                    }
                }

                if (!encontrada) {
                    comidasConsolidadas.add(new ComidaPedido(cp.getIdComida(), cp.getCantidad()));
                }
            }
        }

        // Mostrar en el LinearLayout
        for (ComidaPedido cp : comidasConsolidadas) {
            TextView tv = new TextView(this);
            tv.setText("Producto #" + cp.getIdComida() + " - Cantidad: " + cp.getCantidad());
            tv.setTextSize(18);
            layoutPedidos.addView(tv);
        }
    }
}
