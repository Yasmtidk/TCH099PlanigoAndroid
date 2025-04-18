package com.example.test_planigo.modeles.entitees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecetteComplete extends RecetteAbrege{

    private String description;
    private String difficulter;
    private String portions;
    private String createur_nom_utilisateur;
    private Produit[] ingredients;
    private String[] etapes;
    private String[] restrictions;

    public RecetteComplete(){
    }

    public RecetteComplete(int id, String nom, int temps_de_cuisson, String type, String image, String description, String difficulter, String portions, String createur_nom_utilisateur, Produit[] ingredients, String[] etapes, String[] restrictions) {
        super(id, nom, temps_de_cuisson, type, image);
        this.description = description;
        this.difficulter = difficulter;
        this.portions = portions;
        this.createur_nom_utilisateur = createur_nom_utilisateur;
        this.ingredients = ingredients;
        this.etapes = etapes;
        this.restrictions = restrictions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulter() {
        return difficulter;
    }

    public void setDifficulter(String difficulter) {
        this.difficulter = difficulter;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public String getCreateur_nom_utilisateur() {
        return createur_nom_utilisateur;
    }

    public void setCreateur_nom_utilisateur(String createur_nom_utilisateur) {
        this.createur_nom_utilisateur = createur_nom_utilisateur;
    }

    public Produit[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Produit[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getEtapes() {
        return etapes;
    }

    public void setEtapes(String[] etapes) {
        this.etapes = etapes;
    }

    public String[] getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String[] restrictions) {
        this.restrictions = restrictions;
    }

}
