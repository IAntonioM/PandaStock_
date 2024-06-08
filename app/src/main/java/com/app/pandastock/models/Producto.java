package com.app.pandastock.models;

public class Producto {
    private int id;
    private int tipoProductoId;
    private int marcaId;
    private String modelo;
    private double precio;
    private int stock;

    public Producto(int id, int tipoProductoId, int marcaId, String modelo, double precio, int stock) {
        this.id = id;
        this.tipoProductoId = tipoProductoId;
        this.marcaId = marcaId;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipoProductoId() {
        return tipoProductoId;
    }

    public void setTipoProductoId(int tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }

    public int getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(int marcaId) {
        this.marcaId = marcaId;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
