package com.example.macbookpro.Sopra.View.Interfaces.GUI_listener;

/**
 * Created by macbookpro on 01/01/2016.
 */

import com.example.macbookpro.Sopra.Models.*;


public interface Gui_admin_listener {


    /**
     * Choix d'un site de la part de l'admin.
     * @param site Le site choisi.
     */
    public void perform_choisir_site_admin(Site site);


    /**
     * Modifier un site.
     * @param ancien_site
     * @param nouveau_site
     */
    public void perform_modif_site(Site ancien_site, Site nouveau_site);

    /**
     * Ajouter un site.
     * @param nouveau_site
     */
    public void perform_ajout_site(Site nouveau_site);

    /**
     * Supprimer un site.
     * @param site
     */
    public void perform_supprimer_site(Site site);

    /**
     * Generer le rapport pour chaque salle du site.
     * @param site
     */
    public void perform_generer_rapport(Site site, Date_model dateModel);

    /**
     * Modifier une salle.
     * @param ancienne_salle
     * @param nouvelle_salle
     */
    public void perform_modif_salle(Salle ancienne_salle, Salle nouvelle_salle);

    /**
     * Ajouter une salle.
     * @param site
     * @param nouvelle_salle
     */
    public void perform_ajout_salle(Site site, Salle nouvelle_salle);

    /**
     * Supprimer une salle.
     * @param salle
     */
    public void perform_supprimer_salle(Site site, Salle salle);

}
