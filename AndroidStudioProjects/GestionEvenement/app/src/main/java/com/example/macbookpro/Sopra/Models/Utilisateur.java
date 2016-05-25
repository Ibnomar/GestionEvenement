package com.example.macbookpro.Sopra.Models;

/**
 * Created by jason on 04/12/15.
 */
public class Utilisateur {
    // CHAMPS
    private String pseudo;
    private Site site_associe;

    // CONSTRUCTEUR
    public Utilisateur(String pseudo, Site site_associe) {
        this.pseudo = pseudo;
    }

    // GETTERS
    public String getPseudo() {
        return this.pseudo;
    }
    
    public Site getSite() {
        return this.site_associe;
    }
   
    // SETTERS
    public void setSite(Site nouveau_site) {
        this.site_associe = nouveau_site;
    }
    
    // TO STRING
    public String toString() {
        return pseudo;
    }
}
