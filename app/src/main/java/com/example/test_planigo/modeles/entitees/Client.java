package com.example.test_planigo.modeles.entitees;

// Pas de commentaire nécessaire pour une classe Entité simple si les noms sont clairs.
public class Client {

    private String nom;
    private String prenom;
    private String nom_utilisateur; // Identifiant unique pour la connexion
    private String mot_de_passe;
    private String description; // Biographie de l'utilisateur
    private Integer age;
    private String genre;
    private String profileImageUrl; // URL de l'image de profil

    // Constructeur par défaut (requis par certaines bibliothèques comme Jackson/Firebase)
    public Client() {
    }

    // Constructeur principal pour la création/connexion initiale
    public Client(String nom, String prenom, String nom_utilisateur, String mot_de_passe) {
        this.nom = nom;
        this.prenom = prenom;
        this.nom_utilisateur = nom_utilisateur;
        this.mot_de_passe = mot_de_passe;
        // Les autres champs sont initialisés à null par défaut
        this.description = null;
        this.age = null;
        this.genre = null;
        this.profileImageUrl = null;
    }

    // --- Getters ---
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getNom_utilisateur() {
        return nom_utilisateur;
    }
    public String getMot_de_passe() {
        return mot_de_passe;
    }
    public String getDescription() { return description; }
    public Integer getAge() { return age; }
    public String getGenre() { return genre; }
    public String getProfileImageUrl() { return profileImageUrl; }

    // --- Setters ---
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }
    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }
    public void setDescription(String description) {
        this.description = description; }
    public void setAge(Integer age) {
        this.age = age; }
    public void setGenre(String genre) {
        this.genre = genre; }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl; }
}