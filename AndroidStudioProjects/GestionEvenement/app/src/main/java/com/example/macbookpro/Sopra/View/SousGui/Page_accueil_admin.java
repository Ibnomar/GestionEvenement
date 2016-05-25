package com.example.macbookpro.Sopra.View.SousGui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;
import com.example.macbookpro.Sopra.Models.Site;
import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_admin_listener;
import com.example.macbookpro.Sopra.View.SousGui.Vues_admin.Gestion_Site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 02/01/16.
 */
public class Page_accueil_admin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // --- Composants ---
    // Spinner qui contient la liste des sites
    private Spinner spinner;
    // Bouton qui permet d'aller vers la gestion du site
    private Button bouton_gestion_du_site;
    // Bouton qui permet d'ajouter un nouveau site
    private Button  bouton_ajouter_site;
    // Champ texte ou recuperer le nom du site a ajouter
    private EditText text_site_a_ajouter;

    // --- FIELDS ---
    // Contient la liste des sites et des salles : instance partagee avec le controller
    private Mes_listes_sites_salles mes_listes;
    // Mon listener
    private Gui_admin_listener myListener;
    // Context
    final Context context = this;

    // --- Variables ---
    // Contient le site selectionne
    private Site site_choisi;

    /**
     * Constructeur.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_accueil);

        System.out.println("onCreate de admin");

        // Obtenir les instances du listener et des listes.
        myListener = Controller.get_admin_listener();
        this.mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();

        // Identification du spinner choix site admin
        spinner = (Spinner) findViewById(R.id.ChoixSiteAdmin);
        // Ajout du listener
        spinner.setOnItemSelectedListener(this);
        // Ajout des sites dans le spinner
        addItemsOnSpinner();
        // On selectionne l'ancien site si il y en avait
        int position;
        if (mes_listes.getSite_aux() != null) {
            position = mes_listes.get_position_Site_aux();
            System.out.println("Ancien site a la position : "+position);
            if (position != -1) { spinner.setSelection(position); }
        }

        // Identification du champ text pour ajouter un site
        text_site_a_ajouter = (EditText) findViewById(R.id.site_ajout);

        // Ajout du listener pour le bouton de gestion de site
        ListenerBoutonGestionSite();
        // Ajout du listener pour le bouton ajouter un site
        ListenerBoutonAjouter();
    }

    /**
     * Appele apres un retour vers la page.
     */
    @Override
    protected void onResume() {
        super.onResume();
        update_spinner();
    }

    /**
     * Ajouter les sites dans le spinner.
     */
    public  void  addItemsOnSpinner() {
        // Ajouter le nom des sites dans la liste de nom des sites
        List<String> liste_nom_sites = new ArrayList<String>();
        for(int i=0; i< mes_listes.get_liste_sites().size(); i++){
            liste_nom_sites.add(mes_listes.get_liste_sites().get(i).toString());
        }
        // Adaptateur pour afficher la liste des noms dans notre spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                liste_nom_sites);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * Recuperer le site selectionne.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Obtenir le site choisi.
        site_choisi = mes_listes.get_site( parent.getItemAtPosition(position).toString() );
        // Methode du listener pour effectuer le changement de site.
        myListener.perform_choisir_site_admin(site_choisi);
    }

    /**
     * Methode non utilisee.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    /**
     * Ajouter le listener du bouton gestion d'un site.
     */
    public void ListenerBoutonGestionSite(){
        bouton_gestion_du_site = (Button)findViewById(R.id.btnGestion);
        bouton_gestion_du_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On faite le test pour verifier si le site choisi n'est pas null
                // si c'est le cas on ne fait rien, sinon on passe vers la page de gestion de site
                if(site_choisi != null) {
                    System.out.println("GUI - Admin : Transition vers Gestion Site");
                    Intent i = new Intent(getApplicationContext(), Gestion_Site.class);
                    mes_listes.setSite_aux(site_choisi);
                    startActivity(i);
                } else {
                    System.out.println("GUI admin : site choisi null");
                }
            }
        });
    }

    /**
     * Ajouter le listener du bouton ajouter un site.
     */
    public void ListenerBoutonAjouter(){
        bouton_ajouter_site = (Button)findViewById(R.id.ajoutSite);
        bouton_ajouter_site.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);
                        alertDialogBuilder.setTitle("Ajouter site");
                        // Afficher le message du popup
                        alertDialogBuilder
                                .setMessage("Voulez-vous vraiment ajouter un site ?")
                                .setCancelable(false)
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String site_a_ajouter = text_site_a_ajouter.getText().toString();
                                        // On tente d'ajouter le site
                                        tenter_ajouter_site();
                                    }
                                })
                                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Si click sur non, rien ne se passe
                                        dialog.cancel();
                                    }
                                });
                        // creation de la fenêtre d'alerte
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // Affiche le popup
                        alertDialog.show();
                    }
                }
        );
    }

    /**
     * Updater le spinner.
     */
    private void update_spinner() {
        // Ajout des sites dans le spinner
        addItemsOnSpinner();
        // On selectionne l'ancien site si il y en avait
        int position;
        if (mes_listes.getSite_aux() != null) {
            position = mes_listes.get_position_Site_aux();
            System.out.println("Ancien site a la position : "+position);
            if (position != -1) { spinner.setSelection(position); }
        }
    }


    private void tenter_ajouter_site() {
        String site_a_ajouter = text_site_a_ajouter.getText().toString();
        if ( mes_listes.get_site(site_a_ajouter) != null ) {
            // Le site existe deja
            // Une fenetre alert affiche que le site existe deja
            Toast.makeText(getApplicationContext(), "Le site existe deja"
                    , Toast.LENGTH_LONG).show();
        } else if (!site_a_ajouter.equals("")) {
            // On l'ajoute
            //Appel de la methode du controller pour l'ajout du site
            Site nouveau_site = new Site(site_a_ajouter);
            myListener.perform_ajout_site(nouveau_site);
            // Actualisation de la vue
            update_spinner();
            // Une fenetre alert affiche le site qu'il veut ajouter
            Toast.makeText(getApplicationContext(), "ajout de " +
                    site_a_ajouter, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "nom du site null", Toast.LENGTH_LONG).show();
        }
    }

}
