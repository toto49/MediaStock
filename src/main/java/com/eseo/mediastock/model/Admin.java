package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Produits.Exemplaire;

public class Admin {
    private int id;
    private String email;
    private String password;

    public int getId() {
        return id;
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
}