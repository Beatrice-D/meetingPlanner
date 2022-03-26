package com.example.mettingplanner.model;

import lombok.Builder;

import java.util.List;

@Builder
public class Salle {
    private String nom;
    private int nombresDePlacesMaximale;
    private List<TypeEquipement> listeEquipementsPresents;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombresDePlacesMaximale() {
        return nombresDePlacesMaximale;
    }

    public void setNombresDePlacesMaximale(int nombresDePlacesMaximale) {
        this.nombresDePlacesMaximale = nombresDePlacesMaximale;
    }

    public List<TypeEquipement> getListeEquipementsPresents() {
        return listeEquipementsPresents;
    }

    public void setListeEquipementsPresents(List<TypeEquipement> listeEquipementsPresents) {
        this.listeEquipementsPresents = listeEquipementsPresents;
    }
}
