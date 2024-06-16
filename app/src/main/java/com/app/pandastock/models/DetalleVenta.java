package com.app.pandastock.models;

import com.google.firebase.firestore.DocumentReference;

public class DetalleVenta {
    private String id;
    private DocumentReference Venta;
    private DocumentReference Producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    // Constructor
    public DetalleVenta() {
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getVenta() {
        return Venta;
    }

    public void setVenta(DocumentReference venta) {
        Venta = venta;
    }

    public DocumentReference getProducto() {
        return Producto;
    }

    public void setProducto(DocumentReference producto) {
        Producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
