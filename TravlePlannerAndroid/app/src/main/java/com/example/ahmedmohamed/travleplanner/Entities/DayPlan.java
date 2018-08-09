package com.example.ahmedmohamed.travleplanner.Entities;

import java.util.Date;

/**
 * Created by AHMED Mohamed on 12/12/2017.
 */

public class DayPlan {
    String date;
    String heure;
    String Title;
    String description;
    int id;

    public DayPlan() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DayPlan{" +
                "date=" + date +
                ", Title='" + Title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
