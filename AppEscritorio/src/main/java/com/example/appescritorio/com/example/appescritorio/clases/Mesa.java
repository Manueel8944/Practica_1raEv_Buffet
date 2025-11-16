package com.example.appescritorio.com.example.appescritorio.clases;

public class Mesa {
    private int id;
    private String nombre;
    private boolean estadoMesa;
    private boolean estadoPedido;

    public Mesa(boolean estadoPedido, int id, String nombre, boolean estadoMesa) {
        this.estadoPedido = estadoPedido;
        this.id = id;
        this.nombre = nombre;
        this.estadoMesa = estadoMesa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstadoMesa() {
        return estadoMesa;
    }

    public void setEstadoMesa(boolean estadoMesa) {
        this.estadoMesa = estadoMesa;
    }

    public boolean isEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(boolean estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
}
