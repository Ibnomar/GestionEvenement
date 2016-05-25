package com.example.macbookpro.Sopra.View.SousGui.Vues_user;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.macbookpro.Sopra.Models.Creneau;
import com.example.macbookpro.Sopra.Models.Salle;
import com.example.macbookpro.Sopra.View.SousGui.Outils_vues.DateTimePicker.DateWatcher;

import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;
import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_user_listener;

import java.util.Calendar;

/**
 * Created by macbookpro on 03/01/2016.
 */
public class Reservation extends AppCompatActivity implements DateWatcher{
    // Mon listener.
    private Gui_user_listener myListener;
    // Contient la liste des sites et des salles : instance partagee avec le controller.
    private Mes_listes_sites_salles mes_listes;

    // --- Composants ---
    private TextView textView_nom_salle;
    private TextView textView_creneau;
    private Button bouton_debut;
    private Button bouton_fin;
    private Button bouton_reserver;


    // --- Variables ---
    Salle salle_selectionnee;
    Creneau creneau_libre;

    // Pour les heures
    private EditText txt_heure_deb;
    private EditText txt_heure_fin;
    private EditText txt_objet;
    private short heure_debut;
    private short minute_debut;
    private short heure_fin;
    private short minute_fin;
    private boolean heure_debut_OK;
    private boolean heure_fin_OK;


    // TimePicker
    private TimePicker timePicker1;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 999;
    static final int DATE_DIALOG_ID = 998;
    private int test=0;

    // la date
    private int year;
    private int month;
    private int day;
    private DatePicker dpResult;

    /**
     * Constructeur.
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reservation);

        this.heure_debut_OK = false;
        this.heure_fin_OK = false;

        // On recupere le listener et les listes
        myListener = Controller.get_user_listener();
        this.mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();
        // Initialisation du temps
        heure_debut = heure_fin = minute_debut = minute_fin = 0;
        // Initialisation des composants
        textView_nom_salle=(TextView)findViewById(R.id.nom_salle_reservation);
        textView_creneau=(TextView)findViewById(R.id.creneau_reserver);
        bouton_debut=(Button)findViewById(R.id.button_debut);
        bouton_fin=(Button)findViewById(R.id.button_fin);
        bouton_reserver=(Button)findViewById(R.id.button_reserver);
        txt_heure_deb =(EditText)findViewById(R.id.heure_debut);
        txt_heure_deb.setFocusable(false);
        txt_heure_deb.setEnabled(false);
        txt_heure_fin =(EditText)findViewById(R.id.heure_fin);
        txt_heure_fin.setFocusable(false);
        txt_heure_fin.setEnabled(false);
        txt_objet =(EditText)findViewById(R.id.objet_reservation);

        // Recuperation du creneau et de la salle
        salle_selectionnee = mes_listes.getSalle_aux();
        creneau_libre = mes_listes.getCreneau_1();
        // On les affiche
        textView_nom_salle.setText(salle_selectionnee.toString());
        textView_creneau.setText(creneau_libre.toString());

        // ajout de listener heure debut
        listenerdHeuredebut();
        listemerHeureFin();

        // Ajout du listener pour le bouton reserver salle
        listenerBoutonReserverSalle();

        setCurrentTimeOnView();
    }


    /**
     * Ajout du listener pour le bouton reserver salle.
     */
    public void listenerBoutonReserverSalle(){
        bouton_reserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Heure debut:"+heure_debut_OK+ " heure fin:"+heure_fin_OK+
                                    " et objet : "+txt_objet.getText().toString());
                if (heure_fin_OK && heure_fin_OK && !txt_objet.getText().equals("") &&
                                                        !mes_listes.isReservation_faite()) {
                    Creneau creneau_voulu = new Creneau(heure_debut, minute_debut, heure_fin, minute_fin,
                            creneau_libre.getDateModel());
                    if (creneau_libre.peut_contenir(creneau_voulu) && creneau_voulu.isCreneauCorrecte()) {
                        mes_listes.setReservation_faite(true);
                        myListener.reserver_salle(salle_selectionnee, creneau_voulu, txt_objet.getText().toString());
                        Toast.makeText(getApplicationContext(), "Reservation bien effectuée :\n" + creneau_voulu,
                                Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                finish();
                            }
                        }, 4000);
                    } else {
                        Toast.makeText(getApplicationContext(), "Le creneau choisi n'est pas bon", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (!mes_listes.isReservation_faite())
                        Toast.makeText(getApplicationContext(), "Il manque des paramètres", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //=========== Listener pour le bouton heure debut===============
    public void listenerdHeuredebut() {

        bouton_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test=1;
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    //=========== Listener pour le bouton fin de lheure===============
    public void listemerHeureFin() {

        bouton_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test=2;
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    //=========== Permet d'obtenir l'heure courant===============


    public void setCurrentTimeOnView() {
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        // set current time into timepicker
        timePicker1.setCurrentHour(hour);
        timePicker1.setCurrentMinute(minute);
    }


    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    if(test==1){
                        heure_debut=(short)hour;
                        minute_debut=(short)minute;
                        txt_heure_deb.setText(new StringBuilder().append(pad(hour))
                                .append(":").append(pad(minute)));
                        heure_debut_OK = true;
                        System.out.println("Reservation, heure de debut OK");
                    }
                    if(test==2) {
                        System.out.println("Reservation, heure de fin OK");
                        heure_fin=(short)hour;
                        minute_fin=(short)minute;
                        txt_heure_fin.setText(new StringBuilder().append(pad(hour))
                                .append(":").append(pad(minute)));
                        heure_fin_OK = true;
                    }
                    timePicker1.setCurrentHour(hour);
                    timePicker1.setCurrentMinute(minute);
                }
            };


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
        }
    };


    /**
     * receperer le caliendrier courant.
     * @param c
     */
    public void onDateChanged(Calendar c) {
        Log.e("",
                "" + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
                        + " " + c.get(Calendar.YEAR));
    }


}
