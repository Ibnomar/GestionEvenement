package com.example.macbookpro.Sopra.View.SousGui.Vues_user;

import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_user_listener;
import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.Models.*;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.Menu;
import android.content.Intent;
import android.os.Bundle;

import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.SousGui.Outils_vues.MultiSelectionSpinner;

import android.view.MenuItem;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by macbookpro on 22/12/2015.
 */
public class Rechercher_salle_user extends AppCompatActivity  {

    // Declaration des composantes de la vue
    EditText edit_text; // heure debut
    EditText edit_text1; // heure fin
    EditText edit_text3; // date
    protected Spinner spinner;
    protected Button bouton_rechercher;
    protected Button bouton_debut, bouton_fin,date_recherche;
    protected EditText editText_nbPersonnes;
    private CheckBox checkBox_isHeureQuelconque;
    private MultiSelectionSpinner multiSelectionSpinner;


    private Date_model dateModel;


    // Options choisies
    private Options options;
    // Creneau
    public Creneau creneau;
    // TimePicker
    private TimePicker timePicker1;
    static final int TIME_DIALOG_ID = 999;
    static final int DATE_DIALOG_ID = 998;
    private int test=0;

    private int year;
    private int month;
    private int day;
    private DatePicker dpResult;

    // Variables pour les dates et l'heure
    private short heure_deb;
    private short minute_deb;
    private short heure_fin;
    private short minute_fin;
    private boolean heure_debut_OK;
    private boolean heure_fin_OK;


    // Mon listener.
    private Gui_user_listener myListener;
    private Mes_listes_sites_salles mes_listes;
    // mon site.
    Site site_actuel;
    ArrayList<Salle> liste_salles_correspondantes_creneau;


