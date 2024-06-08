package com.app.pandastock.models;

public class InventarioProducto {
    private int id;
    private int productoId;
    private String codigoBarras;

    public InventarioProducto(int id, int productoId, String codigoBarras) {
        this.id = id;
        this.productoId = productoId;
        this.codigoBarras = codigoBarras;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
}

