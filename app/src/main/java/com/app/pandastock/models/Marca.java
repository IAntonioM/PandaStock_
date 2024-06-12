package com.app.pandastock.models;

public class Marca {
    private String Id;
    private String Nombre;
    private String IdTipoProducto;

    // Constructor vacío necesario para Firestore
    public Marca() {}

    public Marca(String id, String nombre, String idTipoProducto) {
        this.Id = id;
        this.Nombre = nombre;
        this.IdTipoProducto = idTipoProducto;
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

    public String getIdTipoProducto() {
        return IdTipoProducto;
    }

    public void setIdTipoProducto(String idTipoProducto) {
        IdTipoProducto = idTipoProducto;
    }
}
