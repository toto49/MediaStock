package com.eseo.mediastock.model;

public class Admin {
    private int id;
    private String nom;
    private String prenom;
    private String numTel;
    private String email;
    private String passwordHash;
    private String plainPassword;

    // Constructeur
    public Admin(int id, String nom, String prenom, String email, String numTel, String passwordHash) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
        this.passwordHash = passwordHash;
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNumTel() {
        return numTel;
    }  //

    public void setNumTel(String num_Tel) {
        this.numTel = num_Tel;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPlainPassword() { return plainPassword; }
    public void setPlainPassword(String plainPassword) { this.plainPassword = plainPassword; }
}