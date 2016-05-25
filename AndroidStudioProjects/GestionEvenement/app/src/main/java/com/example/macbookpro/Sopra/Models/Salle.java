package com.example.macbookpro.Sopra.Models;

import java.util.ArrayList;

/**
 * Created by jason on 04/12/15.
 */
public class Salle {
    // Parametre modifiables par l'admin.
    String nom_salle;
    Options options;
    short nombre_personnes_max;
    // Parametre actualise lors de l'utilisation admin et generation du rapport.
    Info_rapport info_rapport;
    // Parametre calcule par le controller.
    ArrayList<Creneau> liste_creneaux;
    // Variable pour le GUI
    Creneau creneau_choisi;

    //==========================================================================

    /**
     * ----- GUI -----
     * Constructeur utilise lors de l'ajout d'une nouvelle salle dans le gui.
     * @param nom_salle
     */
    public Salle(String nom_salle) {
        this.nom_salle = nom_salle;
        this.options = new Options(false,false,false,false);
        this.nombre_personnes_max = 10;
        this.liste_creneaux = new ArrayList<>();
    }

    /**
     * ----- GUI -----
     * N1 :
     * Constructeur utilise lors de la modification d'une salle dans le gui.
     * @param salle_a_copier
     */
    public Salle(Salle salle_a_copier) {
        this.nom_salle = salle_a_copier.getNom_salle();
        this.options = salle_a_copier.getOptions();
        this.nombre_personnes_max = salle_a_copier.getNombre_personne_max();
        this.liste_creneaux = salle_a_copier.get_liste_creneaux();
    }

    //
    /**
     * ----- API -----
     * N2 :
     * Constructeur a utiliser dans l'API, get_list_salles() en user.
     * @param nom_salle
     * @param options
     * @param nombre_personnes_max
     */
    public Salle(String nom_salle,Options options,short nombre_personnes_max) {
        this.nom_salle = nom_salle;
        this.options = options;
        this.nombre_personnes_max = nombre_personnes_max;
        this.liste_creneaux = new ArrayList<>();
    }

    /**
     * ----- API -----
     * N3 :
     * Constructeur a utiliser dans l'API, get_list_salles() en admin.
     * generation d'un rapport.
     * @param nom_salle
     * @param options
     * @param nombre_personnes_max
     * @param info_rapport
     */
    public Salle(String nom_salle, Options options, short nombre_personnes_max,
                 Info_rapport info_rapport ) {
        this.nom_salle = nom_salle;
        this.options = options;
        this.nombre_personnes_max = nombre_personnes_max;
        liste_creneaux = new ArrayList<>();
        this.info_rapport = info_rapport;
    }

    //==============================================================================================

    // GETTERS :
    public String getNom_salle() { return nom_salle; }

    public Options getOptions() {
        return options;
    }

    public Info_rapport getInfo_rapport() {
        return info_rapport;
    }

    public short getNombre_personne_max() {
        return nombre_personnes_max;
    }

    public Creneau getCreneau_choisi() { return this.creneau_choisi; }

    public ArrayList<Creneau> get_liste_creneaux() { return this.liste_creneaux; }

    //==============================================================================================

    // SETTERS :

    public void set_liste_creneaux(ArrayList<Creneau> liste_creneaux) {
        this.liste_creneaux = liste_creneaux;
    }
    
    public void add_liste_creneaux(Creneau creneau_a_ajouter) {
        this.liste_creneaux.add(creneau_a_ajouter);
    }

    public void setNom_salle(String nouveau_nom_salle) {
        this.nom_salle = nouveau_nom_salle;
    }

    public void setNombre_personnes_max(short nombre_personnes_max) {
        this.nombre_personnes_max = nombre_personnes_max;
    }

    public void setOptions(Options nouvelles_options) {
        this.options = nouvelles_options;
    }

    public void setCreneau_choisi(Creneau creneau_choisi) { this.creneau_choisi = creneau_choisi; }

    /**
     * Methode toString.
     * @return
     */
    public String toString() {
        return "nom : "+nom_salle+"\nnombre de personnes max. : "+nombre_personnes_max+"\noptions -- \n"+options;
    }
}
