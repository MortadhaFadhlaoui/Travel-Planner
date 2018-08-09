package com.example.ahmedmohamed.travleplanner.Entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by AHMED Mohamed on 12/12/2017.
 */

public class Pack {
    int id;
    String depart;
    int prix;
    String depart_date;
    String return_date;
    ArrayList<Pays> pays;
    ArrayList<DayPlan> dayplans=new ArrayList<>();

    public Pack() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getDepart_date() {
        return depart_date;
    }

    public void setDepart_date(String depart_date) {
        this.depart_date = depart_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public ArrayList<Pays> getPays() {
        return pays;
    }

    public void setPays(ArrayList<Pays> pays) {
        this.pays = pays;
    }

    public ArrayList<DayPlan> getDayplans() {
        return dayplans;
    }

    public void setDayplans(ArrayList<DayPlan> dayplans) {
        this.dayplans = dayplans;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", depart='" + depart + '\'' +
                ", prix=" + prix +
                ", depart_date=" + depart_date +
                ", return_date=" + return_date +
                ", pays=" + pays +
                ", dayplans=" + dayplans +
                '}';
    }
}
