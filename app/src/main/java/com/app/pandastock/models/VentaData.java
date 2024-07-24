package com.app.pandastock.models;

public class VentaData {
    private String fecha;
    private double montoTotal;

    public VentaData(String fecha, double montoTotal) {
        this.fecha = fecha;
        this.montoTotal = montoTotal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}
