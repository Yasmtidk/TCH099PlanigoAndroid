package com.example.test_planigo.modeles.entitees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecetteAbrege {
    private int id;
    private String nom;
    private int temps_de_cuisson;
    private String type;
    private String image;

    public RecetteAbrege() {
    }

    public RecetteAbrege(int id, String nom, int temps_de_cuisson, String type, String image) {
        this.id = id;
        this.nom = nom;
        this.temps_de_cuisson = temps_de_cuisson;
        this.type = type;
        this.image = image;
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

    public int getTemps_de_cuisson() {
        return temps_de_cuisson;
    }

    public void setTemps_de_cuisson(int temps_de_cuisson) {
        this.temps_de_cuisson = temps_de_cuisson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image_url) {
        this.image = image_url;
    }
}
