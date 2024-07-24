package com.app.pandastock.models;

public class Usuario {
    private String uid; // Cambiado a String para adaptarse al ID generado por Firestore
    private String empresa;
    private String email;
    private String password;

    // Constructor vacío necesario para Firestore
    public Usuario() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
}
