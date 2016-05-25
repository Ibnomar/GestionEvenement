package com.example.macbookpro.Sopra.Models;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jason on 04/12/15.
 */
public class Creneau implements Comparable<Creneau>{

    private short heure_debut;
    private short minute_debut;

    private short heure_fin;
    private short minute_fin;

    private Date_model dateModel;

    //==========================================================================
    //==========================================================================

    /**
     * CONSTRUCTEUR
     * @param heure_deb
     * @param minute_deb
     * @param heure_fin
     * @param minute_fin
     * @param dateModel
     */
    public Creneau(short heure_deb, short minute_deb, short heure_fin,
                   short minute_fin, Date_model dateModel) {
        this.heure_debut = heure_deb;
        this.minute_debut = minute_deb;
        this.heure_fin = heure_fin;
        this.minute_fin = minute_fin;
        this.dateModel = dateModel;
    }

    public Creneau(Date_model dateModel) {
        this.heure_debut = (short)0;
        this.minute_debut = (short)0;
        this.heure_fin = (short)0;
        this.minute_fin = (short)0;
        this.dateModel = dateModel;
    }

    //==========================================================================
    //==========================================================================

    // GETTERS
    /**
     * @return l'heure de debut du creneau
     */
    public short getHeure_debut() { return heure_debut; }
    /**
     * @return la minute de debut du creneau
     */
    public short getMinute_debut() { return minute_debut; }
    /**
     * @return l'heure de fin du creneau
     */
    public short getHeure_fin() { return heure_fin; }
    /**
     * @return la minute de fin du creneau
     */
    public short getMinute_fin() {
        return minute_fin;
    }

    /**
     * obtenir la dateModel.
     * @return la dateModel.
     */
    public Date_model getDateModel() { return this.dateModel; }

