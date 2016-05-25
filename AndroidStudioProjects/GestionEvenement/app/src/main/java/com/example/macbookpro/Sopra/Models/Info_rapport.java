package com.example.macbookpro.Sopra.Models;

/**
 * Created by jason on 04/12/15.
 */
public class Info_rapport {
    short taux_occupation_journee;
    int nombre_requetes_journee;
    int nombre_connexions_journee;

    /**
     * Constructeur.
     * @param taux_occupation_journee
     * @param nombre_requetes_journee
     * @param nombre_connexions_journee
     */
    public Info_rapport(short taux_occupation_journee, int nombre_requetes_journee, int nombre_connexions_journee) {
        this.taux_occupation_journee = taux_occupation_journee;
        this.nombre_requetes_journee = nombre_requetes_journee;
        this.nombre_connexions_journee = nombre_connexions_journee;
    }

    /**
     * Constructeur utilise dans l'API.
     * @param nombre_requetes_journee
     * @param nombre_connexions_journee
     */
    public Info_rapport(int nombre_requetes_journee, int nombre_connexions_journee) {
        this.nombre_requetes_journee = nombre_requetes_journee;
        this.nombre_connexions_journee = nombre_connexions_journee;
    }

    public short getTaux_occupation_journee() {
        return taux_occupation_journee;
    }
    
    public void setTaux_occupation_journee(short taux_occupation_journee) {
        this.taux_occupation_journee = taux_occupation_journee;
    }

    public int getNombre_requetes_journee() {
        return nombre_requetes_journee;
    }

    public int getNombre_connexions_journee() {
        return nombre_connexions_journee;
    }

    /**
     *  Methode toString.
     * @return
     */
    public String toString() {
        return "* taux d'occupation ce jour là : "+this.taux_occupation_journee+"%\n"+
               "* nombres de connexions : "+this.nombre_connexions_journee+"\n"+
               "* nombre de requetes : "+this.nombre_requetes_journee;
    }
}
