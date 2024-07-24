package com.app.pandastock.models;

public class ProductoData {
    String producto;
    int StockActual;

    public ProductoData(String producto, int stockActual) {
        this.producto = producto;
        StockActual = stockActual;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getStockActual() {
        return StockActual;
    }

    public void setStockActual(int stockActual) {
        StockActual = stockActual;
    }
}
