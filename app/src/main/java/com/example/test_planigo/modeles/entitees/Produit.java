package com.example.test_planigo.modeles.entitees;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Produit {
    @JsonProperty("nom")
    private String name;

    @JsonAlias({"quantite_disponible", "quantite"})
    private double quantity;
    @JsonProperty("unite_de_mesure")
    private String unit;
    private String icon;

    public Produit(){}

    public Produit(String name, double quantity, String unit){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Produit(String name, double quantity, String unit, String icon) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return Double.compare(quantity, produit.quantity) == 0 && Objects.equals(name, produit.name) && Objects.equals(unit, produit.unit);
    }
}
