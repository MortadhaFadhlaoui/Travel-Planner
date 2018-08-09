package com.example.ahmedmohamed.travleplanner.Entities;

/**
 * Created by AHMED Mohamed on 14/11/2017.
 */

public class User {
    int id;
    String nom;
    String prenom;
    String password;
    String username;
    String image;
    String num;
    String email;
    String role;

    public User() {

    }

    public User(int id, String nom, String prenom, String password, String username, String image, String num, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.username = username;
        this.image = image;
        this.num = num;
        this.email = email;
        this.role = role;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", image='" + image + '\'' +
                ", num='" + num + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
