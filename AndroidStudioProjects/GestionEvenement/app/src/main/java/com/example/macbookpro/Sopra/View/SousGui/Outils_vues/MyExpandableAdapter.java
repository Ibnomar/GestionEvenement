package com.example.macbookpro.Sopra.View.SousGui.Outils_vues;

import com.example.macbookpro.Sopra.Models.Creneau;
import com.example.macbookpro.Sopra.Models.Mes_listes_sites_salles;
import com.example.macbookpro.Sopra.Models.Salle;
import com.example.macbookpro.Sopra.R;
import com.example.macbookpro.Sopra.View.SousGui.Vues_user.Reservation;
import com.example.macbookpro.Sopra.View.SousGui.Vues_user.ResulatRechercheUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by macbookpro on 03/01/2016.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<Object> creneauItems;
    private LayoutInflater inflater;
    private ArrayList<Salle> salleItems;
    private ArrayList<Creneau> les_creneaux;
    public Creneau creneau_selectionne;
    public Salle salle_selectionnee;
    //final MyExpandableAdapter context1 = this;
    private final Context context;

    protected Mes_listes_sites_salles pour_envoyer_infos;

    /**
     * Constructeur.
     * @param parents
     * @param childern
     */
    public MyExpandableAdapter(ArrayList<Salle> parents, ArrayList<Object> childern, Context context)
    {
        this.salleItems = parents;
        this.creneauItems = childern;
        this.context = context;
        pour_envoyer_infos = Mes_listes_sites_salles.get_listes_sites_salles();
    }

    public void setInflater(LayoutInflater inflater, Activity activity)
    {
        this.inflater = inflater;
        this.activity = activity;
    }

    /**
     * methode appelle lors de la selection d'un creneau
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent)
    {
        les_creneaux = (ArrayList<Creneau>) creneauItems.get(groupPosition);
        TextView textView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.creneau_view, null);
        }
        // on recupere la reference du textView qui contient le creneau
        textView = (TextView) convertView.findViewById(R.id.textview_creneau);
        textView.setText(les_creneaux.get(childPosition).toString());

        // Ajout du listener sur l'element qu'on peut selectionner
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Dans onclick choix creneau");
                if(!isLastChild)  creneau_selectionne=les_creneaux.get(childPosition);
                else creneau_selectionne=les_creneaux.get(childPosition);
                // appele de la vue de reservation
                Intent i = new Intent(context, Reservation.class);
                pour_envoyer_infos.setCreneau_1(creneau_selectionne);
                pour_envoyer_infos.setSalle_aux(salle_selectionnee);
                // On met le creneau choisi dans la salle
                //salle_selectionnee.setCreneau_choisi(creneau_selectionne);
                // On passe a la fenetre de reservation
                System.out.println("Ma salle et creneau : \n" + creneau_selectionne +"\n"+ salle_selectionnee);
               // Toast.makeText(activity, salle_selectionnee +"et" +creneau_selectionne ,
                //        Toast.LENGTH_SHORT).show();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });
        return convertView;
    }

    /**
     * Methode appelee lorsqu'on choisi la salle qu'on souhaite reserver
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.salle_view, null);
        }
        ((CheckedTextView) convertView).setText(salleItems.get(groupPosition).toString());
        ((CheckedTextView) convertView).setChecked(isExpanded);
        // on fait le test pour verifier s'il y a eu un click sur la salle
        if(isExpanded){
            salle_selectionnee=salleItems.get(groupPosition);
        }
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return ((ArrayList<String>) creneauItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public int getGroupCount()
    {
        return salleItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

}



