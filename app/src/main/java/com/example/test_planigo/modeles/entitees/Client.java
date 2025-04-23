package com.example.test_planigo.modeles.entitees;

public class Client {

    private String nom;
    private String prenom;
    private String nom_utilisateur;
    private String mot_de_passe;
    private String description;
    private Integer age;
    private String genre;
    private String profileImageUrl;
    public Client() {
    }

    public Client(String nom, String prenom, String nom_utilisateur, String mot_de_passe) {
        //this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.nom_utilisateur = nom_utilisateur;
        this.mot_de_passe = mot_de_passe;
        this.description = null;
        this.age = null;
        this.genre = null;
        this.profileImageUrl = null;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {

        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {

        this.prenom = prenom;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {

        this.mot_de_passe = mot_de_passe;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) {
        this.age = age; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) {
        this.genre = genre; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl; }

}