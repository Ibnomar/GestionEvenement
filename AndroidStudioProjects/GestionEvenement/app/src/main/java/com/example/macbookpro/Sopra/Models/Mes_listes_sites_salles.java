/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.macbookpro.Sopra.Models;

import java.util.ArrayList;

/**
 * Contient la liste de tous les sites et toutes les salles.
 * @author jason
 */
public class Mes_listes_sites_salles {
    ArrayList<Site> liste_sites;
    ArrayList<Salle> liste_salles;

    // Variables utilises pour transferer ces infos dans le gui
    // La methode n'est pas correcte ou bonne, mais elle est plus facile a mettre en oeuvre ici.
    private Site site_aux;
    private Salle salle_aux;
    private Creneau creneau_1;
    private boolean reservation_faite;
    
    // Singleton
    private static Mes_listes_sites_salles ma_liste;
    
    //==========================================================================
    //==========================================================================
    /**
     * Constructeur.
     */
    private Mes_listes_sites_salles() {
        this.liste_sites = new ArrayList<>();
        this.liste_salles = new ArrayList<>();
        this.site_aux = null;
        this.salle_aux = null;
        this.creneau_1 = null;
        this.reservation_faite = false;
    }
    
    //==========================================================================
    //==========================================================================
    //Singleton
    public static Mes_listes_sites_salles get_listes_sites_salles() {
        if (ma_liste == null) {
            ma_liste = new Mes_listes_sites_salles();
        }
        return ma_liste;
    }
    
    //==========================================================================
    //==========================================================================
    // SETTERS
    /**
     * Setter pour la liste des sites.
     * @param liste_sites 
     */
    public void set_liste_sites(ArrayList<Site> liste_sites) {
        this.liste_sites = liste_sites;
    }
    
    /**
     * Setter pour la liste des salles.
     * @param liste_salles
     */
    public void set_liste_salles(ArrayList<Salle> liste_salles) {
        this.liste_salles = liste_salles;
    }
    
    //==========================================================================
    //==========================================================================
    // GETTERS
    /**
     * Getter pour la liste des sites.
     * @return La liste des sites.
     */
    public ArrayList<Site> get_liste_sites() {
        return this.liste_sites;
    }
    
    /**
     * Getter pour la liste des salles.
     * @return La liste des salles
     */
    public ArrayList<Salle> get_liste_salles() {
        return this.liste_salles;
    }


    //==========================================================================
    //================== Methodes pour les GUI =================================

    /**
     * Retourne un site parmi la liste de sites.
     * @param nom_site
     * @return Le site correspondant.
     */
    public Site get_site( String nom_site) {
        Site site_correspondant = null;
        boolean trouve = false;
        for (Site site : liste_sites) {
            // Si on trouve le site avec le bon nom
            if (!trouve) {
                if (site.getNom_site().equals(nom_site)) {
                    site_correspondant = site;
                    trouve = true;
                }
            }
        }
        return site_correspondant;
    }

    /**
     * Retourne une salle parmi la liste de salles.
     * @param nom_salle
     * @return La salle correspondante.
     */
    public Salle get_salle(String nom_salle) {
        Salle salle_correspondante = null;
        boolean trouve = false;
        for (Salle salle : liste_salles) {
            // Si on trouve le site avec le bon nom
            if (!trouve) {
                System.out.println("MES_listes : "+salle.getNom_salle()+" et "+nom_salle);
                if (salle.getNom_salle().equals(nom_salle)) {
                    salle_correspondante = salle;
                    trouve = true;
                }
            }
        }
        return salle_correspondante;
    }

    //==============================================================================================
    //==============================================================================================

    /**
     * Retourne le site aux.
     * @return
     */
    public Site getSite_aux() {
        return site_aux;
    }

    /**
     * Retourner la position du site aux dans la liste de sites.
     * @return la position, -1 si le site n'est pas dans la liste.
     */
    public int get_position_Site_aux() {
        boolean trouve = false;
        int position = -1;
        int i = 0;
        for (Site site : liste_sites) {
            if (!trouve) {
                System.out.println("TEST_SITE : "+site.getNom_site()+ " et "+site_aux.getNom_site());
                if (site.getNom_site().equals(site_aux)) {
                    trouve = true;
                    position = i;
                }
            }
            i++;
        }
        return position;
    }

    /**
     * Setter du site aux.
     * @param site_aux
     */
    public void setSite_aux(Site site_aux) {
        this.site_aux = site_aux;
    }

    /**
     * Obtenir la salle aux.
     * @return lea salle aux.
     */
    public Salle getSalle_aux() {
        return salle_aux;
    }

    /**
     * Setter de la salle aux.
     * @param salle_aux
     */
    public void setSalle_aux(Salle salle_aux) {
        this.salle_aux = salle_aux;
    }

    public Creneau getCreneau_1() {return creneau_1;}

    public void setCreneau_1(Creneau creneau_1) {this.creneau_1 = creneau_1;}

    public boolean isReservation_faite() {
        return reservation_faite;
    }

    public void setReservation_faite(boolean reservation_faite) {
        this.reservation_faite = reservation_faite;
    }
}