    public boolean isCreneauNull() {
        if (this.heure_debut == (short)0 && this.heure_fin == (short)0 &&
                this.minute_debut == (short)0 && this.minute_fin == (short)0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Comparer le temps des creneaux et retourner :
     * * 1 si le creneau teste est plus tard;
     * * 0 si plus tot;
     * * -1 si ils se croisent.
     * @param creneau2
     * @return 1 si le creneau teste est plus tard, 0 si il est plus tot, -1 si ils se croisent.
     */
    private int TimeIsEarlierThan(Creneau creneau_teste) {
        int result = -2;
        if (this.getHeure_debut() >= creneau_teste.getHeure_fin()) {
            // Le creneau teste est plus tot
            result = 0;
        } else if (this.getHeure_fin() <= creneau_teste.getHeure_debut()) {
            // Le creneau teste est plus tard
            result = 1;
        }
        // Autrement les creneaux se croisent
        return result;
    }

    /**
     * Implementation pour de compareTo -> trier la liste.
     * @param creneau_teste
     * @return 1 si creneau teste est plus grand, -1 si il est plus tot
     */
    @Override
    public int compareTo(Creneau creneau_teste) {
        int result = this.TimeIsEarlierThan(creneau_teste);
        if (result == 1) {
            // Before
            return -1;
        } else {
            // After
            return 1;
        }
    }

    //==========================================================================
    //================ Methodes pour le controller -> Static ===================

    // Filtrer une liste avec une dateModel, et la ranger dans l'ordre

    public static ArrayList<Creneau> filtrer_list_reservations(ArrayList<Creneau> liste_reservation, Date_model date_model_voulue) {
        ArrayList<Creneau> liste_filtree = new ArrayList<>();
        // Ne recuperer que les reservations le meme jour
        for (Creneau creneau : liste_reservation) {
            if (creneau.getDateModel().equals(date_model_voulue)) {
                liste_filtree.add(creneau);
            }
        }
        // Regarder d'abord si il n'y a pas des creneaux qui se croisent -> pbm
        boolean probleme_liste = false;
        Creneau creneau_avant = null;
        for (Creneau creneau : liste_filtree) {
            if (!probleme_liste && creneau_avant != null) {
                if (creneau.TimeIsEarlierThan(creneau_avant) == -1) {
                    probleme_liste = true;
                }
            }
            creneau_avant = creneau;
        }
        // S'il y a un pbm, on redonne une liste nulle.
        if (probleme_liste) {
            System.out.println("Creneau : probleme lors du trie, les creneaux se chevauchent le meme jour");
            liste_filtree = null;
        } else {
            // Sinon on range la liste
            Collections.sort(liste_filtree);
        }
        // on change finalement l'argument.
        return liste_filtree;
    }

    // Calculer le taux d'occupation sur un jour -------------------------------

    /**
     * Methode statique pour obtenir le taux d'occupation a partir d'une liste de
     * reservations.
     * @param liste_reservations
     * @param debut_journee
     * @param fin_journee
     * @return taux d'occupation
     */
    public static short calcul_taux_occupation(ArrayList<Creneau> liste_reservations,
                                               int debut_journee, int fin_journee, Date_model date_model_voulue) {
        int total_max = (fin_journee - debut_journee)*60;
        int total = 0;
        short taux_occupation;

        // On recupere le temps de chaque reservation
        for (Creneau reservation_actuelle : liste_reservations) {
            if (date_model_voulue.equals(reservation_actuelle.getDateModel())) {
                total += reservation_actuelle.calcul_temps_reservation();
            }
            System.out.println(" **** Creneau , temps total : "+total);
        }
        // On calcule le taux d'occupation
        taux_occupation = (short)((float)(total)/(float)(total_max)*(float)100);
        // on retourne ce taux d'occupation
        System.out.println("**** Creneau, Taux d'occupation : "+taux_occupation);
        return taux_occupation;
    }

    // Obtenir la liste de tous les creneaux disponibles dans une liste --------

    /**
     * Retourne la liste des creneaux disponibles parmi une liste de reservation.
     * @param liste_reservations
     * @param debut_journee
     * @param fin_journee
     * @param dateModel
     * @return une liste de creneau libre.
     */
    public static ArrayList<Creneau> get_liste_tous_creneaux_dispo(ArrayList<Creneau> liste_reservations,
                                                                   int debut_journee, int fin_journee,
                                                                   Date_model dateModel) {
        short temps_reservation_min = 15;
        short heure_debut = 0;
        short minute_debut = 0;
        short heure_fin = 0;
        short minute_fin = 0;

        Creneau creneau_aux;
        ArrayList<Creneau> liste_creneaux_dispo = new ArrayList<>();
        // Pour chaque reservation de la liste
        System.out.println("****** get_liste_tous_creneaux_dispo ******");
        System.out.println("Creneau --- liste reservation, taille : "+liste_reservations.size());

        if (liste_reservations.isEmpty()) {
        // XXXXXXXXX cas : aucune reservation XXXXXXXXXXXXX
            creneau_aux = new Creneau((short)debut_journee,(short)0,(short)fin_journee,(short)0,dateModel);
            System.out.println(" -------- libre : "+creneau_aux.toString_TimeInLine());
            liste_creneaux_dispo.add(creneau_aux);
        } else {
            boolean premier = true;
            Creneau reservation_avant = null;
            for (Creneau reservation_actuel : liste_reservations) {
                // XXXXXXXXX CAS : debut de journee XXXXXXXXXXXXX
                if (premier) {
                    System.out.println("********** XXXXXX premier");
                    premier = false;
                    creneau_aux = new Creneau((short)debut_journee,(short)0,
                            reservation_actuel.getHeure_debut(),reservation_actuel.getMinute_debut(),
                            reservation_actuel.getDateModel());
                    if (creneau_aux.calcul_temps_reservation() > temps_reservation_min) {
                        liste_creneaux_dispo.add(creneau_aux);
                    }
                } else {
                // XXXXXXXXX CAS : milieu journee XXXXXXXXXXXXX
                    System.out.println("********** XXXXXX milieu");
                    creneau_aux = reservation_avant.get_creneau_entre(reservation_actuel);
                    if (creneau_aux.calcul_temps_reservation() < temps_reservation_min) {
                        // Si le creneau n'est pas trop petit, on l'ajoute
                        liste_creneaux_dispo.add(creneau_aux);
                    }
                }
                reservation_avant = reservation_actuel;
            } // fin de la boucle for

            // XXXXXXXXX CAS : fin de journee XXXXXXXXXXXXX
            System.out.println("********** XXXXXX fin");
            creneau_aux = new Creneau(reservation_avant.getHeure_fin(), reservation_avant.getMinute_fin(),
                                        (short)fin_journee, (short)0,
                                        dateModel);
            if (creneau_aux.calcul_temps_reservation() > temps_reservation_min) {
                liste_creneaux_dispo.add(creneau_aux);
            }
        }

        // on retourne la liste des creneaux disponibles
        return liste_creneaux_dispo;
    }

    // Rechercher un creneau libre dans une liste ------------------------------

    /**
     * Retourne le creneau libre qui correspondrait au creneau voulu parmi une liste
     * de reservation.
     * @param liste_reservations
     * @param debut_journee
     * @param fin_journee
     * @param creneau_voulu
     * @return le creneau libre correspondant, null si pas trouve.
     */
    public static Creneau get_creneau_libre(ArrayList<Creneau> liste_reservations,
                                            int debut_journee, int fin_journee,
                                            Creneau creneau_voulu ) {
        // On obtient la liste des creneaux disponibles
        ArrayList<Creneau> liste_creneau_dispo;
        liste_creneau_dispo = Creneau.get_liste_tous_creneaux_dispo(liste_reservations,
                                                                    debut_journee, fin_journee,
                                                                    creneau_voulu.getDateModel());
        // Variables pour l'obtention du creneau
        Creneau creneau_libre = null;
        boolean creneau_trouve = false;
        // Debut du test sur la liste des creneaux dispo
        if ( !liste_creneau_dispo.isEmpty() ) {
            int index = 0;
            // On parcourt toute la liste ou on s'arrete lorsqu'on a trouve un creneau
            for (Creneau creneau_teste : liste_creneau_dispo) {
                if (!creneau_trouve) {
                    // Il faut que le creneau soit le meme jour
                    // Si le creneau teste libre correspond au voulu, on l'a trouve.
                    if (creneau_teste.peut_contenir(creneau_voulu)) {
                        creneau_trouve = true;
                        creneau_libre = creneau_teste;
                    }
                }
            }
        } // fin du if
        return creneau_libre;
    }


    // Rechercher un creneau dans une liste ------------------------------------

    /**
     * Recherche le creneau correspondant dans une liste.
     * @param liste_creneaux
     * @param creneau_recherche
     * @return  Le creneau egale au creneau recherche.
     */
    public static Creneau get_creneau_liste(ArrayList<Creneau> liste_creneaux, Creneau creneau_recherche) {
        boolean trouve = false;
        Creneau creneau_voulu = null;
        for (Creneau creneau : liste_creneaux) {
            if (!trouve) {
                if (creneau_recherche.equals(creneau)) {
                    trouve = true;
                    creneau_voulu = creneau;
                }
            }
        }
        return creneau_voulu;
    }

    //======================== Methodes d'affichage ============================

    /**
     * Methode to string
     * @return string de creneau
     */
    @Override
    public String toString() {
        String retour = "Creneau du : "+ this.dateModel.toString() + "\n";
        retour += "debut : "+this.heure_debut+"h";
        if (minute_debut<10)
            retour +="0";
        retour += this.minute_debut;
        retour += " et fin : "+this.getHeure_fin()+"h";
        if (minute_fin<10)
            retour +="0";
        retour += this.minute_fin;
        return retour;
    }

    /**
     * Methode to String in line.
     * @return string de creneau temps en ligne.
     */
    public String toString_TimeInLine() {
        String retour = "Creneau du : "+ this.dateModel.toString();
        retour += " --- debut : "+this.heure_debut+"h";
        if (minute_debut<10)
            retour +="0";
        retour += this.minute_debut;
        retour += " et fin : "+this.getHeure_fin()+"h";
        if (minute_fin<10)
            retour +="0";
        retour += this.minute_fin;
        return retour;
    }

    public boolean isCreneauCorrecte() {
        boolean isCorrect = false;
        if ( this.heure_debut < this.heure_fin ) {
            isCorrect = true;
        } else if ( this.heure_debut == this.heure_fin ) {
            if ( this.minute_debut < this.minute_fin ) {
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    /**
     * Affiche la liste des reservations.
     * @param liste_creneaux
     * @param timeInLine
     */
    public static void afficher_liste_reservation(ArrayList<Creneau> liste_creneaux, boolean timeInLine) {
        for (Creneau creneau : liste_creneaux) {
            if (timeInLine)
                System.out.println(" ------------- \n"+creneau.toString_TimeInLine());
            else
                System.out.println(" ------------- \n"+creneau);
        }
    }


    //======================== Methodes privees ================================

    /**
     * Calcul le temps de la reservation en minutes.
     * @return le temps de la reservation en minutes.
     */
    private int calcul_temps_reservation() {
        short heures, minutes;
        int temps_minutes = 0;

        // calcul des heures
        heures = (short) (this.getHeure_fin() - this.getHeure_debut());
        // Si les heures sont inferieures a 0 : probleme, on met le temps a 0
        if ( !(heures < 0) ) {
            // calcul des minutes
            minutes = (short) (this.getMinute_fin() - this.getMinute_debut());
            if (minutes<0) {
                // par exemple 15h40 a 16h20 : on retranche 1 aux heures
                heures--;
                minutes += 60;
            }
            // Calcul du temps total en minute
            // Ajout des heures
            temps_minutes += 60*heures;
            temps_minutes += minutes;
        }
        // On retourne le temps en minute
        return temps_minutes;
    }

    /**
     * Teste si le creneau peut contenir le creneau en argument.
     * @param creneau_interieur_teste
     * @return True si le creneau est compris dedans.
     */
    public boolean peut_contenir(Creneau creneau_interieur_teste) {
        boolean compris = false;
        // Test sur la dateModel
        if (this.dateModel.equals(creneau_interieur_teste.getDateModel())) {
            // Test sur les heures
            if (this.heure_debut <= creneau_interieur_teste.getHeure_debut() &&
                    this.heure_fin >= creneau_interieur_teste.getHeure_fin()) {
                if (heure_debut == creneau_interieur_teste.getHeure_debut() &&
                        heure_fin == creneau_interieur_teste.getHeure_fin()) {
                    // Il faut regarder les minutes debut et fin
                    if (this.minute_debut <= creneau_interieur_teste.getMinute_debut() &&
                            this.minute_fin >= creneau_interieur_teste.getMinute_fin()) {
                        compris = true;
                    }
                } else if(heure_debut == creneau_interieur_teste.getHeure_debut()) {
                    if (this.minute_debut <= creneau_interieur_teste.getMinute_debut()) {
                        compris = true;
                    }
                } else if(heure_fin == creneau_interieur_teste.getHeure_fin()) {
                    if (this.minute_fin >= creneau_interieur_teste.getMinute_fin()) {
                        compris = true;
                    }
                } else {
                    compris = true;
                }
            }
        }
        return compris;
    }


    /**
     * Retourne si le creneau est le meme jour que celui en parametre.
     * @param creneau_2
     * @return
     */
    private boolean meme_jour(Creneau creneau_2) {
        if (this.dateModel.equals(creneau_2.getDateModel())) {
            return true;
        } else {
            return false;
        }
    }


    private Creneau get_creneau_entre(Creneau creneau_2) {
        Creneau creneau_entre = null;
        if (this.isCreneauCorrecte() && creneau_2.isCreneauCorrecte()) {
            if (this.getDateModel().equals(creneau_2.getDateModel())) {
                if (this.TimeIsEarlierThan(creneau_2) == 1) {
                    // On obtient le creneau entre
                    creneau_entre = new Creneau(this.getHeure_fin(),this.getMinute_debut(),
                            creneau_2.getHeure_debut(), creneau_2.getMinute_debut(),
                                                this.getDateModel());
                    // fin de l'obtention
                }
            }
        }
        return creneau_entre;
    }


    //==========================================================================
    //============================== Main Test =================================

    public static void main(String[] args) {
        ArrayList<Creneau> liste_reservations_non_filtree = new ArrayList<>();
        ArrayList<Creneau> liste_reservations = new ArrayList<>();
        ArrayList<Creneau> liste_creneaux_dispo = new ArrayList<>();
        Date_model dateModel = new Date_model((short)15,(short)1,(short)2015);

        // 8h15 a 10h30 ------------------
        Creneau reservation = new Creneau((short)8, (short)20, (short)10, (short)30, dateModel);

        // TEST : compris ======================================================
        // Test pour la methode creneau_compris_dans
        System.out.println("\n*************** Test compris *******************");
        System.out.println("Creneau de base : "+reservation.toString_TimeInLine());
        // Le test n°1 doit rendre vrai
        Creneau test_compris = new Creneau((short)8, (short)30, (short)10, (short)20, dateModel);
        System.out.println("Creneau teste : "+test_compris.toString_TimeInLine());
        if (reservation.peut_contenir(test_compris))
            System.out.println("Test 1 compris : ca marche OK");
        else
            System.out.println("Test 1 compris : marche pas");
        // Le test n°2 doit rendre faux
        test_compris = new Creneau((short)8, (short)15, (short)9, (short)20, dateModel);
        System.out.println("Creneau teste : "+test_compris.toString_TimeInLine());
        if (reservation.peut_contenir(test_compris))
            System.out.println("Test 2 compris : marche pas");
        else
            System.out.println("Test 2 compris : ca marche OK");
        // Le test n°3 doit rendre faux
        Date_model d_aux = new Date_model((short)1, (short)1, (short)1);
        test_compris = new Creneau((short)8, (short)30, (short)10, (short)20,d_aux);
        System.out.println("Creneau teste avec dateModel differente : "+test_compris.toString_TimeInLine());
        if (reservation.peut_contenir(test_compris))
            System.out.println("Test 2 compris : marche pas");
        else
            System.out.println("Test 3 compris : ca marche OK");
        System.out.println("*************** Test compris fini *********************\n");


        // **** Creation d'une liste de reservation ****
        // 12h40 a 15h00 ------------------
        reservation = new Creneau((short)12, (short)40, (short)15, (short)0, dateModel);
        System.out.println("Creneau 1 : "+reservation.toString_TimeInLine());
        liste_reservations_non_filtree.add(reservation);
        // 8h20 a 10h30 -------------------
        reservation = new Creneau((short)8, (short)20, (short)10, (short)30, dateModel);
        System.out.println("Creneau 2 : "+reservation.toString_TimeInLine());
        liste_reservations_non_filtree.add(reservation);
        // 17h00 a 19h40 ------------------
        reservation = new Creneau((short)17, (short)15, (short)19, (short)40, dateModel);
        System.out.println("Creneau 3 : "+reservation.toString_TimeInLine());
        liste_reservations_non_filtree.add(reservation);
        // 15h30 a 17h00 ------------------
        reservation = new Creneau((short)15, (short)30, (short)17, (short)0, dateModel);
        System.out.println("Creneau 4 : "+reservation.toString_TimeInLine());
        liste_reservations_non_filtree.add(reservation);
        // 10h40 a 11h mais sur une autre dateModel
        reservation = test_compris;
        System.out.println("Creneau 5 sur une autre dateModel : "+reservation.toString_TimeInLine());
        liste_reservations_non_filtree.add(reservation);
        // **** Fin de la creation de la liste test de reservation ****

        // TEST : filtrage de la liste et trie =================================
        System.out.println("\n*************** Test filtrage et trie *********************");
        System.out.println("--- Avant le trie ---");
        afficher_liste_reservation(liste_reservations_non_filtree, true);
        // On trie la liste
        liste_reservations = filtrer_list_reservations(liste_reservations_non_filtree, dateModel);
        // On l'affiche
        System.out.println("\n--- Apres le tri ---");
        afficher_liste_reservation(liste_reservations, true);
        System.out.println("*************** Test filtrage et trie fini *********************\n");

        //TEST : Taux d'occupation =============================================
        System.out.println("\n*************** Test taux d'occupation *********************");
        int debut = 8;
        int fin = 20;
        System.out.println("L'heure d'ouverture est "+debut+"h et fermeture "+fin+"h");
        System.out.println("Taux occupation : "+Creneau.calcul_taux_occupation(liste_reservations,debut,fin, dateModel));
        System.out.println("*************** Test taux d'occupation fini *********************\n");


        //TEST : liste creneaux dispo ==========================================
        // On va tester get_liste_creneaux_dispo
        System.out.println("\n*************** Test get_liste_creneaux_dispo*********************");
        System.out.println("==== TEST avec la liste ci-dessus");
        liste_creneaux_dispo = Creneau.get_liste_tous_creneaux_dispo(liste_reservations, 8, 20, dateModel);
        afficher_liste_reservation(liste_creneaux_dispo, true);
        System.out.println("\n==== TEST avec une liste vide -> S'il n'y a rien c'est ok");
        ArrayList<Creneau> liste_aux = new ArrayList<>();
        liste_aux = Creneau.get_liste_tous_creneaux_dispo(liste_aux, 8, 20, dateModel);
        afficher_liste_reservation(liste_aux, true);
        System.out.println("==== TEST avec une liste de reservation d'un autre jour -> S'il n'y a rien c'est ok");
        liste_aux.add(test_compris);
        afficher_liste_reservation(liste_aux, true);
        System.out.println("*************** Test get_liste_creneaux_dispo fini*********************\n");


        //TEST : get_creneau_libre =============================================
        // On dit le creneau qu'on veut : 10h30 a 12h15 -> on a un libre
        System.out.println("\n*************** Test get_creneau_libre *********************");
        Creneau creneau_voulue = new Creneau((short)10, (short)35, (short)12, (short)15, dateModel);
        System.out.println("On teste le creneau : "+creneau_voulue.toString_TimeInLine());
        // On obtient le creneau libre
        Creneau creneau_libre = Creneau.get_creneau_libre(liste_reservations, 8, 20, creneau_voulue);
        if (creneau_libre != null) {
            System.out.println("On a trouve un creneau : "+creneau_libre.toString_TimeInLine()+" -> OK");
        } else {
            System.out.println("PBM On a pas trouve de creneau");
        }
        // on dit le creneau qu'on veut : 12h30 a 14h0 -> aucun de libre
        creneau_voulue = new Creneau((short)12, (short)30, (short)14, (short)0, dateModel);
        System.out.println("\nOn teste le creneau : "+creneau_voulue.toString_TimeInLine());
        creneau_libre = Creneau.get_creneau_libre(liste_reservations, 8, 20, creneau_voulue);
        if (creneau_libre != null) {
            System.out.println("PBM On a trouve un creneau : "+creneau_libre.toString_TimeInLine());
        } else {
            System.out.println("On a pas trouve de creneau -> OK");
        }
        System.out.println("*************** Test get_liste_creneaux_dispo fini*********************");
    }
}
