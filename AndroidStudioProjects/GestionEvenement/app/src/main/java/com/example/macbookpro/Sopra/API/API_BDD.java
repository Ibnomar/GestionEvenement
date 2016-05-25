package com.example.macbookpro.Sopra.API;

import com.example.macbookpro.Sopra.Models.*;

import java.sql.SQLException;
import java.util.ArrayList;



public interface API_BDD {

    // POUR LA RECUPERATION DE L'INTANCE DE L'API !!!
    /**
     * Ne pas oublier de creer la methode singleton : public static API_BDD get_API_BDD()
     * Afin de pouvoir recuperer l'instance de l'API_BDD.
     */

    //==========================================================================
    //================== IDENTIFICATION ========================================
    
    /**
     * Verifier si l'utilisateur existe dans le base de données, l'admin peut aussi etre considere comme
     * un User.
     * @param pseudo
     * @param password
     * @param is_admin
     * @return true si l'utilisateur existe dans la BDD.
     */
    public boolean user_existe(String pseudo, String password, Boolean is_admin);

  
    //==========================================================================
    //================== Gerer profil ==========================================
    
    /**
     * Retourner le site auquel etait rattache l'utilisateur ou l'admin lors de sa derniere connexion.
     * @param pseudo
     * @return le site auquel il etait connecte avant, null si c'est sa premiere connexion.
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    public Site get_ancien_site(String pseudo) throws SQLException, ClassNotFoundException;

    /**
     * Modifier le site auquel est associe l'utilisateur.
     * @param pseudo
     * @param site
     * @throws SQLException 
     */
    public void update_site_user(String pseudo, Site site) throws SQLException;

    //==========================================================================
    //================== Liste des sites =======================================
    
    /**
     * Obtenir la liste des sites dans notre BDD.
     * @return une ArrayList de tous les sites.
     */
    public ArrayList<Site> get_list_sites();

    
    //==========================================================================
    //================== Liste des salles ======================================
    
    /**
     * Utilisation ADMIN :
     * Obtenir la liste de toutes les salles d'un site dans notre BDD avec :
     * - Parametre generaux (nom_salle, options, nombre_max_personnes)
     * - Info_rapport specifie : Pas besoin de specifier le taux_occupation_journee,
     *                          il sera calcule par l'application
     * - creneau_disponible -> Pas besoin de le specifier
     * @param nomSite
     * @return une ArrayList de toutes les salles du site.
     */
    public ArrayList<Salle> get_list_salles(String nomSite);

    /**
     * Utilisation USER :
     * Obtenir la liste de toutes les salles disponibles d'un site dans notre BDD avec :
     * - Parametre generaux (nom_salle, options, nombre_max_personnes)
     * - Info_rapport -> pas besoin de le specifier
     * - creneau_disponible -> Pas besoin de le specifier
     * @param options_voulues
     * @return une ArrayList de salles disponibles.
     */
    public ArrayList<Salle> get_list_salles(String nomSite, Options options_voulues);

    
    //==========================================================================
    //=============== Information/modification d'une salle =====================
    
    /**
     * Obtenir les options d'une salle.
     * @param nomSalle
     * @return les Options de la salle.
     */
    public Options get_options(String nomSalle);

    /**
     * Modifier les parametres d'une salle dans notre BDD.
     * @param ancienne_salle
     * @param nouvelle_salle    Dans la nouvelle salle, seuls les parametres nom_salle, options,
     *                          nombre_personne_max sont specifies.
     * @throws SQLException 
     */
    public void update_salle(Salle ancienne_salle, Salle nouvelle_salle) throws SQLException;

    //==========================================================================
    //================== ajout/suppression d'une salle  ========================
    
    /**
     * Ajouter une salle a un site.
     * @param site le site auquel ajouter la salle.
     * @param nouvelle_salle la nouvelle salle a ajouter.
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    public void add_salle(Site site, Salle nouvelle_salle) throws SQLException, ClassNotFoundException;
    
    /**
     * Supprimer une salle.
     * @param salle salle a supprimer.
     */
    public void supprimer_salle(Salle salle);
    
    //==========================================================================
    //================== Modification/ajout d'un site ==========================
    
    /**
     * Modifier un site (son nom) dans notre BDD.
     * @param ancien_site
     * @param nouveau_site
     * @throws SQLException 
     */
    public void update_site(Site ancien_site, Site nouveau_site) throws SQLException;

    /**
     * Ajouter un site dans notre BDD.
     * @param site nouveau site.
     */
    public void add_site(Site site);
    
    /**
     * Supprimer un site dans notre BDD.
     * @param site site a supprimer.
     */
    public void supprimer_site(Site site);
    
    //==========================================================================
    //================== Liste des reservations d'une salle ====================
    
    /**
     * Obtenir la liste des reservations d'une salle pour la journee en cours,
     * une reservation est caracterisee par un creneau.
     * 
     * @param salle
     * @return La liste des reservations (creneaux occupes) d'une salle aujourd'hui.
     */
    public ArrayList<Creneau> get_liste_reservations(String nomSalle, Date_model date_model_voulue);


    //==========================================================================
    //================== Reserver une salle ====================================

    /**
     * Reserver une salle/
     * @param nom_salle
     * @param reservation
     */
	public void reserver_salle(String nom_salle, Creneau reservation, String objet, String nom_personne);

}
