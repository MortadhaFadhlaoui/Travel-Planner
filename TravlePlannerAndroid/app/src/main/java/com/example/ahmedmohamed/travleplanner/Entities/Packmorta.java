package com.example.ahmedmohamed.travleplanner.Entities;

import android.support.annotation.NonNull;

/**
 * Created by Mortadha Fadhlaoui on 12/12/2017.
 */

public class Packmorta implements Comparable {
    public int id;
    public String date_debut;
    public String date_fin;
    public String nom_depart;
    public Double prix;

    public Packmorta() {
    }



    public Packmorta(int id, String date_debut, String date_fin, String nom_depart, Double prix) {
        this.id = id;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.nom_depart = nom_depart;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getNom_depart() {
        return nom_depart;
    }

    public void setNom_depart(String nom_depart) {
        this.nom_depart = nom_depart;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Packmorta pack = (Packmorta) o;

        if (id != pack.id) return false;
        return prix.equals(pack.prix);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + prix.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", nom_depart='" + nom_depart + '\'' +
                ", prix=" + prix +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Packmorta pack = (Packmorta) o;
        return (prix.intValue() - pack.prix.intValue());
    }
}
