package com.example.test_planigo.modeles.entitees;

import java.util.List;

public class Recette extends RecetteAbrege{
    private String description;
    private String createur_nom_utilisateur;
    private List<String> ingredients;
    private String categorie;
    private List<String> etapes;

    public Recette() {
    }

    public Recette(int id, String nom, String description, int temps_de_cuisson, String type, String image_url, String createur_nom_utilisateur, List<String> ingredients, String categorie, List<String> etapes) {
        super(id, nom, temps_de_cuisson, type, image_url);
        this.description = description;
        this.createur_nom_utilisateur = createur_nom_utilisateur;
        this.ingredients = ingredients;
        this.categorie = categorie;
        this.etapes = etapes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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