    /**
     * Definir le Listener pour le picker pour la dateModel.
     */
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth+1;
            day = selectedDay;
            // set selected dateModel into textview
            edit_text3.setText(new StringBuilder().append(month)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
            // set selected dateModel into datepicker also
            dpResult.init(year, month, day, null);
        }
    };

    /**
     * Definir le Listener pour le picker des heures.
     */
    private TimePickerDialog.OnTimeSetListener timePickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            int hour = selectedHour;
            int minute = selectedMinute;
            if (test == 1) {
                heure_deb = (short) hour;
                minute_deb = (short) minute;
                edit_text.setText(new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));
            }
            if (test == 2) {
                heure_fin = (short) hour;
                minute_fin = (short) minute;
                edit_text1.setText(new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));
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



    /**
     * Constructeur
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_recherche_salle);

        this.heure_debut_OK = false;
        this.heure_fin_OK = false;

        liste_salles_correspondantes_creneau=new ArrayList<>();
        // Recuperer le listener et les listes
        myListener = Controller.get_user_listener();
        mes_listes = Mes_listes_sites_salles.get_listes_sites_salles();
        // On recupere le site actuel
        site_actuel = mes_listes.getSite_aux();

        // initialiser les options
        options = new Options(false, false, false, false);

        // Identification du champs debut  et fin du creneau.
        edit_text = (EditText) findViewById(R.id.edittext1);
        edit_text.setClickable(false);
        edit_text.setFocusable(false);
        edit_text1 = (EditText) findViewById(R.id.edittext2);
        edit_text1.setClickable(false);
        edit_text1.setFocusable(false);
        edit_text3=(EditText)findViewById(R.id.edittext3);
        edit_text3.setClickable(false);
        edit_text3.setFocusable(false);

        // Checkbox
        checkBox_isHeureQuelconque = (CheckBox)findViewById(R.id.checkBox2);

        // Identification du champs nombre de personne
        editText_nbPersonnes = (EditText) findViewById(R.id.nbPersonne);

        // Identification du bouton rechercher salle.
        bouton_rechercher = (Button) findViewById(R.id.buttonRecherche);
        bouton_debut = (Button) findViewById(R.id.button1);
        bouton_fin = (Button) findViewById(R.id.button2);
        date_recherche=(Button)findViewById(R.id.button3);

        // la liste des options proposees.
        String[] array = {"Visio", "Telephone", "Securise", "Digilab"};
        // Declaration d'un mulitspinner permettant de choisir plusieurs options
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(array);

        // ces methodes ont pour but d'obliger l'utilisateur a mettre le nombre de personne
        // pour pouvoir effectuer la recherhce
        editText_nbPersonnes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bouton_rechercher.setEnabled(!editText_nbPersonnes.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listenerdHeuredebut();
        listenerHeureFin();
        listenerDate();
        boutonRechercherSalle();

        setCurrentTimeOnView();
        setCurrentDateOnView();
    }

    //A l'ecoute du bouton qui permet de renvoyer l'utilisateur vers la page de resultat
    public void boutonRechercherSalle() {
        bouton_rechercher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre_personnes_string = editText_nbPersonnes.getText().toString();
                boolean isHeureQuelconque = checkBox_isHeureQuelconque.isChecked();
                if ((heure_debut_OK && heure_fin_OK && !nombre_personnes_string.equals(""))||isHeureQuelconque) {
                    System.out.println("GUI recherche_salle, Creneau debut : " + heure_deb + "h" + minute_deb);
                    System.out.println("GUI recherche_salle, Creneau fin : " + heure_fin + "h" + minute_fin);

                    boolean is_only_numbers = true;
                    String nb_personnes_string = edit_text.getText().toString();
                    if (nb_personnes_string.length() == 0) is_only_numbers = false;
                    int nombre_personnes = 0;
                    if (is_only_numbers) {
                        try {
                            nombre_personnes = Integer.parseInt(nombre_personnes_string);
                            System.out.println("nombre de personnes : " + nombre_personnes);
                        } catch (NumberFormatException nfe) {
                            Toast.makeText(getApplicationContext(), "Vous n'avez pas entré un nombre",
                                    Toast.LENGTH_LONG).show();
                            is_only_numbers = false;
                        }
                    }
                    if (is_only_numbers) {
                        Mise_a_jour_options();

                        dateModel = new Date_model((short) day, (short) month, (short) year);
                        System.out.println("dateModel new : " + dateModel);

                        boolean isCorrect = true;
                        if (isHeureQuelconque) {
                            creneau = new Creneau(dateModel);
                            System.out.println("Heure quelconque");
                        } else {
                            creneau = new Creneau(heure_deb, minute_deb, heure_fin, minute_fin, dateModel);
                            if (!creneau.isCreneauCorrecte()) {
                                isCorrect = false;
                            }
                            //String nombre_personne a ajouter dans la methode
                            System.out.println("creneau new : " + creneau);
                        }

                        if (isCorrect) {
                            // effectuer la recherche des salles disponibles
                            System.out.println("GUI, recherche de salles");
                            myListener.perform_recherche_salle(site_actuel, creneau, options, nombre_personnes);
                            System.out.println("GUI, recherche de salles terminee");

                            if (mes_listes.get_liste_salles().size() == 0) {
                                // On a trouve aucune salle disponible
                                Toast.makeText(getApplicationContext(), "Aucune salle disponible", Toast.LENGTH_LONG).show();
                            } else {
                                // On affiche le resultat de la recherche
                                Intent i = new Intent(getApplicationContext(), ResulatRechercheUser.class);
                                startActivity(i);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Les creneaux sont incorrectes",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Parametres incorrectes",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Bouton a l'ecoute du bouton pour le debut du creneau.
     */

    public void listenerdHeuredebut() {
        bouton_debut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // bouton_debut_creneau();
                test = 1;
                showDialog(TIME_DIALOG_ID);
                heure_debut_OK = true;
            }
        });
    }

    /**
     * Bouton a l'ecoute du bouton pour la fin du creneau.
     */

    public void listenerHeureFin() {
        bouton_fin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                test=2;
                showDialog(TIME_DIALOG_ID);
                heure_fin_OK = true;
            }
        });
    }

    public void listenerDate(){
        date_recherche.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    /**
     * Mettre a jour les options.
     */
    public void Mise_a_jour_options() {
        System.out.println("mise a jour option ");
        List<String> Options_choisis = new ArrayList<String>();
        Options_choisis = multiSelectionSpinner.getSelectedStrings();
        options = new Options(false,false,false,false);
        for (int i = 0; i < Options_choisis.size(); i++) {
            if (Options_choisis.get(i).equals("Visio")) options.setVisio(true);
            if (Options_choisis.get(i).equals("Telephone")) options.setTelephone(true);
            if (Options_choisis.get(i).equals("Securise")) options.setSecurise(true);
            if (Options_choisis.get(i).equals("Digilab")) options.setDigilab(true);
        }
    }

    /**
     * Pour la gestion des options.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Pour la gestion des options.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCurrentTimeOnView() {

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(true);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // set current time into timepicker
        timePicker1.setCurrentHour(hour);
        timePicker1.setCurrentMinute(minute);

        // Heure debut
        edit_text.setText(new StringBuilder().append(pad(hour))
                .append(":").append(pad(minute)));

        edit_text1.setText(new StringBuilder().append(pad(hour))
                .append(":").append(pad(minute)));

    }

    /**
     * Afficher la dateModel sur la gui.
     */
    public void setCurrentDateOnView() {
        dpResult = (DatePicker)findViewById(R.id.dpResult);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        // set current dateModel into datepicker
        dpResult.init(year, month, day, null);

        edit_text3.setText(new StringBuilder().append(month)
                .append("-").append(day).append("-").append(year)
                .append(" "));
    }

    /**
     * Creer le dialog -> DatePicker ou TimerPicker.
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener,
                                            year, month,day);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timePickerListener,
                                            heure_deb, minute_deb, false);
        }
        return null;
    }

}
