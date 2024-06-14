package com.app.pandastock.models;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class MovimientoInventario {

    private String id;
    private DocumentReference vendedor;
    private DocumentReference producto;
    private DocumentReference venta;
    private DocumentReference InventarioProducto;
    private int cantidad;
    private String tipo;
    private @ServerTimestamp Date fecha;

    public MovimientoInventario() {
        // Constructor vacío requerido para Firestore
    }

    public MovimientoInventario(String id, DocumentReference vendedor, DocumentReference producto, int cantidad, String tipo, Date fecha) {
        this.id = id;
        this.vendedor = vendedor;
        this.producto = producto;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getVendedor() {
        return vendedor;
    }

    public void setVendedor(DocumentReference vendedor) {
        this.vendedor = vendedor;
    }

    public DocumentReference getProducto() {
        return producto;
    }

    public void setProducto(DocumentReference producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

