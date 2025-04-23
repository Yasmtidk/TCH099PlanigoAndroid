package com.example.test_planigo.modeles.entitees;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class RecetteAbrege implements Parcelable{
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

    // Constructeur utilisé par le CREATOR pour recréer l'objet depuis un Parcel
    protected RecetteAbrege(Parcel in) {
        id = in.readInt();
        nom = in.readString();
        temps_de_cuisson = in.readInt();
        type = in.readString();
        image = in.readString();
    }

    // Objet CREATOR requis pour Parcelable
    public static final Parcelable.Creator<RecetteAbrege> CREATOR = new Parcelable.Creator<RecetteAbrege>() {
        @Override
        public RecetteAbrege createFromParcel(Parcel in) {
            return new RecetteAbrege(in);
        }

        @Override
        public RecetteAbrege[] newArray(int size) {
            return new RecetteAbrege[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nom);
        dest.writeInt(temps_de_cuisson);
        dest.writeString(type);
        dest.writeString(image);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecetteAbrege that = (RecetteAbrege) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
