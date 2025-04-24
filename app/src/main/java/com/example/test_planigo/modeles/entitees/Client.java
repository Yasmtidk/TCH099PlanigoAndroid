package com.example.test_planigo.modeles.entitees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Pas de commentaire nécessaire pour une classe Entité simple si les noms sont clairs.
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {

    private String nom;
    private String prenom;
    private String nom_utilisateur; // Identifiant unique pour la connexion
    private String mot_de_passe;
    private String description; // Biographie de l'utilisateur

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
}