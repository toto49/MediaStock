package com.eseo.mediastock.model;

/**
 * Entité représentant un utilisateur ayant des droits de gestion sur l'application.
 * <p>
 * Contient les identifiants de connexion (email, mot de passe haché) et les
 * autorisations de l'administrateur.
 * </p>
 */
public class Admin {
    private int id;
    private String nom;
    private String prenom;
    private String numTel;
    private String email;
    private String passwordHash;
    private String plainPassword;

    /**
     * Instantiates a new Admin.
     *
     * @param id           the id
     * @param nom          the nom
     * @param prenom       the prenom
     * @param email        the email
     * @param numTel       the num tel
     * @param passwordHash the password hash
     */
// Constructeur
    public Admin(int id, String nom, String prenom, String email, String numTel, String passwordHash) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
        this.passwordHash = passwordHash;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
// Getters/Setters
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets nom.
     *
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Sets nom.
     *
     * @param nom the nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Gets prenom.
     *
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Sets prenom.
     *
     * @param prenom the prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Gets num tel.
     *
     * @return the num tel
     */
    public String getNumTel() {
        return numTel;
    }  //

    /**
     * Sets num tel.
     *
     * @param num_Tel the num tel
     */
    public void setNumTel(String num_Tel) {
        this.numTel = num_Tel;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets password hash.
     *
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets password hash.
     *
     * @param passwordHash the password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets plain password.
     *
     * @return the plain password
     */
    public String getPlainPassword() {
        return plainPassword;
    }

    /**
     * Sets plain password.
     *
     * @param plainPassword the plain password
     */
    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
}