package com.example.mettingplanner.model;

import lombok.Builder;

import java.util.List;

@Builder
public class SalleMatching {
    private boolean salleDejaDansMapAssociations;
    private String nomSalle;
    private boolean allMatch;
    private boolean anyMatch;
    private List<TypeEquipement> listeEquipementsManquants;
    private int nombreEquipementsManquants;

    public boolean isSalleDejaDansMapAssociations() {
        return salleDejaDansMapAssociations;
    }

    public void setSalleDejaDansMapAssociations(boolean salleDejaDansMapAssociations) {
        this.salleDejaDansMapAssociations = salleDejaDansMapAssociations;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public boolean isAllMatch() {
        return allMatch;
    }

    public void setAllMatch(boolean allMatch) {
        this.allMatch = allMatch;
    }

    public boolean isAnyMatch() {
        return anyMatch;
    }

    public void setAnyMatch(boolean anyMatch) {
        this.anyMatch = anyMatch;
    }

    public List<TypeEquipement> getListeEquipementsManquants() {
        return listeEquipementsManquants;
    }

    public void setListeEquipementsManquants(List<TypeEquipement> listeEquipementsManquants) {
        this.listeEquipementsManquants = listeEquipementsManquants;
    }

    public int getNombreEquipementsManquants() {
        return nombreEquipementsManquants;
    }

    public void setNombreEquipementsManquants(int nombreEquipementsManquants) {
        this.nombreEquipementsManquants = nombreEquipementsManquants;
    }
}
