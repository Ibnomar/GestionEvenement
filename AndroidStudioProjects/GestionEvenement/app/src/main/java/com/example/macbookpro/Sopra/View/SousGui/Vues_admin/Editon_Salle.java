package com.example.macbookpro.Sopra.View.SousGui.Vues_admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;
import com.example.macbookpro.Sopra.Models.Options;
import com.example.macbookpro.Sopra.Models.Site;
import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_admin_listener;
import com.example.macbookpro.Sopra.View.SousGui.Outils_vues.Interface_multiSelec_gui;
import com.example.macbookpro.Sopra.View.SousGui.Outils_vues.MultiSelectionSpinner;
import com.example.macbookpro.Sopra.Models.Salle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 28/12/2015.
 */
public class Editon_Salle extends AppCompatActivity implements Interface_multiSelec_gui {

    // --- Composants ---
    private Button bouton_modifier_nom_salle;
    private Button bouton_modifier_nombre_personnes;
    private MultiSelectionSpinner spinner_options;
    private TextView texte_nom_salle;
    private TextView texte_nom_site;
    private EditText editText_nom_salle;
    private EditText editText_nombre_personnes;

    // --- Variables ---
    private Salle salle_actuelle;
    private Site site_actuel;

    // --- Fields ---
    // Contient la liste des sites et des salles, partage avec le controller.
    private Mes_listes_sites_salles mes_listes;
    // Mon listener.
    private Gui_admin_listener myListener;

    /**
     * Constructeur.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edition_salle);

        // On recupere le listener et les listes
        this.myListener = Controller.get_admin_listener();
        this.mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();

        // on recupere le site et la salle actuelle
        this.site_actuel = mes_listes.getSite_aux();
        this.salle_actuelle = mes_listes.getSalle_aux();

        // On recupere les text
        texte_nom_site=(TextView)findViewById(R.id.textViewSite);
        texte_nom_salle=(TextView)findViewById(R.id.textViewSalle);
        // les editText
        editText_nom_salle = (EditText)findViewById(R.id.editText1);
        editText_nombre_personnes = (EditText)findViewById(R.id.editText2);

        // on affiche le nom du site et de la salle
        texte_nom_site.setText(site_actuel.toString());
        texte_nom_salle.setText(salle_actuelle.toString());

        // On ajoute les listener sur les boutons
        ListenerModifierNom();
        ListenerModifierPersonnes();
        // On rempli la liste des options
        RemplissageSpinner();
    }

    /**
     * Ajouter le listener au bouton modifier le nom de la salle.
     */
    public void ListenerModifierNom(){
        bouton_modifier_nom_salle = (Button)findViewById(R.id.valider);
        bouton_modifier_nom_salle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenter_modifier_nom_salle();
            }
        });
    }

    /**
     *  Ajouter le listener au bouton modifier le nombre de personnes.
     */
    public void ListenerModifierPersonnes(){
        bouton_modifier_nombre_personnes = (Button)findViewById(R.id.btnValiderSalle);
        bouton_modifier_nombre_personnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_nombre_personnes = (EditText) findViewById(R.id.editText2);
                // On recupere le nouveau nombre de personnes
                int nouveau_nombre_personnes = Integer.parseInt(
                        editText_nombre_personnes.getText().toString());
                // On appelle la methode pour modifier la salle du listener
                Salle nouvelle_salle = new Salle(salle_actuelle);
                nouvelle_salle.setNombre_personnes_max((short)nouveau_nombre_personnes);
                myListener.perform_modif_salle(salle_actuelle, nouvelle_salle);
                texte_nom_salle.setText(nouvelle_salle.toString());
            }
        });
    }

    /**
     * Remplir le spinner pour les options.
     */
    public void RemplissageSpinner(){
        System.out.println("Test_options, remplissage spinner");
        // La liste des options disponibles
        String[] array = {"Visio", "Telephone","Securisee","Salle Digilab"};
        // on les met dans le spinner
        spinner_options = (MultiSelectionSpinner) findViewById(R.id.particularite);
        spinner_options.setItems(array);
        spinner_options.set_Interface_Multi_gui(this);

        System.out.println("Test_options, Salle actuelle : "+salle_actuelle.getNom_salle()+
                            "\nOptions de la salle actuelle : "+salle_actuelle.getOptions());
        boolean visio = salle_actuelle.getOptions().aVisio();
        boolean tel = salle_actuelle.getOptions().aTelephone();
        boolean secu = salle_actuelle.getOptions().aSecurise();
        boolean digi = salle_actuelle.getOptions().aDigilab();
        ArrayList<String> liste_options_integer = new ArrayList<>();
        if (visio) {
            liste_options_integer.add(array[0]);
        }
        if (tel) {
            liste_options_integer.add(array[1]);
        }
        if (secu) {
            liste_options_integer.add(array[2]);
        }
        if (digi) {
            liste_options_integer.add(array[3]);
        }
        spinner_options.setSelection(liste_options_integer);
    }

    /**
     * Creation du spinner cochable (menu).
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Effectuer les changements lorsqu'on a change le spinner -> click sur "Envoyer" dans
     * MultiSelectionSpinner.
     */
    @Override
    public void change_in_spinner() {
        List<Integer> listes_indices_options = spinner_options.getSelectedIndices();
        boolean visio = false;
        boolean tel = false;
        boolean secu = false;
        boolean digi = false;
        for (Integer num : listes_indices_options) {
            if (num == 0)
                visio = true;
            if (num == 1)
                tel = true;
            if (num == 2)
                secu = true;
            if (num == 3)
                digi = true;
            Salle nouvelle_salle = new Salle(salle_actuelle);
            Options nouvelles_options = new Options(visio,tel,secu,digi);
            System.out.println("GUI, edition salle : " +
                    "\n --- anciennes options : "+salle_actuelle.getOptions() +
                    "\n --- nouvelles options : "+nouvelles_options);
            nouvelle_salle.setOptions( nouvelles_options );
            myListener.perform_modif_salle(salle_actuelle,nouvelle_salle);
            salle_actuelle = nouvelle_salle;
            texte_nom_salle.setText(salle_actuelle.toString());
        }
    }

    //==============================================================================================

    private void tenter_modifier_nom_salle() {
        System.out.println("Tentative_modif_salle");
        String nouveau_nom_salle = editText_nom_salle.getText().toString();
        if (nouveau_nom_salle.equals("")) {
            System.out.println("Tentative_modif_salle : nom null");
            Toast.makeText(getApplicationContext(), "Echec : nom null", Toast.LENGTH_LONG).show();
        } else if ( mes_listes.get_salle(nouveau_nom_salle) != null ) {
            System.out.println("Tentative_modif_salle : salle existe deja");
            Toast.makeText(getApplicationContext(), "La salle existe deja", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("Tentative_modif_salle : le nom est disponible");
            // La salle n'existe pas encore
            String phrase = "Vous avez modifie " + salle_actuelle.getNom_salle() + " en " +
                    nouveau_nom_salle;
            Toast.makeText(getApplicationContext(), phrase, Toast.LENGTH_LONG).show();
            //Appel de la methode du controller pour la modification
            Salle nouvelle_salle = new Salle(salle_actuelle);
            nouvelle_salle.setNom_salle(nouveau_nom_salle);
            myListener.perform_modif_salle(salle_actuelle, nouvelle_salle);
            salle_actuelle = nouvelle_salle;
            // Actualiser le site aux dans mes listes, utilise lors de onCreate()
            mes_listes.setSalle_aux( salle_actuelle );
            // rafraichir la vue
            texte_nom_salle.setText(salle_actuelle.toString());
        }
    }

}
