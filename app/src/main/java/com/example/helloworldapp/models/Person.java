package com.example.helloworldapp.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Person {

    private String prenom;
    private String nom;
    private String email;
    private String clef;
    private String dateNaissance;

    public Person(String prenom, String nom, String email, String clef, String dateNaissance) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.clef = clef;
        this.dateNaissance = dateNaissance;
    }

    public Person(String prenom, String nom, String email, String clef) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.clef = clef;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClef() {
        return clef;
    }

    public void setClef(String clef) {
        this.clef = clef;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @NonNull
    @Override
    public String toString() {
        return this.prenom + " ########### " + this.nom;
    }

    public String getFullName(){
        return String.format("%s %s", this.prenom, this.nom);
    }
}
