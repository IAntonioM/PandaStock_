package com.app.pandastock.models;

public class TipoProducto {
    private String Id;
    private String Nombre;

    // Constructor vacío necesario para Firestore
    public TipoProducto() {}

    public TipoProducto(String id, String nombre) {
        this.Id = id;
        this.Nombre = nombre;
    }

    // Getters y Setters
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}

