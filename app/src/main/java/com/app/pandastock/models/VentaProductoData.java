package com.app.pandastock.models;

public class VentaProductoData {
    String producto;
    double ganancia;
    int cantidadVendida;

    public VentaProductoData(int cantidadVendida, double ganancia, String producto) {
        this.cantidadVendida = cantidadVendida;
        this.ganancia = ganancia;
        this.producto = producto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public double getGanancia() {
        return ganancia;
    }

    public void setGanancia(double ganancia) {
        this.ganancia = ganancia;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }
}
