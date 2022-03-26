package com.example.mettingplanner.model;

import lombok.Builder;

@Builder
public class CreneauxHoraire {
    private Integer debut;
    private Integer fin;

    public Integer getDebut() {
        return debut;
    }

    public void setDebut(Integer debut) {
        this.debut = debut;
    }

    public Integer getFin() {
        return fin;
    }

    public void setFin(Integer fin) {
        this.fin = fin;
    }
}
