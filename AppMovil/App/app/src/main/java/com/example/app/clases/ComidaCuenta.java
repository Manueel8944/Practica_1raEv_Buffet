package com.example.app.clases;

public class ComidaCuenta {
    int idComida;
    String nombre;
    double precio;
    int cantidad;

    public ComidaCuenta(int idComida, String nombre, double precio, int cantidad) {
        this.idComida = idComida;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public int getIdComida() {
        return idComida;
    }

    public void setIdComida(int idComida) {
        this.idComida = idComida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
