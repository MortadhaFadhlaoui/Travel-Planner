package com.example.ahmedmohamed.travleplanner.Entities;

/**
 * Created by mortadha on 12/31/17.
 */

public class Country {
    String name;
    String flag;

    public Country() {
    }

    public Country(String name, String flag) {
        this.name = name;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
