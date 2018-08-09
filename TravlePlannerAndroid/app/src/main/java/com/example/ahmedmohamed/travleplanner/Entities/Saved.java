package com.example.ahmedmohamed.travleplanner.Entities;

/**
 * Created by AHMED Mohamed on 10/12/2017.
 */

public class Saved {
    int id;
    String title;
    String name;
    String categorie;
    double lat;
    double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Saved(int id, String title, String name, String categorie) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.categorie = categorie;
    }

    public Saved() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
