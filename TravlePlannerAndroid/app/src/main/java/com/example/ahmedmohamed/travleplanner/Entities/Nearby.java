package com.example.ahmedmohamed.travleplanner.Entities;

/**
 * Created by Mortadha Fadhlaoui on 11/26/2017.
 */

public class Nearby {
    private String id;
    private String name;
    private String categorie;
    private String tips;
    private String image;
    private String iconcat;
    private Double lat;
    private Double lng;
    private Integer distance;
    private Double rating;
    private String ratingColor;
    private Boolean isOpen;

    public Nearby() {
    }

    public Nearby(String id, String name, String categorie, String tips, String image, String iconcat, Double lat, Double lng, Integer distance, Double rating, String ratingColor, Boolean isOpen) {
        this.id = id;
        this.name = name;
        this.categorie = categorie;
        this.tips = tips;
        this.image = image;
        this.iconcat = iconcat;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.rating = rating;
        this.ratingColor = ratingColor;
        this.isOpen = isOpen;
    }

    public Nearby(String name, String categorie, String tips, String image, String iconcat, Double lat, Double lng, Integer distance, Double rating, String ratingColor, Boolean isOpen) {
        this.name = name;
        this.categorie = categorie;
        this.tips = tips;
        this.image = image;
        this.iconcat = iconcat;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.rating = rating;
        this.ratingColor = ratingColor;
        this.isOpen = isOpen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIconcat() {
        return iconcat;
    }

    public void setIconcat(String iconcat) {
        this.iconcat = iconcat;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "Nearby{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categorie='" + categorie + '\'' +
                ", tips='" + tips + '\'' +
                ", image='" + image + '\'' +
                ", iconcat='" + iconcat + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", distance=" + distance +
                ", rating=" + rating +
                ", ratingColor='" + ratingColor + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }
}
