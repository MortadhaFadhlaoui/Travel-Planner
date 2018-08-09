package com.example.ahmedmohamed.travleplanner.Entities;

/**
 * Created by AHMED Mohamed on 30/12/2017.
 */

public class Reservation {
    int id;
    User user;
    Pack pack;
    String date;
    String etat;

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Reservation() {
    }

    public Reservation(int id, User user, Pack pack, String date) {
        this.id = id;
        this.user = user;
        this.pack = pack;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
