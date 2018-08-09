package com.example.ahmedmohamed.travleplanner.Entities;

/**
 * Created by AHMED Mohamed on 11/12/2017.
 */

public class Pays {

    String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Pays() {
    }

    @Override
    public String toString() {
        return "Pays{" +
                "nom='" + nom + '\'' +
                '}';
    }
}
