package com.example.test_planigo.modeles.entitees;

public class PlanRepasSommaire {

    private int id;
    private String titre;
    private String descriptions;
    private String nom_utilisateur;

    public PlanRepasSommaire(){}
    public PlanRepasSommaire(int id, String titre, String descriptions, String nom_utilisateur) {
        this.id = id;
        this.titre = titre;
        this.descriptions = descriptions;
        this.nom_utilisateur = nom_utilisateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }
}
