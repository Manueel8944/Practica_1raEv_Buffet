package com.example.app.clases;

import java.util.ArrayList;

public class Pedido {
    private String _id;
    private Number idMesa;
    private ArrayList<ComidaPedido> comidas;

    public Pedido() { // Hay que crear un constructor vacio ya que en el constructor principal no pongo el id asi que es necesario para que retrofit no le ponga un valor nulo

    }

    public Pedido(Number idMesa, ArrayList<ComidaPedido> comidas) {
        this.idMesa = idMesa;
        this.comidas = comidas;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Number getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Number idMesa) {
        this.idMesa = idMesa;
    }

    public ArrayList<ComidaPedido> getComidas() {
        return comidas;
    }

    public void setComidas(ArrayList<ComidaPedido> comidas) {
        this.comidas = comidas;
    }
}
