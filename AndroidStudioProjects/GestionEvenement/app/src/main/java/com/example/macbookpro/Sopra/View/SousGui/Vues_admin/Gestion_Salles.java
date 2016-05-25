package com.example.macbookpro.Sopra.View.SousGui.Vues_admin;
import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbookpro.Sopra.Models.Site;
import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.Models.Salle;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_admin_listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 28/12/2015.
 */
public class Gestion_Salles extends AppCompatActivity implements OnItemSelectedListener {

    // --- Composants ---
    private Spinner spinner_salle;
    private Button boutonEditer;
    private Button boutonSupprimer;
    private Button boutonAjouter;
    private TextView texte_nom_site_gere;
    private EditText text_salle_a_ajouter;

    // --- Variables ---
    private Site site_actuel;
    private Salle salle_selectionnee;
    // Context
    final Context context = this;

    // Contient la liste des sites et des salles, partage avec le controller.
    private Mes_listes_sites_salles mes_listes;
    // Mon listener.
    private Gui_admin_listener myListener;

    /**
     * Constructeur.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_gestion_salles);

        // Recuperation du listener et des listes
        myListener = Controller.get_admin_listener();
        mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();

        // Initialisation des composants
        spinner_salle =(Spinner)findViewById(R.id.choixSalle);
        spinner_salle.setOnItemSelectedListener(this);
        texte_nom_site_gere=(TextView)findViewById(R.id.nom_site_gere);
        text_salle_a_ajouter = (EditText) findViewById(R.id.editText);
        remplissageSpinner();

        // Ajout des listener
        ListenerEditionSalle();
        ListenerSupprimerSalle();
        ListenerAjouterSalle();

        // On recupere le site passe en argument
        site_actuel = mes_listes.getSite_aux();

        // affiche le site courant
        texte_nom_site_gere.setText(site_actuel.toString());
    }

    /**
     * Appele apres un retour vers la page.
     */
    @Override
    protected void onResume() {
        super.onResume();
        remplissageSpinner();
    }

    /**
     * Remplir le spinner, la liste des salles.
     */
    private void remplissageSpinner(){
        // Ajout du nom des salles a la liste de nom des salles
        List<String> choix= new ArrayList<>();
        for(Salle salle : mes_listes.get_liste_salles()) {
            choix.add(salle.getNom_salle());
        }
        // Adaptateur pour la liste
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, choix);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_salle.setAdapter(dataAdapter);
    }

    /**
     *  Action lorsqu'on selectionne une salle.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String nouveau_nom_salle = parent.getItemAtPosition(position).toString();
        this.salle_selectionnee = mes_listes.get_salle(nouveau_nom_salle);
    }

    /**
     * Methode non utilisee.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    /**
     * Ajouter le listener pour le bouton editer la salle.
     */
    public void ListenerEditionSalle(){
        boutonEditer=(Button)findViewById(R.id.editerSalle);
        boutonEditer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mes_listes.get_liste_salles().isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Liste vide", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), Editon_Salle.class);
                    // Le site est deja mis, on met la salle dans mes_listes en aux
                    mes_listes.setSalle_aux(salle_selectionnee);
                    startActivity(i);
                }
            }
        });
    }

    /**
     * Ajouter le listener pour le bouton supprimer la salle.
     */
    private void ListenerSupprimerSalle(){
        boutonSupprimer=(Button)findViewById(R.id.suppSalle);
        // Action de suppression a implementer
        boutonSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salle_selectionnee != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setTitle("Supprimer");
                    // Afficher le message du popup
                    alertDialogBuilder
                            .setMessage("Voulez-vous vraiment supprimer la salle : "+
                                        salle_selectionnee.getNom_salle())
                            .setCancelable(false)
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Si click sur oui, on supprime la salle et on met a jour la liste
                                    //Appel de la methode du controller pour l'ajout du site
                                    tenter_supprimer_salle();
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
                } else {

                }
            }
        });



    }

    /**
     * Ajouter le listener pour le bouton ajouter une nouvelle salle.
     */
    private void ListenerAjouterSalle(){
        boutonAjouter=(Button)findViewById(R.id.btnValiderSalle);
        // action de validation a implementer
        boutonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenter_ajouter_salle();
            }
        });
    }


    //==============================================================================================

    /**
     * Tenter d'ajouter une salle.
     */
    private void tenter_ajouter_salle() {
        String salle_a_ajouter = text_salle_a_ajouter.getText().toString();
        if ( mes_listes.get_salle(salle_a_ajouter) != null ) {
            // La salle existe deja
            // Une fenetre alert affiche que la salle existe deja
            Toast.makeText(getApplicationContext(), "La salle existe deja"
                    , Toast.LENGTH_LONG).show();
        } else if (!salle_a_ajouter.equals("")) {
            // On l'ajoute
            //Appel de la methode du controller pour l'ajout du site
            Salle nouvelle_salle = new Salle(salle_a_ajouter);
            myListener.perform_ajout_salle(site_actuel, nouvelle_salle);
            // Actualisation de la vue
            remplissageSpinner();
            // Une fenetre alert affiche le site qu'il veut ajouter
            Toast.makeText(getApplicationContext(), "ajout de " +
                    salle_a_ajouter, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Echec : le nom est null", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Tenter de supprimer une salle.
     */
    private void tenter_supprimer_salle() {
        if (!mes_listes.get_liste_salles().isEmpty()) {
            myListener.perform_supprimer_salle(site_actuel, salle_selectionnee);
            // On affiche un message pour dire qu'on a ajoute la salle
            Toast.makeText(getApplicationContext(), "Suppression de la salle : " +
                    salle_selectionnee.getNom_salle(), Toast.LENGTH_LONG).show();
            // update du spinner
            remplissageSpinner();
        } else {
            Toast.makeText(getApplicationContext(), "Liste des salles est vide"
                    , Toast.LENGTH_LONG).show();
        }
    }

}
