package com.example.macbookpro.Sopra.View;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.example.macbookpro.Sopra.Controller.Controller;
import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.Interfaces.GUI_listener.Gui_login_listener;
import com.example.macbookpro.Sopra.View.SousGui.Page_accueil_admin;
import com.example.macbookpro.Sopra.View.SousGui.Page_gerer_profil_user;

/**
 * Created by macbookpro on 01/01/2016.
 */
public class GUI extends AppCompatActivity {

    // Singleton cree des le demarrage.
    private static GUI instance_gui = null;

    private static Context context = null;

    // my listener
    private Gui_login_listener myListener;

    // Composants de texte pour le nickname et le password
    EditText txtNickname, txtPassword;

    public static Context getContext() {
        return context;
    }

    /**
     * Constructeur.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);

        // Mettre le contexte
        this.context = this;

        // On initialise le controller et l'API BDD
        create_controller();

        // On recupere les composants
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        Button logUser = (Button) findViewById(R.id.btnUser);
        Button logAdmin = (Button) findViewById(R.id.btnAdmin);
        myListener = Controller.get_login_listener();

        // Listener pour le bouton de login User
        logUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connexion_user();
            }
        });

        // Listener pour le bouton de login Admin
        logAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connexion_admin();
            }
        });
    }

    /**
     * Methode pour la creation du controller qui creera ensuite l'API implementee.
     */
    private void create_controller() {
        // Creer le controller
        Controller my_controller = Controller.get_controller();
        // Le controller creera lui meme l'API implementee...
    }

    //=================== Methode singleton ====================================

    /**
     * Retourner le GUI.
     * @return GUI.
     */
    public static GUI get_GUI() {
        if (instance_gui == null) {
            instance_gui = new GUI();
        }
        return instance_gui;
    }

    //==========================================================================
    //=========== Methodes pour la transition vers les GUI ================

    /**
     * Methode appelee dans le constructeur  -> Faire la transition de Login a User
     */
    private void transition_vers_guiUser() {
        System.out.println("GUI : Transition vers gerer_profil_user");
        Intent a = new Intent(getContext(), Page_gerer_profil_user.class);
        startActivity(a);
    }

    /**
     * Methode appelee dans le contructeur  -> Faire la transition de Login a Admin
     */
    private void transition_vers_guiAdmin() {
        System.out.println("GUI : Transition vers page_accueil_admin");
        Intent a = new Intent(getContext(), Page_accueil_admin.class);
        startActivity(a);
    }


    //==========================================================================

    /**
     * Tentative de connexion en user, appelee lors du clique sur le bouton User.
     */
    private void connexion_user() {
        System.out.println("Tentative de connexion en user");
        boolean connexion_user_reussie = false;
        if (txtPassword.getText().toString().equals("") || (txtNickname.equals(""))) {
            Toast.makeText(GUI.this, "Les champs ne doivent pas etre vide", Toast.LENGTH_LONG).show();
        }
        else {
            connexion_user_reussie = myListener.perform_connect(txtNickname.getText().toString(),
                                                                txtPassword.getText().toString(),
                                                                false);
            if (connexion_user_reussie) {
                transition_vers_guiUser();
            } else {
                echec_connection(false);
            }
        }
    }

    /**
     * Tentative de connexion en admin, appelee lors du clique sur le bouton Admin.
     */
    private void connexion_admin() {
        System.out.println("Tentative de connexion en admin");
        boolean connexion_admin_reussie = false;
        if (txtPassword.getText().toString().equals("") || (txtNickname.equals(""))) {
            Toast.makeText(GUI.this, "Les champs ne doivent pas etre vide", Toast.LENGTH_LONG).show();
        }
        else {
            connexion_admin_reussie = myListener.perform_connect(txtNickname.getText().toString(),
                                                                 txtPassword.getText().toString(),
                                                                 true);
            if (connexion_admin_reussie) {
                transition_vers_guiAdmin();
            } else {
                echec_connection(true);
            }
        }
    }

    /**
     * Action lors de l'echec d'une connection.
     */
    private void echec_connection(boolean admin) {
        if (admin)
            Toast.makeText(GUI.this, "Erreur d'authentification Admin", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(GUI.this, "Erreur d'authentification User", Toast.LENGTH_LONG).show();
    }
}
