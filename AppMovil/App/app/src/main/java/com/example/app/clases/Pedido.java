package com.example.app.clases;

import java.util.ArrayList;

public class Pedido {
    private Number _id;
    private Number idMesa;
    private ArrayList<ComidaPedido> comidas;

    public Pedido(Number idMesa, ArrayList<ComidaPedido> comidas) {
        this.idMesa = idMesa;
        this.comidas = comidas;
    }

    public Number get_Id() {
        return _id;
    }

    public void set_Id(Number id) {
        this._id = id;
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
