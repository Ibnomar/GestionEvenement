package com.example.macbookpro.Sopra.API;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by root on 11/01/16.
 */
public class Gestion_database {
    private SQLiteDatabase ma_database;

    /**
     * Constructeur.
     * @param ma_database
     */
    public Gestion_database(SQLiteDatabase ma_database) {
        this.ma_database = ma_database;
    }

    /**
     * Supprimer les tables.
     */
    public void supprimer_tables () {
        ma_database.execSQL("DROP TABLE if exists Sites");
        ma_database.execSQL("DROP TABLE if exists Salles");
        ma_database.execSQL("DROP TABLE if exists Composee_options");
        ma_database.execSQL("DROP TABLE if exists Reservations");
        ma_database.execSQL("DROP TABLE if exists Users");
        ma_database.execSQL("DROP TABLE if exists Options");
    }

    /**
     * Creer les tables.
     */
    public void creer_tables() {
        // Sites
        System.out.println("Creation table : sites");
        ma_database.execSQL(
            "create table Sites(" +
                    "id_site INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nom_site TEXT UNIQUE"+
                    ");"
        );
        // Salles
        System.out.println("Creation table : salles");
        ma_database.execSQL(
                "create table Salles(" +
                        "id_salle INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_site UNSIGNED INTEGER REFERENCES Sites(ID_site)," +
                        "nb_personne_max UNSIGNED INTEGER," +
                        "nom_salle TEXT UNIQUE," +
                        "nb_requete UNSIGNED INTEGER," +
                        "nb_connection UNSIGNED INTEGER" +
                        ");"
        );
        // Composee_options
        System.out.println("Creation table : Composee_options");
        ma_database.execSQL(
            "create table Composee_options(" +
                    "id_compo INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_salle UNSIGNED INTEGER REFERENCES Salles(id_salle)," +
                    "id_option UNSIGNED INTEGER REFERENCES Optionq(id_option)" +
                    ");"
        );
        // Reservations
        System.out.println("Creation table : reservations");
        ma_database.execSQL(
            "create table Reservations(" +
                    "id_reservation INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_salle UNSIGNED INTEGER REFERENCES Salles(id_salle)," +
                    "date_reservation TEXT," +
                    "heure_debut TEXT," +
                    "heure_fin TEXT," +
                    "objet TEXT," +
                    "nom TEXT" +
                    ");"
        );
        // Users
        System.out.println("Creation table : Users");
        ma_database.execSQL(
            "create table Users(\n" +
                    "id_user INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_site UNSIGNED INTEGER REFERENCES sites(id_site)," +
                    "pseudo TEXT UNIQUE," +
                    "password TEXT," +
                    "is_admin BOOLEAN" +
                    ");"
        );
        // Options
        System.out.println("Creation table :Options");
        ma_database.execSQL(
                "create table Options(" +
                        "id_option INTEGER PRIMARY KEY," +
                        "nom_option TEXT" +
                        ");"
        );
    }

    /**
     * Inserer des elements dans la table pour le test.
     */
    public void inserer_elements_tables() {

        /******************INSERTION DANS USERS************************************/
        ma_database.execSQL("INSERT INTO \"Users\" VALUES(1,2,'jason','mdp','true');");
        ma_database.execSQL("INSERT INTO \"Users\" VALUES(2,1,'aminata','mdp','false');");
        ma_database.execSQL("INSERT INTO \"Users\" VALUES(3,2,'ines','mdp','false');");
        ma_database.execSQL("INSERT INTO \"Users\" VALUES(4,2,'mamadou','mdp','false');");
        ma_database.execSQL("INSERT INTO \"Users\" VALUES(5,3,'inconnu','mdp','false');");
        ma_database.execSQL("INSERT INTO \"Users\" VALUES(6,1,'jas','x','true');");

        /******************INSERTION DANS RESERVATIONS************************************/
        // salle 101 le 15, de 17h à 18h
        ma_database.execSQL("INSERT INTO \"Reservations\" VALUES(2,2,'2016-15-01','17h00','18h00','Examen', 'Albert');");
        // salle 101 le 16, de 16h à 18h
        ma_database.execSQL("INSERT INTO \"Reservations\" VALUES(3,2,'2016-16-01','16h00','18h00','Lecture','Caroline');");
        // salle de conference le 15, de 9h à 10h30
        ma_database.execSQL("INSERT INTO \"Reservations\" VALUES(1,1,'2016-15-01','09h00','10h30','Conference','Malou');");
        // salle de conference le 15, de 15h30 à 16h30
        ma_database.execSQL("INSERT INTO \"Reservations\" VALUES(4,1,'2016-15-01','15h30','16h30','Travail','Jean');");

        /******************INSERTION DANS SITES************************************/
        ma_database.execSQL("INSERT INTO Sites VALUES (1,'Sopra1');");
        ma_database.execSQL("INSERT INTO Sites VALUES (2,'Sopra2');");
        ma_database.execSQL("INSERT INTO Sites VALUES (3,'Sopra3');");
        ma_database.execSQL("INSERT INTO Sites VALUES (4,'Sopra4');");

        /******************INSERTION DANS SALLES************************************/
        ma_database.execSQL("INSERT INTO Salles VALUES (1,1,100,'salle conference',0,0);");
        // La salle de conf a en options :   visio
        ma_database.execSQL("INSERT INTO Salles VALUES (2,1,100,'salle 101',0,0);");
        ma_database.execSQL("INSERT INTO Salles VALUES (3,1,100,'salle 102',0,0);");
        ma_database.execSQL("INSERT INTO Salles VALUES (4,2,100,'amphi vinci',0,0);");
        ma_database.execSQL("INSERT INTO Salles VALUES (5,3,100,'reunion1',0,0);");
        ma_database.execSQL("INSERT INTO Salles VALUES (6,1,666,'chambre des secrets',0,0);");


        /******************INSERTION DANS OPTIONS************************************/
        ma_database.execSQL("INSERT INTO Options (id_option,nom_option) VALUES (1,'visio');");
        ma_database.execSQL("INSERT INTO Options (id_option,nom_option) VALUES(2,'telephone');");
        ma_database.execSQL("INSERT INTO Options (id_option,nom_option) VALUES(3,'securise');");
        ma_database.execSQL("INSERT INTO Options (id_option,nom_option) VALUES(4,'digilab');");

        /******************INSERTION DANS COMPOSEE_OPTIONS************************************/
        // id - id_salle - id_option
        ma_database.execSQL("INSERT INTO Composee_options VALUES (1,1,1);");
        ma_database.execSQL("INSERT INTO Composee_options VALUES (2,1,2);");
        ma_database.execSQL("INSERT INTO Composee_options VALUES (3,2,1);");
        ma_database.execSQL("INSERT INTO Composee_options VALUES (4,2,3);");
        ma_database.execSQL("INSERT INTO Composee_options VALUES (5,3,3);");
        ma_database.execSQL("INSERT INTO Composee_options VALUES (6,3,4);");
        ma_database.execSQL("INSERT INTO Composee_options VALUES (7,4,2);");
        ma_database.execSQL("INSERT INTO Composee_options VALUES (8,5,4);");

    }
}
