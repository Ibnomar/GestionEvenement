package com.example.macbookpro.Sopra.View.Interfaces.GUI_listener;

/**
 * Created by macbookpro on 01/01/2016.
 */
public interface Gui_login_listener {

    /**
     * Essayer de se connecter en tant qu'user ou admin.
     * @param pseudo
     * @param password
     * @param admin true si on veut se connecter en admin.
     */
    public boolean perform_connect(String pseudo, String password, boolean admin);

}
