package com.example.test_planigo.modeles.entitees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {

    @JsonProperty("nom")
    private String name;
    @JsonProperty("unite_de_mesure")
    private String uniteMesure;

    public Ingredient(){}

    public Ingredient(String name, String uniteMesure) {
        this.name = name;
        this.uniteMesure = uniteMesure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniteMesure() {
        return uniteMesure;
    }

    public void setUniteMesure(String uniteMesure) {
        this.uniteMesure = uniteMesure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) && Objects.equals(uniteMesure, that.uniteMesure);
    }
}
