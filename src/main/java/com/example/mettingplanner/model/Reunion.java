package com.example.mettingplanner.model;

import lombok.Builder;

@Builder
public class Reunion {
    private CreneauxHoraire creneauxHoraire;
    private TypeReunion type;
    private int nombreDePersonnesConvieesSurSite;

    public CreneauxHoraire getCreneauxHoraire() {
        return creneauxHoraire;
    }

    public void setCreneauxHoraire(CreneauxHoraire creneauxHoraire) {
        this.creneauxHoraire = creneauxHoraire;
    }

    public TypeReunion getType() {
        return type;
    }

    public void setType(TypeReunion type) {
        this.type = type;
    }

    public int getNombreDePersonnesConvieesSurSite() {
        return nombreDePersonnesConvieesSurSite;
    }

    public void setNombreDePersonnesConvieesSurSite(int nombreDePersonnesConvieesSurSite) {
        this.nombreDePersonnesConvieesSurSite = nombreDePersonnesConvieesSurSite;
    }
}
