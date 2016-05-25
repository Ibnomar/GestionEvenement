package com.example.macbookpro.Sopra.Controller;


    import com.example.macbookpro.Sopra.API.API_BDD;
    import com.example.macbookpro.Sopra.API.API_Implemented;
    import com.example.macbookpro.Sopra.Models.Date_model;
    import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_admin_listener;
    import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_login_listener;
    import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_user_listener;
    import com.example.macbookpro.Sopra.Models.Creneau;
    import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;
    import com.example.macbookpro.Sopra.Models.Options;
    import com.example.macbookpro.Sopra.Models.Salle;
    import com.example.macbookpro.Sopra.Models.Site;
    import com.example.macbookpro.Sopra.Models.Utilisateur;

    import java.sql.SQLException;
    import java.util.ArrayList;

    /**
     * Classe a instancier en premier lors du lancement de l'application.
     *
     * @author jason
     */
    public class Controller implements  Gui_login_listener,
                                        Gui_user_listener,
                                        Gui_admin_listener {

        // Singleton cree des le demarrage.
        private static Controller instance_controller;

        // Mon API implementee.
        private API_BDD my_apiBDD;

        // Utilisateur actuel
        private Utilisateur utilisateur_actuel;

        // liste des sites et des salles => partagees avec la GUI
        Mes_listes_sites_salles mes_listes;

        // information pour le debut et la fin de la journee
        int debut_journee = 8;
        int fin_journee = 20;

        /**
         * Constructeur prive car singleton,
         * C'est ici qu'on defini/cree l'API implementee.
         */
        private Controller() {
            // Recuperer l'API_BDD
            this.my_apiBDD = API_Implemented.get_API_BDD();
            // Recuperer l'instance de mes_listes
            mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();
        }


        //=================== Methodes singleton ===================================

        /**
         * Obtenir le controller.
         *
         * @return controller.
         */
        public static Controller get_controller() {
            if (instance_controller == null)
                instance_controller = new Controller();
            return instance_controller;
        }

        /**
         * Obtenir le gui_login_listener.
         *
         * @return gui_login_listener
         */
        public static Gui_login_listener get_login_listener() {
            if (instance_controller == null)
                instance_controller = new Controller();
            return (Gui_login_listener) instance_controller;
        }

        /**
         * Obtenir le gui_admin_listener.
         *
         * @return gui_login_listener
         */
        public static Gui_admin_listener get_admin_listener() {
            if (instance_controller == null)
                instance_controller = new Controller();
            return (Gui_admin_listener) instance_controller;
        }

        /**
         * Obtenir le gui_user_listener.
         *
         * @return gui_user_listener
         */
        public static Gui_user_listener get_user_listener() {
            if (instance_controller == null)
                instance_controller = new Controller();
            return (Gui_user_listener) instance_controller;
        }


        //==========================================================================
        //==========================================================================
        // =========== Implementation : Gui_login_listener =========================

        /**
         * Demande de connexion de la part du GUI login, verifier avec l'identification
         * avec l'API et faire sur le output_login connectOK_user si ok, connectFaux sinon.
         *
         * @param pseudo
         * @param password
         * @param admin true si on veut se connecter en tant qu'admin.
         */
        @Override
        public boolean perform_connect(String pseudo, String password, boolean admin) {
            Site ancien_site = null;
            boolean connexion_reussie = false;
            try {
                if (admin) {
                    System.out.println("**** Tentative de connexion en admin");
                } else {
                    System.out.println("**** Tentative de connexion en user");
                }
                if (my_apiBDD.user_existe(pseudo, password, admin)) {
                    // ===== L'identification user a reussi =====
                    System.out.println("**** tentative effectuee et reussie");
                    // 1 - On recupere la liste des sites en utilisant l'API
                    this.mes_listes.set_liste_sites(my_apiBDD.get_list_sites());
                    // 2 - On recupere l'ancien site de l'utilisateur si il en avait un
                    ancien_site = my_apiBDD.get_ancien_site(pseudo);
                    mes_listes.setSite_aux(ancien_site);
                    // 3 - On fixe le pseudo utilise dans le controller
                    this.utilisateur_actuel = new Utilisateur(pseudo, ancien_site);
                    // 4 - On met la connexion reussie a true
                    connexion_reussie = true;
                } else {
                    // ===== L'identification a echoue =====
                    System.out.println("**** tentative effectuee et echouee");
                }
            } catch (SQLException e) {
                System.out.println("**** SQLException pendant la tentative de connexion user");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("**** ClassNotFoundException pendant la tentative de connexion user");
                e.printStackTrace();
            }
            return connexion_reussie;
        }

        //==========================================================================
        // =========== Implementation : Gui_user_listener ==========================

        /**
         * Lors d'une connection, assigne un site a un utilisateur/admin, qui sera
         * choisi propose par defaut lors de sa prochaine connexion.
         * @param site
         */
        @Override
        public void perform_choisir_site_user(Site site) {
            try {
                my_apiBDD.update_site_user(this.utilisateur_actuel.getPseudo(), site);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Proceder a la recherche de salles disponibles avec les parametres voulus.
         *
         * @param site        Le nom du site ou on recherche les salles.
         * @param creneau_voulu   Le creneau voulu, null si pas de preference.
         * @param options_voulues La liste des options voulues.
         * @return La liste des salles disponibles avec les options et le ou les bons
         * creneaux libres.
         */
        @Override
        public void perform_recherche_salle(Site site, Creneau creneau_voulu,
                                            Options options_voulues, int nb_personnes) {
            // Var : contient les salles avec les bonnes options
            ArrayList<Salle> liste_salles_recherche;
            // Var : contient les salles qui correspondent au bon creneau
            ArrayList<Salle> liste_salles_correspondantes_creneau = new ArrayList<>();

            // Var : contient la liste des reservation d'une salle
            ArrayList<Creneau> liste_reservations_salle;
            // Var : contient la liste des creneaux disponibles pour une salle
            ArrayList<Creneau> liste_creneaux_dispo;

            // Var : contient si le creneau est specifie, un creneau libre correspondant
            Creneau creneau_libre;

            // On effecture la recherche des salles avec l'API
            liste_salles_recherche = my_apiBDD.get_list_salles(site.getNom_site(), options_voulues);

            System.out.println("TEST123 : on a trouve "+liste_salles_recherche.size()+" salles avec les options");
            System.out.println("------- Options : "+options_voulues);
            if (!creneau_voulu.isCreneauNull()) {
                // On verifie le creneau pour chaque salle ayant les bonnes options
                for (Salle salle : liste_salles_recherche) {
                    System.out.println("------- Salle : "+salle.getNom_salle());
                    salle.get_liste_creneaux().clear();
                    if (salle.getNombre_personne_max() > nb_personnes) {
                        System.out.println("Nombre de personnes voulues : "+nb_personnes+" et max : "+salle.getNombre_personne_max());
                        // On obtient la liste de toutes les reservations de la salle
                        liste_reservations_salle = my_apiBDD.get_liste_reservations(salle.getNom_salle(),
                                creneau_voulu.getDateModel());
                        // On regarde si elle a un creneau libre correspondant
                        creneau_libre = Creneau.get_creneau_libre(liste_reservations_salle,
                                debut_journee, fin_journee,
                                creneau_voulu);
                        // Si on a un creneau libre
                        if (creneau_libre != null && salle.getNombre_personne_max() > nb_personnes) {
                            System.out.println(" ** Controller, salle trouvee : " + salle);
                            // on l'ajoute aux caracteristiques de la salle
                            salle.add_liste_creneaux(creneau_libre);
                            liste_salles_correspondantes_creneau.add(salle);
                        } else {
                            System.out.println(" ** Controller, salle non disponible : " + salle);
                        }
                    }
                } // fin de la verification pour toutes les salles
            // Si on a pas specifie de creneau, on renvoie toutes les salles
            } else {
                System.out.println("Controller, recherche salles ,creneau null -> On donne tout");
                for (Salle salle : liste_salles_recherche) {
                    salle.get_liste_creneaux().clear();
                    if (salle.getNombre_personne_max() > nb_personnes) {
                        // On obtient la liste de toutes les reservations de la salle
                        liste_reservations_salle = my_apiBDD.get_liste_reservations(salle.getNom_salle(),
                                creneau_voulu.getDateModel());
                        // On recupere tous les creneaux libres de la salle
                        liste_creneaux_dispo = Creneau.get_liste_tous_creneaux_dispo(
                                                liste_reservations_salle,
                                                debut_journee, fin_journee,
                                                creneau_voulu.getDateModel());
                        // On les met dans les caracteristiques de la salle
                        salle.set_liste_creneaux(liste_creneaux_dispo);
                        liste_salles_correspondantes_creneau.add(salle);
                    }
                }
            }
            // retourner la liste des salles recherchees -> Update de mes_listes
            mes_listes.set_liste_salles(liste_salles_correspondantes_creneau);
            System.out.println("TEST123 : on a trouve " + mes_listes.get_liste_salles().size() + " salles libres");
        }

        /**
         * Reserver une salle.
         * @param salle
         * @param creneau
         * @param objet
         */
        @Override
        public void reserver_salle(Salle salle, Creneau creneau, String objet) {
            System.out.println("**** On reserve : "+salle+" pour le creneau : \n"+creneau);
            my_apiBDD.reserver_salle(salle.getNom_salle(), creneau, objet, utilisateur_actuel.getPseudo());
            System.out.println("**** Reservation faite !");
        }

        //==========================================================================
        // =========== Implementation : Gui_admin_listener =========================

        /**
         * Choix d'un site a administrer de l'admin.
         * @param site Le site choisi.
         */
        @Override
        public void perform_choisir_site_admin(Site site) {
            // On update le site pour la prochaine connexion de l'user
            try {
                this.my_apiBDD.update_site_user(this.utilisateur_actuel.getPseudo(),site);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // On actualise alors la liste des salles
            mes_listes.set_liste_salles( my_apiBDD.get_list_salles(site.getNom_site()) );
        }

        /**
         * Proceder a la modification d'un site.
         *
         * @param ancien_site
         * @param nouveau_site
         */
        @Override
        public void perform_modif_site(Site ancien_site, Site nouveau_site) {
            try {
                my_apiBDD.update_site(ancien_site, nouveau_site);
                mes_listes.set_liste_sites(my_apiBDD.get_list_sites());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Proceder a l'ajout d'un site.
         *
         * @param nouveau_site
         */
        @Override
        public void perform_ajout_site(Site nouveau_site) {
            System.out.println("Controller - admin : Ajout d'un site");
            my_apiBDD.add_site(nouveau_site);
            mes_listes.set_liste_sites(my_apiBDD.get_list_sites());
            System.out.println("Controller - admin : site ajoute");
        }

        /**
         * Proceder a la suppresion d'un site.
         *
         * @param site
         */
        @Override
        public void perform_supprimer_site(Site site) {
            my_apiBDD.supprimer_site(site);
            mes_listes.set_liste_sites( my_apiBDD.get_list_sites() );
        }

        /**
         * Proceder a la generation du rapport -> calcul des taux d'occupation.
         *
         * @param site
         */
        @Override
        public void perform_generer_rapport(Site site, Date_model dateModel) {
            System.out.println("Controller : Generation du rapport sur le site : "+site.getNom_site());
            ArrayList<Creneau> liste_reservations;
            short taux_occupation_actuel;
            // Les salles, de la liste des salles d'un site dans mes_listes,
            // n'ont que le taux d'occupation qui manque.
            this.mes_listes.set_liste_salles( my_apiBDD.get_list_salles(site.getNom_site()) );
            // On calcule ce taux d'occupation pour chacune des salles.
            for (Salle salle_actuelle : this.mes_listes.get_liste_salles()) {
                System.out.println("Controller, rapport de la salle : "+salle_actuelle.getNom_salle());
                // Obtention de la liste des reservations de la salle
                liste_reservations = my_apiBDD.get_liste_reservations(salle_actuelle.getNom_salle(),
                                                                        dateModel);
                // Calcul du taux d'occupation
                if( liste_reservations.isEmpty() ) {
                    System.out.println(" --- Liste de reservation null pour cette salle");
                    taux_occupation_actuel = 0;
                } else {
                    Creneau reservation_1 = liste_reservations.get(0);
                    System.out.println(" --- La reservation de ref est : \n"+reservation_1);
                    taux_occupation_actuel = Creneau.calcul_taux_occupation(liste_reservations,
                            this.debut_journee, this.fin_journee, reservation_1.getDateModel());
                }
                // Ajout de ce taux au rapport associe a la salle actuelle
                System.out.println("Controller, Info rapport : "+salle_actuelle.getInfo_rapport());
                salle_actuelle.getInfo_rapport().setTaux_occupation_journee(taux_occupation_actuel);
                System.out.println("Controller, Info rapport apres : " + salle_actuelle.getInfo_rapport());
            }
            System.out.println("Controller : Fin de la generation du rapport ");
        }

        /**
         * Proceder a la modification d'une salle.
         *
         * @param ancienne_salle
         * @param nouvelle_salle
         */
        @Override
        public void perform_modif_salle(Salle ancienne_salle, Salle nouvelle_salle) {
            try {
                my_apiBDD.update_salle(ancienne_salle, nouvelle_salle);
                mes_listes.set_liste_salles( my_apiBDD.get_list_salles(mes_listes.getSite_aux()
                                                                                .getNom_site()) );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Proceder a l'ajout d'une salle.
         *
         * @param site
         * @param nouvelle_salle
         */
        @Override
        public void perform_ajout_salle(Site site, Salle nouvelle_salle) {
            try {
                my_apiBDD.add_salle(site, nouvelle_salle);
                mes_listes.set_liste_salles( my_apiBDD.get_list_salles(site.getNom_site()) );
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * Proceder a la suppression d'une salle.
         *
         * @param salle
         */
        @Override
        public void perform_supprimer_salle(Site site, Salle salle) {
            my_apiBDD.supprimer_salle(salle);
            mes_listes.set_liste_salles( my_apiBDD.get_list_salles(site.getNom_site()) );
        }

    }

