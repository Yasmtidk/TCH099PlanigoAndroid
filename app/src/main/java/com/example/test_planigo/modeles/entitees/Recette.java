package com.example.test_planigo.modeles.entitees;

import java.util.List;

public class Recette {
    private int id;
    private String nom;
    private String description;
    private int temps_de_cuisson;
    private String image_url;
    private String createur_nom_utilisateur;
    private List<String> ingredients;
    private String categorie;
    private List<String> etapes;

    public Recette() {
    }

    public Recette(int id, String nom, String description, int temps_de_cuisson, String image_url, String createur_nom_utilisateur, List<String> ingredients, String categorie, List<String> etapes) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.temps_de_cuisson = temps_de_cuisson;
        this.image_url = image_url;
        this.createur_nom_utilisateur = createur_nom_utilisateur;
        this.ingredients = ingredients;
        this.categorie = categorie;
        this.etapes = etapes;
    }

     public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemps_de_cuisson() {
        return temps_de_cuisson;
    }

    public void setTemps_de_cuisson(int temps_de_cuisson) {
        this.temps_de_cuisson = temps_de_cuisson;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreateur_nom_utilisateur() {
        return createur_nom_utilisateur;
    }

    public void setCreateur_nom_utilisateur(String createur_nom_utilisateur) {
        this.createur_nom_utilisateur = createur_nom_utilisateur;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<String> getEtapes() {
        return etapes;
    }

    public void setEtapes(List<String> etapes) {
        this.etapes = etapes;
    }
}