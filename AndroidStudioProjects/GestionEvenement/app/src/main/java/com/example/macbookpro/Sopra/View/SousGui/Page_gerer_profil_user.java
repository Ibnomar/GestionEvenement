package com.example.macbookpro.Sopra.View.SousGui;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_user_listener;
import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.SousGui.Vues_user.Rechercher_salle_user;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by macbookpro on 23/11/2015.
 */
public class Page_gerer_profil_user extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // --- Composants ---
    // le spinner qui contient la liste des sites.
    private Spinner spinner;
    // Bouton pour choisir le site
    private Button bouton_choisir_site;

    // --- FIELDS ---
    // Contient la liste des sites et des salles : instance partage avec le controller.
    private Mes_listes_sites_salles mes_listes;
    // Mon listener.
    private Gui_user_listener myListener;

    // --- Variables ---
     // Correspond au site que l'utilisateur aura choisit.
    private Site site_choisi;


    /**
     * Constructeur.
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_choix_site);
        // On recupere le listener et les listes
        myListener = Controller.get_user_listener();
        this.mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();
        // On ajoute les sites dans le spinner
        addItemsOnSpinner();
        // On lui associe le itemSelecter
        spinner.setOnItemSelectedListener(this);
        // On ajoute le listener sur le bouton choisir
        ListenerBoutonChoisirSite();
    }

    /**
     *  Ajouter les sites dans le spinner.
     */
    public  void addItemsOnSpinner() {
        // Recuperer le spinner
        spinner= (Spinner)findViewById(R.id.spinner);
        // On ajoute les noms des sites dans la liste de nom des sites
        List<String> liste_nom_sites= new ArrayList<String>();
        for(int i=0; i< mes_listes.get_liste_sites().size(); i++){
            liste_nom_sites.add(mes_listes.get_liste_sites().get(i).toString());
        }
        // On ajoute l'adaptateur pour mettre la liste dans le spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, liste_nom_sites);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * methode permettant de recuperer le site choisi dans la liste.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // permet de recuperer l'item selectionne
        site_choisi = mes_listes.get_site( parent.getItemAtPosition(position).toString() );
    }

    /**
     * Methode non utilisee.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    /*
     Listener lors du clique sur le bouton choisir
     */

    /**
     *  Ajouter le listener au bouton choisir site
     */
    public void ListenerBoutonChoisirSite() {
        // recuperation du bouton choisir site
        bouton_choisir_site =(Button)findViewById(R.id.choix);
        bouton_choisir_site.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("GUI - Choix du site : "+site_choisi);
                    myListener.perform_choisir_site_user(site_choisi);
                    Intent i = new Intent(getApplicationContext(), Rechercher_salle_user.class);
                    mes_listes.setSite_aux(site_choisi);
                    System.out.println("GUI - transition vers la recherche de salle");
                    startActivity(i);
                    }
            }
        );
    }


}