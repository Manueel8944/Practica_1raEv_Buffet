package com.example.app.conexion;

import com.example.app.clases.Comida;
import com.example.app.clases.Mesa;
import com.example.app.clases.Pedido;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("pedidos")
    Call<List<Pedido>> getPedido();

    @POST("pedidos")
    Call<Pedido> createPedido(@Body Pedido pedido);

    @PUT("pedidos/{id}")
    Call<Pedido> updatePedido(@Path("id") String id, @Body Pedido pedido);

    @DELETE("pedidos/{id}")
    Call<Void> deletePedido(@Path("id") String id);

    @GET("comidas")
    Call<List<Comida>> getComidas();

    @PUT("comidas/{id}")
    Call<Comida> updateComida(@Path("id") int id, @Body Comida comida);

    @GET("mesas")
    Call<List<Mesa>> getMesa();

    @PUT("mesas/{id}")
    Call<Mesa> updateMesa(@Path("id") int id, @Body Mesa mesa);
}
