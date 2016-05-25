package com.example.macbookpro.Sopra.View.SousGui.Vues_admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.Date_model;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;
import com.example.macbookpro.Sopra.Models.Salle;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_admin_listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by macbookpro on 28/12/2015.
 */
public class Affichage_rapport extends AppCompatActivity implements OnClickListener {

    // --- Fields ---
    // Contient la liste des sites et des salles, partage avec le controller.
    private Mes_listes_sites_salles mes_listes;
    // Mon listener.
    private Gui_admin_listener myListener;

    private EditText text_date;
    private DatePickerDialog myDatePicker;
    private Date_model date_app;

    private ScrollView sv;
    private LinearLayout layout_salles;

    private static final int DATE_DIALOG_ID = 999;

    /**
     * Constructeur.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On recupere le listener et les listes
        myListener = Controller.get_admin_listener();
        mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();

        sv = new ScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setVerticalScrollBarEnabled(true);
        layout.setOrientation(LinearLayout.VERTICAL);

        System.out.println("Gui, Affichage_rapport, perform generer rapport");

        // On met le nom du site
        System.out.println("On va creer le text view.");
        TextView textView = new TextView(this);
        System.out.println("Site aux : " + mes_listes.getSite_aux().toString());
        textView.setText(mes_listes.getSite_aux().toString());
        textView.setBackgroundColor(Color.rgb(159, 89, 10));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        layout.addView(textView, 0);

        // Composant pour la date
        Date myDate = new Date();
        this.date_app = new Date_model(myDate);
        // On cree le EditText associe
        text_date = new EditText(this);
        text_date.setText(date_app.toString());
        text_date.setInputType(0x00000000); // Enlever la possibilite d'ecrire
        text_date.requestFocus();
        text_date.setOnClickListener(this);

        // On met la date
        Calendar c = Calendar.getInstance();
        date_app = new Date_model(  (short)c.get(Calendar.DAY_OF_MONTH),
                                    (short)(c.get(Calendar.MONTH)+1),
                                    (short)c.get(Calendar.YEAR));
        System.out.println("OK pour la date : " + date_app);
        text_date.setText(date_app.toString());
        setTimeOnEditText();
        layout.addView(text_date,1);

        // On update la view pour les salles
        layout_salles = new LinearLayout(this);
        update_view_salles();

        // Enfin on ajoute ajoute nos layouts
        layout.addView(layout_salles,2);
        sv.addView(layout);
        setContentView(sv);
    }

    private void update_view_salles() {
        // On genere le rapport pour le jour voulu
        myListener.perform_generer_rapport(mes_listes.getSite_aux(),this.date_app);
        // On fait ou refait les composants de la GUI
        layout_salles.removeAllViews();
        layout_salles.setOrientation(LinearLayout.VERTICAL);
        TextView textView;
        int i = 0;
        // On ajoute les salles/infos une a une
        for (Salle salle_actuelle : mes_listes.get_liste_salles()) {
            // On met la salle actuelle
            textView = new TextView(this);
            textView.setText(salle_actuelle.toString());
            textView.setBackgroundColor(Color.rgb(0, 191, 255));
            textView.setTextSize(15);
            layout_salles.addView(textView,i);
            i++;
            // On met le rapport la concernant
            textView = new TextView(this);
            textView.setText(salle_actuelle.getInfo_rapport().toString());
            textView.setBackgroundColor(Color.rgb(135,206,250));
            layout_salles.addView(textView,i);
            i++;
        }
        System.out.println("OK pour l'update view salles");
    }

    /**
     * Changer la date sur la vue.
     */
    private void setTimeOnEditText() {
        myDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date_app = new Date_model((short)dayOfMonth,(short)(monthOfYear+1),(short)year);
                text_date.setText(date_app.toString());
                update_view_salles();
                System.out.println("Rapport : update de la vue");
            }
        },date_app.getAnnee(), date_app.getMois()-1, date_app.getJour());
    }

    /**
     * Action quand on clique sur le EditText : choisir la date.
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == text_date )
            myDatePicker.show();
    }
}
