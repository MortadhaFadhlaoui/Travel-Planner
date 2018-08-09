package com.example.ahmedmohamed.travleplanner.Entities;

import android.support.annotation.NonNull;

/**
 * Created by Mortadha Fadhlaoui on 12/12/2017.
 */

public class Paysmorta implements Comparable  {

    public int id;
    public String nom_pays;
    public Packmorta pack;
    public String image;
    public int etat;

    public Paysmorta() {
    }

    public Paysmorta(int id, String nom_pays, Packmorta pack, String image, int etat) {
        this.id = id;
        this.nom_pays = nom_pays;
        this.pack = pack;
        this.image = image;
        this.etat = etat;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_pays() {
        return nom_pays;
    }

    public void setNom_pays(String nom_pays) {
        this.nom_pays = nom_pays;
    }

    public Packmorta getPack() {
        return pack;
    }

    public void setPack(Packmorta pack) {
        this.pack = pack;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Pays{" +
                "id=" + id +
                ", nom_pays='" + nom_pays + '\'' +
                ", pack=" + pack +
                ", image='" + image + '\'' +
                ", etat=" + etat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paysmorta pays = (Paysmorta) o;

        return pack.equals(pays.pack);
    }

    @Override
    public int hashCode() {
        return pack.hashCode();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Paysmorta pays = (Paysmorta) o;
        return (pack.prix.intValue() - pays.pack.prix.intValue());
    }
}
