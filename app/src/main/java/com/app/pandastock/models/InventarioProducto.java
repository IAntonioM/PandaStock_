package com.app.pandastock.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class InventarioProducto {
    private String id;
    private String codigoRegistro;
    private int productoId;
    private ArrayList<String> codigosBarras;
    private @ServerTimestamp Date fechaRegistro;

    public InventarioProducto() {
        // Constructor vacío requerido por Firestore
    }

    // Constructor con parámetros, puedes ajustarlo según tus necesidades
    public InventarioProducto(int productoId, ArrayList<String> codigosBarras, Date fechaRegistro) {
        this.productoId = productoId;
        this.codigosBarras = codigosBarras;
        this.fechaRegistro = fechaRegistro;
        // Generamos un código de registro único basado en la fecha y el productoId (ejemplo)
        this.codigoRegistro = generarCodigoRegistro(productoId, fechaRegistro);
    }

    // Método para generar un código de registro único
    private String generarCodigoRegistro(int productoId, Date fechaRegistro) {
        // Aquí puedes implementar la lógica para generar el código de registro
        // Ejemplo: concatenación de productoId y fecha formateada
        return "Lote-" + productoId + "-" + fechaRegistro.getTime(); // Ejemplo de formato
    }

}


