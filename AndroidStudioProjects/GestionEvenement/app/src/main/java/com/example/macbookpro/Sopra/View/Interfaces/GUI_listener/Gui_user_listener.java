package com.example.macbookpro.Sopra.View.Interfaces.GUI_listener;

/**
 * Created by macbookpro on 01/01/2016.
 */
import com.example.macbookpro.Sopra. Models.*;
import java.util.ArrayList;


public interface Gui_user_listener {

    /**
     * Choix d'un site de la part de l'user.
     * @param site Le site choisi.
     */
    public void perform_choisir_site_user(Site site);

    /**
     * Rechercher une salle avec les bonnes caracteristiques.
     * @param site  Le site de l'user.
     * @param creneau_voulu Le creneau voulu, null si pas de preference.
     * @param options_voulues   Les options voulues.
     * @param nb_personnes Le nombre de personnes.
     */
    public void perform_recherche_salle(Site site, Creneau creneau_voulu,
                                        Options options_voulues, int nb_personnes);


    public void reserver_salle(Salle salle, Creneau creneau, String objet);
}
