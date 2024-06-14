package com.app.pandastock.models;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class MovimientoInventario {

    private String id;
    private DocumentReference usuario;
    private DocumentReference producto;
    private ArrayList<String> codigosBarra;
    private int cantidad;
    private String tipo;
    private Date fechaRegistro;

    //Constructor
    public MovimientoInventario() {
    }

    public MovimientoInventario(String id, DocumentReference usuario, DocumentReference producto, ArrayList<String> codigosBarra, int cantidad, String tipo, Date fechaRegistro) {
        this.id = id;
        this.usuario = usuario;
        this.producto = producto;
        this.codigosBarra = codigosBarra;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.fechaRegistro = fechaRegistro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getUsuario() {
        return usuario;
    }

    public void setUsuario(DocumentReference usuario) {
        this.usuario = usuario;
    }

    public DocumentReference getProducto() {
        return producto;
    }

    public void setProducto(DocumentReference producto) {
        this.producto = producto;
    }

    public ArrayList<String> getCodigosBarra() {
        return codigosBarra;
    }

    public void setCodigosBarra(ArrayList<String> codigosBarra) {
        this.codigosBarra = codigosBarra;
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}

