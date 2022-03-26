package com.example.mettingplanner.controller;

import com.example.mettingplanner.model.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class MeetingPlannerController {

    Map<String, List<Reunion>> mapAssociationsSalleReunion;
    List<SalleMatching> listeSallesMatching = new ArrayList<>();
    Map<TypeEquipement, ReservationEquipement> mapEquipementsSupplementaires;

    /**
     * Gestionnaire des salles de réunion d’une PME
     * @return
     */
    @GetMapping(value = "/handle-meeting-planner", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Reunion>>> handleMeetingPlanner() {
        mapAssociationsSalleReunion = new HashMap<>();
        List<Salle> listeSalles = initialisationSalles();
        List<Reunion> listeReunions = initialisationReunions();
        mapEquipementsSupplementaires = initmapEquipementsSupplementaires();

        listeReunions.forEach(reunion -> {
            listeSalles.forEach(salle -> {
                // Vérification de si le nombre de personnes conviées sur site est inférieur au 70% de la capacité
                // maximale de la salle
                if (reunion.getNombreDePersonnesConvieesSurSite() <= (salle.getNombresDePlacesMaximale() * (70 / 100.0))) {
                    // Variable qui indique si la salle a déjà été réservée pour des réunions
                    boolean salleDejaDansMapAssociations = mapAssociationsSalleReunion.containsKey(salle.getNom());
                    if (salleDejaDansMapAssociations) {
                        // Si la salle a déjà été réservée pour des réunions, vérifier si la salle sera libre
                        // pour effecteur la réunion en question
                        List<Reunion> reunionDejaPlanifieeDansSalle = mapAssociationsSalleReunion.get(salle.getNom());
                        if (!horairesSeChevauchent(reunion, reunionDejaPlanifieeDansSalle)) {
                            // En fonction du type de réunion, calcul des ressources disponibles pour la réunion
                            gestionDesRessourcesDisponibles(salleDejaDansMapAssociations, salle, reunion);
                        }
                    } else {
                        // En fonction du type de réunion, calcul des ressources disponibles pour la réunion
                        gestionDesRessourcesDisponibles(salleDejaDansMapAssociations, salle, reunion);
                    }
                }
            });
            // Association des salles aux réunions
            gestionAssociationsSalleReunion(reunion);
            listeSallesMatching.clear();
        });

        return ResponseEntity.ok(mapAssociationsSalleReunion);
    }


    /**
     * Initialisation des salles
     * @return
     */
    public List<Salle> initialisationSalles() {
        return Stream.of(initSalle("E1001", 23, Stream.of(TypeEquipement.NEANT)
                                .collect(Collectors.toList())),
                        initSalle("E1002", 10, Stream.of(TypeEquipement.ECRAN)
                                .collect(Collectors.toList())),
                        initSalle("E1003", 8, Stream.of(TypeEquipement.PIEUVRE)
                                .collect(Collectors.toList())),
                        initSalle("E1004", 4, Stream.of(TypeEquipement.TABLEAU)
                                .collect(Collectors.toList())),
                        initSalle("E2001", 4, Stream.of(TypeEquipement.NEANT)
                                .collect(Collectors.toList())),
                        initSalle("E2002", 15, Stream.of(TypeEquipement.ECRAN,
                                TypeEquipement.WEBCAM).collect(Collectors.toList())),
                        initSalle("E2003", 7, Stream.of(TypeEquipement.NEANT)
                                .collect(Collectors.toList())),
                        initSalle("E2004", 9, Stream.of(TypeEquipement.TABLEAU)
                                .collect(Collectors.toList())),
                        initSalle("E3001", 13, Stream.of(TypeEquipement.ECRAN,
                                TypeEquipement.WEBCAM, TypeEquipement.PIEUVRE).collect(Collectors.toList())),
                        initSalle("E3002", 8, Stream.of(TypeEquipement.NEANT)
                                .collect(Collectors.toList())),
                        initSalle("E3003", 9, Stream.of(TypeEquipement.ECRAN,
                                TypeEquipement.PIEUVRE).collect(Collectors.toList())),
                        initSalle("E3004", 4, Stream.of(TypeEquipement.NEANT)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
    public Salle initSalle(String nom, int nombresDePlacesMaximale, List<TypeEquipement> listeEquipementsPresents) {
        return Salle.builder().nom(nom).nombresDePlacesMaximale(nombresDePlacesMaximale)
                .listeEquipementsPresents(listeEquipementsPresents).build();
    }

    /**
     * Initialisation des réunions
     * @return
     */
    public List<Reunion> initialisationReunions() {
        return Stream.of(initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.VC, 8),
                        initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.VC, 6),
                        initReunion(CreneauxHoraire.builder().debut(11).fin(12).build(), TypeReunion.RC, 4),
                        initReunion(CreneauxHoraire.builder().debut(11).fin(12).build(), TypeReunion.RS, 2),
                        initReunion(CreneauxHoraire.builder().debut(11).fin(12).build(), TypeReunion.SPEC, 9),
                        initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.RC, 7),
                        initReunion(CreneauxHoraire.builder().debut(8).fin(9).build(), TypeReunion.VC, 9),
                        initReunion(CreneauxHoraire.builder().debut(8).fin(9).build(), TypeReunion.SPEC, 10),
                        initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.SPEC, 5),
                        initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.RS, 4),
                        initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.RC, 8),
                        initReunion(CreneauxHoraire.builder().debut(11).fin(12).build(), TypeReunion.VC, 12),
                        initReunion(CreneauxHoraire.builder().debut(11).fin(12).build(), TypeReunion.SPEC, 5),
                        initReunion(CreneauxHoraire.builder().debut(8).fin(9).build(), TypeReunion.VC, 3),
                        initReunion(CreneauxHoraire.builder().debut(8).fin(9).build(), TypeReunion.SPEC, 2),
                        initReunion(CreneauxHoraire.builder().debut(8).fin(9).build(), TypeReunion.VC, 12),
                        initReunion(CreneauxHoraire.builder().debut(10).fin(11).build(), TypeReunion.VC, 6),
                        initReunion(CreneauxHoraire.builder().debut(11).fin(12).build(), TypeReunion.RS, 2),
                        initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.RS, 4),
                        initReunion(CreneauxHoraire.builder().debut(9).fin(10).build(), TypeReunion.RC, 7)
                )
                .collect(Collectors.toList());
    }
    public Reunion initReunion(CreneauxHoraire creneauxHoraire, TypeReunion type, int nombreDePersonnesConvieesSurSite) {
        return Reunion.builder().creneauxHoraire(creneauxHoraire).type(type)
                .nombreDePersonnesConvieesSurSite(nombreDePersonnesConvieesSurSite).build();
    }

    /**
     * Récupération des listes d'équipements nécessaires par type de réunion
     * @param typeReunion
     * @return
     */
    public List<TypeEquipement> listeEquipementsNecessairesParReunion(TypeReunion typeReunion) {
        List<TypeEquipement> listeEquipementsNecessaires = new ArrayList<>();
        switch (typeReunion) {
            case VC:
                listeEquipementsNecessaires = Stream.of(TypeEquipement.ECRAN, TypeEquipement.PIEUVRE, TypeEquipement.WEBCAM).collect(Collectors.toList());
                break;
            case SPEC:
                listeEquipementsNecessaires = Stream.of(TypeEquipement.TABLEAU).collect(Collectors.toList());
                break;
            case RS:
                listeEquipementsNecessaires = Stream.of(TypeEquipement.SALLE).collect(Collectors.toList());
                break;
            case RC:
                listeEquipementsNecessaires = Stream.of(TypeEquipement.TABLEAU, TypeEquipement.ECRAN, TypeEquipement.PIEUVRE).collect(Collectors.toList());
                break;
        }
        return listeEquipementsNecessaires;
    }

    /**
     * Initialisation des équipements supplémentaires à réserver par réunion
     * @return
     */
    public Map<TypeEquipement, ReservationEquipement> initmapEquipementsSupplementaires() {
        return Map.of(TypeEquipement.PIEUVRE, ReservationEquipement.builder().nombreRessources(4).build(),
                TypeEquipement.ECRAN, ReservationEquipement.builder().nombreRessources(5).build(),
                TypeEquipement.WEBCAM, ReservationEquipement.builder().nombreRessources(4).build(),
                TypeEquipement.TABLEAU, ReservationEquipement.builder().nombreRessources(2).build());
    }

    /**
     * Calcul des ressources disponibles pour la réunion
     * @param salleDejaDansMapAssociations
     * @param salle
     * @param reunion
     */
    public void gestionDesRessourcesDisponibles(boolean salleDejaDansMapAssociations, Salle salle, Reunion reunion) {
        // Type de réunion
        List<TypeEquipement> listeTypeEquipementNecessaires = listeEquipementsNecessairesParReunion(reunion.getType());
        // Ressources disponibles
        boolean allMatch = listeTypeEquipementNecessaires.stream().allMatch(salle.getListeEquipementsPresents()::contains);
        boolean anyMatch = listeTypeEquipementNecessaires.stream().anyMatch(salle.getListeEquipementsPresents()::contains);
        List<TypeEquipement> listeEquipementsManquants = listeTypeEquipementNecessaires.stream()
                .filter(element -> !salle.getListeEquipementsPresents().contains(element))
                .collect(Collectors.toList());
        listeSallesMatching.add(SalleMatching.builder().salleDejaDansMapAssociations(salleDejaDansMapAssociations)
                .nomSalle(salle.getNom()).allMatch(allMatch).anyMatch(anyMatch)
                .listeEquipementsManquants(listeEquipementsManquants).build());
    }

    /**
     * Association des salles aux réunions
     * @param reunion
     */
    public void gestionAssociationsSalleReunion(Reunion reunion) {
        if (!listeSallesMatching.isEmpty()) {
            Optional<SalleMatching> salleAllMatch = listeSallesMatching.stream().filter(salleMatching ->
                            salleMatching.isAllMatch())
                    .findFirst();
            Optional<SalleMatching> salleAnyMatch = listeSallesMatching.stream().filter(salleMatching ->
                            salleMatching.isAnyMatch())
                    .min(Comparator.comparing(SalleMatching::getNombreEquipementsManquants));
            Optional<SalleMatching> salleNotAnyMatch = listeSallesMatching.stream().filter(salleMatching ->
                            !salleMatching.isAnyMatch())
                    .findFirst();
            // Si tous les équipements de la salle matche avec les équipements nécessaires de la réunion
            if (salleAllMatch.isPresent()) {
                gestionAjoutReunionDansMapAssociations(salleAllMatch, reunion);
            // Si certains équipements de la salle matche avec les équipements nécessaires de la réunion,
            // prendre la salle ayant le plus d'équipements présents
            } else if (salleAnyMatch.isPresent()) {
                // Calcul si possibilité de réserver des équipements supplémentaires
                if (miseAJourmapEquipementsSupplementairesAccompli(reunion.getCreneauxHoraire(),
                        salleAnyMatch.get().getListeEquipementsManquants())) {
                    gestionAjoutReunionDansMapAssociations(salleAnyMatch, reunion);
                }
            // Si aucun équipement de la salle matche
            } else if (salleNotAnyMatch.isPresent()) {
                // Calcul si possibilité de réserver des équipements supplémentaires
                if (miseAJourmapEquipementsSupplementairesAccompli(reunion.getCreneauxHoraire(),
                        salleNotAnyMatch.get().getListeEquipementsManquants())) {
                    gestionAjoutReunionDansMapAssociations(salleNotAnyMatch, reunion);
                }
            }
        }
    }

    /**
     * Ajout des réunions dans la map d'association salle/réunion selon que la salle comporte déjà des réunions ou non
     * @param salleMatching
     * @param reunion
     */
    public void gestionAjoutReunionDansMapAssociations(Optional<SalleMatching> salleMatching, Reunion reunion) {
        if (salleMatching.get().isSalleDejaDansMapAssociations()) {
            mapAssociationsSalleReunion.get(salleMatching.get().getNomSalle()).add(reunion);
        } else {
            mapAssociationsSalleReunion.put(salleMatching.get().getNomSalle(),
                    Stream.of(reunion).collect(Collectors.toList()));
        }
    }

    /**
     * Calcul de la possibilité d'utiliser les équipements supplémentaires si des ressources sont encore disponibles
     * @param creaneauxHorairesReservation
     * @param listeEquipementsManquants
     * @return
     */
    public boolean miseAJourmapEquipementsSupplementairesAccompli(CreneauxHoraire creaneauxHorairesReservation,
                                                                    List<TypeEquipement> listeEquipementsManquants) {
        List<Boolean> listeRessourcesSontDisponibles = new ArrayList<>();
        listeEquipementsManquants.forEach(equipementManquant -> {
            for (Map.Entry mapentry : mapEquipementsSupplementaires.entrySet()) {
                if (equipementManquant.equals(mapentry.getKey())) {
                    ReservationEquipement verificationReservationEquipement = ((ReservationEquipement) mapentry.getValue());
                    // Si meme créneau horaire entre la demande de réservation et les réservations déjà en cours
                    if (memeCreneauxHoraires(verificationReservationEquipement, creaneauxHorairesReservation)) {
                        // Vérification des ressources disponibles
                        listeRessourcesSontDisponibles.add(ressourcesSontDisponibles(mapentry,
                                verificationReservationEquipement, creaneauxHorairesReservation));
                    } else {
                        // Vérification des ressources disponibles
                        listeRessourcesSontDisponibles.add(ressourcesSontDisponibles(mapentry,
                                verificationReservationEquipement, creaneauxHorairesReservation));
                    }
                }
            }
        });
        return listeRessourcesSontDisponibles.stream().allMatch(ressourcesSontDisponibles ->
                ressourcesSontDisponibles == true);
    }

    /**
     * Vérification si meme créneau horaire entre la demande de réservation et les réservations déjà en cours
     * @param verificationReservationEquipement
     * @param creaneauxHorairesReservation
     * @return
     */
    public boolean memeCreneauxHoraires(ReservationEquipement verificationReservationEquipement,
                                        CreneauxHoraire creaneauxHorairesReservation) {
        return (verificationReservationEquipement.getCreneauxHoraire() != null) &&
                (verificationReservationEquipement.getCreneauxHoraire().getDebut().intValue()
                        == creaneauxHorairesReservation.getDebut().intValue() &&
                        verificationReservationEquipement.getCreneauxHoraire().getFin().intValue()
                                == creaneauxHorairesReservation.getFin().intValue());
    }

    /**
     * Vérification de si des ressources sont encore disponibles, si oui, on décrémente la ressource qui sera utilisée
     * et mis à jour du créneau horaire pour réservation de l'équipement
     * @param mapentry
     * @param verificationReservationEquipement
     * @param creaneauxHorairesReservation
     * @return
     */
    public boolean ressourcesSontDisponibles(Map.Entry mapentry, ReservationEquipement verificationReservationEquipement,
                                             CreneauxHoraire creaneauxHorairesReservation) {
        if (verificationReservationEquipement.getNombreRessources() != 0) {
            mapEquipementsSupplementaires.get(mapentry.getKey()).setNombreRessources(verificationReservationEquipement
                    .getNombreRessources() - 1);
            mapEquipementsSupplementaires.get(mapentry.getKey()).setCreneauxHoraire(creaneauxHorairesReservation);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vérification de s'il y a chevauchement des créneaux horaires entre les réunions déjà réservées dans la salle
     * et celle en question
     * @param reunion
     * @param listeReunionDejaPlanifieeDansSalle
     * @return
     */
    public boolean horairesSeChevauchent(Reunion reunion, List<Reunion> listeReunionDejaPlanifieeDansSalle) {
        List<Boolean> listeHorairesSeChevauchent = new ArrayList<>();
        listeReunionDejaPlanifieeDansSalle.forEach(reunionDejaPlanifieeDansSalle -> {
            listeHorairesSeChevauchent.add((reunion.getCreneauxHoraire().getFin().intValue()
                    == reunionDejaPlanifieeDansSalle.getCreneauxHoraire().getDebut().intValue()) ||
                    (reunion.getCreneauxHoraire().getDebut().intValue()
                            == reunionDejaPlanifieeDansSalle.getCreneauxHoraire().getDebut().intValue() &&
                            reunion.getCreneauxHoraire().getFin().intValue()
                                    == reunionDejaPlanifieeDansSalle.getCreneauxHoraire().getFin().intValue()) ||
                    (reunion.getCreneauxHoraire().getDebut().intValue()
                            == reunionDejaPlanifieeDansSalle.getCreneauxHoraire().getFin().intValue()));
        });
        return listeHorairesSeChevauchent.stream().anyMatch(HorairesSeChevauchent -> HorairesSeChevauchent == true);
    }
}