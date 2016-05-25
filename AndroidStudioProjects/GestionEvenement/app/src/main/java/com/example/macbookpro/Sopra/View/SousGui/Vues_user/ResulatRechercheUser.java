package com.example.macbookpro.Sopra.View.SousGui.Vues_user;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;

import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.Creneau;
import com.example.macbookpro.Sopra.Models.Date_model;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;

import com.example.macbookpro.Sopra.Models.Options;
import com.example.macbookpro.Sopra.Models.Salle;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_user_listener;
import com.example.macbookpro.Sopra.View.SousGui.Outils_vues.MyExpandableAdapter;

import java.util.ArrayList;

/**
 * Created by macbookpro on 29/11/2015.
 */

public class ResulatRechercheUser extends ExpandableListActivity {
    // Mon listener.
    private Gui_user_listener myListener;
    // Contient la liste des sites et des salles, partage avec le controller.
    private Mes_listes_sites_salles mes_listes;
    // Le bouton permettant de faire le retour a la page precedente.

    // les listes pour stocker les salles et les creneaux
    private ArrayList<String> salleItems = new ArrayList<String>();
    private ArrayList<ArrayList<String>> creneauItems = new ArrayList<>();

    // Test : liste pour test
    private ArrayList<Salle> liste_test_salles;

    // L'adaptateur pour la liste des salles et des creneaux : contient aussi les onClick
    private MyExpandableAdapter adaptateur_listes;
    // Variables pour l'adaptateur
    ArrayList<Salle> parents_items = new ArrayList<Salle>();
    ArrayList<Object> child_items = new ArrayList<Object>();



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        System.out.println("GUI, Resultat Recherche");

        // On recupere mes listes et le listener
        this.mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();
        this.myListener = Controller.get_user_listener();

        // Creation d'une liste expandanble
        ExpandableListView expandableList = getExpandableListView();
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        // init liste test
        init_liste_test();

        // Initialiser les listes pour l'adaptateur
        setGroups_parents_child();
        // Initialiser l'adaptateur
        adaptateur_listes = new MyExpandableAdapter(parents_items,
                                                    child_items,
                                                    getApplicationContext());
        adaptateur_listes.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        // Mettre l'adptateur sur la liste
        expandableList.setAdapter(adaptateur_listes);
        expandableList.setOnChildClickListener(this);

        System.out.println("GUI, Resultat Recherche, fin du create");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mes_listes.isReservation_faite()) {
            System.out.println("Resultat recherche user : on a fait une reservation");
            mes_listes.setReservation_faite(false);
            finish();
        }
    }

    // Test : methode pour ajouter les salles
    public void init_liste_test() {
        Options option1 = new Options(true, false,true,false);
        Options option2 = new Options(false,true,true,false);
        liste_test_salles = new ArrayList<Salle>();
        ArrayList<Creneau> liste_test_creneau= new ArrayList<>();

        Salle salle = new Salle("Salle_1",option1,(short)20);
        Date_model dateModel1 = new Date_model((short)20,(short)12,(short)2016);
        Creneau creneau = new Creneau((short)10,(short)20,(short)12,(short)30, dateModel1);
        liste_test_creneau.add(creneau);
        creneau = new Creneau((short)13,(short)00,(short)16,(short)30, dateModel1);
        liste_test_creneau.add(creneau);
        salle.set_liste_creneaux(liste_test_creneau);
        liste_test_salles.add(salle);

        salle =  new Salle("Salle_2",option1,(short)40);
        creneau = new Creneau((short) 17, (short) 00, (short) 18, (short) 30, dateModel1);
        liste_test_creneau.add(creneau);
        creneau = new Creneau((short)14, (short) 00, (short) 15, (short) 30, dateModel1);
        liste_test_creneau.add(creneau);
        salle.set_liste_creneaux(liste_test_creneau);

        liste_test_salles.add(salle);

        salle =  new Salle("Salle_3",option1,(short)30);
        creneau = new Creneau((short) 8, (short) 00, (short) 10, (short) 40, dateModel1);
        liste_test_creneau.add(creneau);
        creneau = new Creneau((short)11, (short) 00, (short) 14, (short) 30, dateModel1);
        liste_test_creneau.add(creneau);
        salle.set_liste_creneaux(liste_test_creneau);
        liste_test_salles.add(salle);
    }

    /**
     * Initialiser la liste des parents et enfants.
     */
    public void setGroups_parents_child() {
        ArrayList<Creneau> child;
        for (Salle salle_actuelle : mes_listes.get_liste_salles()) {
            parents_items.add(salle_actuelle);
            child = new ArrayList<Creneau>();
            for (Creneau creneau_actuel : salle_actuelle.get_liste_creneaux()) {
                child.add(creneau_actuel);
            }
            child_items.add(child);
        }
    }



}
