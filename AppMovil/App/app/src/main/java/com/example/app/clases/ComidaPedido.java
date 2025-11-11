package com.example.app.clases;

public class ComidaPedido {
    private int idComida;
    private int cantidad;

    public ComidaPedido(int idComida, int cantidad) {
        this.idComida = idComida;
        this.cantidad = cantidad;
    }

    public int getIdComida() {
        return idComida;
    }

    public void setIdComida(int idComida) {
        this.idComida = idComida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
