package com.app.pandastock.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Producto {
    private String id;
    private DocumentReference tipoProductoRef;
    private DocumentReference marcaRef;
    private String modelo;
    private double precio;
    private ArrayList<String> codigosBarra;
    private int stock;
    private long fechaCreacion;
    private long fechaActualizacion;

    public Producto() {
    }

    public Producto(String id, DocumentReference tipoProductoRef, DocumentReference marcaRef,
                    String modelo, double precio, ArrayList<String> codigosBarra, int stock,
                    long fechaCreacion, long fechaActualizacion) {
        this.id = id;
        this.tipoProductoRef = tipoProductoRef;
        this.marcaRef = marcaRef;
        this.modelo = modelo;
        this.precio = precio;
        this.codigosBarra = codigosBarra;
        this.stock = stock;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getTipoProductoRef() {
        return tipoProductoRef;
    }

    public void setTipoProductoRef(DocumentReference tipoProductoRef) {
        this.tipoProductoRef = tipoProductoRef;
    }

    public DocumentReference getMarcaRef() {
        return marcaRef;
    }

    public void setMarcaRef(DocumentReference marcaRef) {
        this.marcaRef = marcaRef;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public ArrayList<String> getCodigosBarra() {
        return codigosBarra;
    }

    public void setCodigosBarra(ArrayList<String> codigosBarra) {
        this.codigosBarra = codigosBarra;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(long fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}