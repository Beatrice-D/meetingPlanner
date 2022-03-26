package com.example.mettingplanner.model;

import lombok.Builder;

@Builder
public class ReservationEquipement {
    private int nombreRessources;
    private CreneauxHoraire creneauxHoraire;

    public int getNombreRessources() {
        return nombreRessources;
    }

    public void setNombreRessources(int nombreRessources) {
        this.nombreRessources = nombreRessources;
    }

    public CreneauxHoraire getCreneauxHoraire() {
        return creneauxHoraire;
    }

    public void setCreneauxHoraire(CreneauxHoraire creneauxHoraire) {
        this.creneauxHoraire = creneauxHoraire;
    }
}
