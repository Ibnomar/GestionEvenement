package com.example.macbookpro.Sopra.View.SousGui.Vues_admin;

import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;
import com.example.macbookpro.Sopra.Models.Site;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_admin_listener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.SousGui.Page_accueil_admin;

/**
 * Created by macbookpro on 28/12/2015.
 */
public class Gestion_Site extends AppCompatActivity {

    private Button bouton_gestion_salles;
    private Button bouton_rapport;
    private Button bouton_supprimer;
    private Button bouton_modifier;
    private Site  site_courant;
    private TextView text_site_courant;
    private EditText text_modifier_nom_site;

    // Contient la liste des sites et des salles, partage avec le controller.
    private Mes_listes_sites_salles mes_listes;
    // Mon listener.
    private Gui_admin_listener myListener;
    // on prend le context actuel
    Context context=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_gestion_site);

        // Recuperer le listener et les listes
        myListener = Controller.get_admin_listener();
        mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();

        // Intialisation des boutons et champs de texte
        text_site_courant = (TextView)findViewById(R.id.nomSiteGerer);
        text_modifier_nom_site = (EditText)findViewById(R.id.editText_Modifier_site);
        bouton_modifier = (Button)findViewById(R.id.nomsite);
        bouton_gestion_salles=(Button)findViewById(R.id.gererSalle);
        bouton_rapport=(Button)findViewById(R.id.genererRapport);
        bouton_supprimer=(Button)findViewById(R.id.suppsite);

        // Initialisation des methodes permettant gerer toutes les actions : ajout des listener
        ListenerBoutonModifier();
        addListenerOnButtonGestionSalles();
        addListenerOnButtonRapport();
        addListenerOnButtonSupprimer();

        // On recupere le site actuel
        site_courant = mes_listes.getSite_aux();

        // afficher le site courant
        text_site_courant.setText(site_courant.toString());

        /*
        Tant que l'admin n'aura pas saisi le nom du site a modifier il ne pourra pas
        cliquer sur le bouton modifier
        */
        text_modifier_nom_site.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bouton_modifier.setEnabled(!text_modifier_nom_site.getText().toString().trim().isEmpty());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Listener pour le bouton modifier le nom du site.
     */
    public void ListenerBoutonModifier(){
        bouton_modifier.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // On affiche une alerte pour dire qu'on veut modifier le nom du site
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Modifier");
                    // Afficher le message du popup
                    alertDialogBuilder
                    .setMessage("Voulez-vous vraiment Modifier le nom du site")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            tenter_modifier_site();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // si non, rien ne se passe
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
     * Ajout du listener pour le bouton gerer les salles.
     */
    public void addListenerOnButtonGestionSalles(){
        bouton_gestion_salles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GUI - Admin : Transition vers Gestion des salles");
                Intent i = new Intent(getApplicationContext(), Gestion_Salles.class);
                mes_listes.setSite_aux(site_courant);
                startActivity(i);
            }
        });
    }

    /**
     * Ajout du listener pour le bouton generer le rapport.
     */
    public void addListenerOnButtonRapport(){
        bouton_rapport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GUI - Admin : Transition vers Affichage du rapport");
                mes_listes.setSite_aux(site_courant);
                Intent i = new Intent(getApplicationContext(), Affichage_rapport.class);
                startActivity(i);
            }
        });
    }

    /**
     * Ajout du listener pour le bouton supprimer un site.
     */
    public void addListenerOnButtonSupprimer(){
        bouton_supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GUI - Admin : Suppression du site");
                // On affiche une alerte pour dire qu'on veut supprimer un site
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Suppression");
                // Afficher le message du popup
                alertDialogBuilder
                        .setMessage("Voulez-vous vraiment supprimer le site")
                        .setCancelable(false)
                        .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // si ok, on supprime le site et on revient a la page d'accueil
                                //Appel de la methode du controller pour la suppression
                                myListener.perform_supprimer_site(site_courant);
                                // retour a la page d'accueil admin
                                finish();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // si non, rien ne se passe
                                dialog.cancel();
                            }
                        });
                // creation de la fenêtre d'alerte
                AlertDialog alertDialog = alertDialogBuilder.create();
                // Affiche le popup
                alertDialog.show();
            }
        });
    }


    private void tenter_modifier_site() {
        String nouveau_nom_site = text_modifier_nom_site.getText().toString();
        if (nouveau_nom_site.equals("")) {
            Toast.makeText(getApplicationContext(), "Echec : nom null", Toast.LENGTH_LONG).show();
        } else if ( mes_listes.get_site(nouveau_nom_site) == null ) {
            // Le site n'existe pas encore
            String phrase = "Vous avez modifie " + site_courant.getNom_site() + " en " +
                    nouveau_nom_site;
            Toast.makeText(getApplicationContext(), phrase, Toast.LENGTH_LONG).show();
            //Appel de la methode du controller pour la modification
            Site nouveau_site = new Site(text_modifier_nom_site.getText().toString());
            myListener.perform_modif_site(site_courant, nouveau_site);
            site_courant = nouveau_site;
            // Actualiser le site aux dans mes listes, utilise lors de onCreate()
            mes_listes.setSite_aux(nouveau_site);
            // rafraichir la vue
            text_site_courant.setText(site_courant.getNom_site());
        } else {
            Toast.makeText(getApplicationContext(), "Le site existe deja", Toast.LENGTH_LONG).show();
        }
    }

}
