package com.example.macbookpro.Sopra.Models;

/**
 * Created by jason on 04/12/15.
 */
public class Site {

    private String nom_site;

    /**
     * Constructeur.
     * @param nom_site
     */
    public Site(String nom_site) {
        this.nom_site = nom_site;
    }

    /**
     * Getter nom_site
     * @return
     */
    public String getNom_site() { return nom_site; }
    /**
     * Equals.
     * @param site_a_comparer
     * @return
     */
    public boolean equals(Site site_a_comparer) {
        boolean est_egal = false;
        if (this.nom_site.equals(site_a_comparer.getNom_site())) {
            est_egal = true;
        }
        return est_egal;
    }

    /**
     * Methode toString.
     * @return
     */
    public String toString() {
        return this.nom_site;
    }

}
