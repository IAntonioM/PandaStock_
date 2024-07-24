package com.app.pandastock.models;

public class Personal {
    private String id;
    private String fullName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String role;

    public Personal() {}

    public Personal(String id, String fullName, String lastName, String username, String password, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}

