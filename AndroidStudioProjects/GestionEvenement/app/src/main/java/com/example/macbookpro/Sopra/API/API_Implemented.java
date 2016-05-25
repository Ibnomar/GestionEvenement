package com.example.macbookpro.Sopra.API;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.macbookpro.Sopra.Models.Creneau;
import com.example.macbookpro.Sopra.Models.Date_model;
import com.example.macbookpro.Sopra.Models.Info_rapport;
import com.example.macbookpro.Sopra.Models.Options;
import com.example.macbookpro.Sopra.Models.Salle;
import com.example.macbookpro.Sopra.Models.Site;
import com.example.macbookpro.Sopra.View.GUI;

import java.util.ArrayList;

public class API_Implemented implements API_BDD{

	private static API_Implemented my_api = null;
    private Context myContext;
    private SQLiteDatabase ma_database;

    public SQLiteDatabase db;

	/**
	 * Constructeur prive.
	 */
	private API_Implemented() {
        // Recuperer le context
        myContext = GUI.getContext();
        // Creer le handler
        ma_database = myContext.openOrCreateDatabase("ma_database_sopra2.db",
                SQLiteDatabase.CREATE_IF_NECESSARY,null);
        System.out.println("*** API *** Data base ouverte : " + ma_database.getPath());

        // On ouvre l'outil de gestion de database
        Gestion_database gestion_database = new Gestion_database(ma_database);
        // On supprime les anciennes tables si il y avait
        System.out.println("*** API *** SUPPRESSION DES TABLES ");
        gestion_database.supprimer_tables();
        // On cree les nouvelles tables
        System.out.println("*** API *** CREATION DES TABLES ");
        gestion_database.creer_tables();
        // On ajoute des elements
        System.out.println("*** API *** INSERTION DES ELEMENTS ");
        gestion_database.inserer_elements_tables();

        // Ensuite on test pour voir les users qu'on a mis
        System.out.println("On affiche les users de la database");
        Cursor cursor = ma_database.rawQuery("SELECT pseudo FROM Users ", null);
        while (cursor.moveToNext()) {
            System.out.println(" ---- "+cursor.getString(0));
        }
        System.out.println("*** API *** data inserees ");

		System.out.println("**** TEST123 ****" );

		System.out.println("*** FIN123 ***");
    }

	/**
	 * Methode singleton.
	 * @return
	 */
	public static API_BDD get_API_BDD() {
		if (my_api == null) {
			my_api = new API_Implemented();
		}
		return my_api;
	}


	//un admin peut se connecter en temps qu'utilisateur, mais pas l'inverse!
//-----------------------------------------------------------------------------------------------------
	// OK
	public boolean user_existe(String Pseudo, String Password, Boolean admin) {
		System.out.println("**** API **** tentative de connexion en user");
		String str_is_admin = Boolean.toString(admin);
		System.out.println("**** API **** is_admin: " + str_is_admin);
		boolean retour=false;

        Cursor c = ma_database.rawQuery("select pseudo, password, is_admin from Users where pseudo=? and password=?",
                                        new String[]{Pseudo, Password});
        try{
            c.moveToFirst();
            String recup_pseudo = c.getString(0);
            String recup_mdp = c.getString(1);
            String  recup_is_admin= c.getString(2);
            System.out.println("pseudo/mdp/isadmin : "+recup_pseudo+" "+recup_mdp+" "+recup_is_admin);
            if (recup_pseudo.equals(Pseudo) & recup_mdp.equals(Password)) {
                if (admin) {
                    if (recup_is_admin.equals(str_is_admin)) {
                        retour = true;
                    }
                } else {
                    retour = true;
                }
            }
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        if (retour){
            if (admin) {
                System.out.println("Connexion de " + Pseudo +" en temps qu'admin reussie");
            }
            else {
                System.out.println("Connexion de " + Pseudo +" en temps qu'utilisateur reussie");
            }
        }
        else {
            if(!admin && retour){
                System.out.println( Pseudo +" ne peut pas se connecter en temps qu'administrateur");
            }
        }
        return retour;
	}

//----------------------------------------------------------------------------------------------------------
	// OK
	public ArrayList<Site> get_list_sites(){
		ArrayList<Site> list= new ArrayList<Site>();
		Cursor c = ma_database.rawQuery("SELECT nom_site FROM Sites;",null);
		int i=0;
		while (c.moveToNext()) {
			System.out.println(" ---- " + c.getString(0));
			String nom_site = c.getString(0);
			Site s = new Site(nom_site);
			list.add(s);
			System.out.println();
		}
		return list;
	}

//---------------------------------------------------------------------------------------------------------------
    // OK
    public Options get_options(String nomSalle){
		ArrayList<String> listOptions = new ArrayList<String>();
		Options o = new Options(false, false, false, false);
		int id_salle = 0;
		Cursor c = null;
		try {
			c = ma_database.rawQuery("select id_salle from Salles where nom_salle =?", new String[]{nomSalle});
			c.moveToFirst();
			id_salle = c.getInt(0);
		} catch (Exception e) {
			System.out.println("Nom de salle invalide");
			return o;
		}
		ArrayList<Integer> l = new ArrayList<Integer>();

		try {
			String id_sal = Integer.toString(id_salle);
			c = ma_database.rawQuery("select id_option from composee_options where id_salle=?", new String[]{id_sal});
			while (c.moveToNext()) {
				int id_option = c.getInt(0);
				l.add(id_option);
			}
			//select nom_option from Options where id_option=2 or id_option=3;
			for (int i = 0; i < l.size(); i++) {
				String id_opt = Integer.toString(l.get(i));
				c = ma_database.rawQuery("select nom_option from Options where id_option=?", new String[]{id_opt});
				c.moveToFirst();
				String Nom_option = c.getString(0);
				listOptions.add(Nom_option);
				switch (Nom_option) {
					case ("visio"): {
						o.setVisio(true);
						break;
					}
					case ("telephone"): {
						o.setTelephone(true);
						break;
					}
					case ("securise"): {
						o.setSecurise(true);
						break;
					}
					case ("digilab"): {
						o.setDigilab(true);
						break;
					}
				}
			} // fin du for
		} catch (Exception e) {
			System.out.println("Aucune option pour cette salle");
			return o;
		}
		//System.out.println("Operation done successfully");
		return o;
}
	
//--------------------------------------------------------------------------------------------------

    // OK
	public ArrayList<Salle> get_list_salles(String nomSite){
		ArrayList<Salle> list= new ArrayList<Salle>();
	    try {
			try{
				Cursor c = ma_database.rawQuery("SELECT id_site FROM Sites where nom_site='"+nomSite+"';",null);
				int id_site;
				if (c.moveToFirst()) {
					id_site = c.getInt(0);
					c = ma_database.rawQuery("select * from Salles where id_site=" +id_site,null);
                    while ( c.moveToNext() ) {
						String nom_salle = c.getString(3);
						System.out.println("API : toutes les salles du site : "+nom_salle);
						int nb_personne_max = c.getInt(2);
						int  nb_requete = c.getInt(4);
						int  nb_connexion = c.getInt(5);

						Options options = this.get_options(nom_salle);
						Info_rapport info = new Info_rapport(nb_requete,nb_connexion);
						Salle s = new Salle(nom_salle,options,(short)nb_personne_max,info);
						list.add(s);
					}
				}
			} catch (Exception e){
				System.out.println("nom site invalide");
				list =null;
				return list;
			}
	    } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
	   // System.out.println("Operation done successfully");
		System.out.println("Api : get list nous a retourne une liste de taille "+list.size());
		return list;
		
	}

//----------------------------------------------------------------------------------------------------------

    // OK
	public ArrayList<Salle> get_list_salles(String nomSite, Options options_voulues){
		ArrayList<Salle> list_salles = this.get_list_salles(nomSite);
		ArrayList<Salle> list = new ArrayList<>();
		System.out.println("API : Debut get list salles avec les options");
		// On enleve les salles qui n'ont pas les options voulues
		int i = 0;
		for(Salle salle : list_salles) {
			Options opt= get_options(salle.getNom_salle());
			System.out.println("API : --- salle : "+salle.getNom_salle());
			if (opt.comprendLesOptions(options_voulues)) {
				list.add(salle);
			}
		}
		// On affiche les salles disponibles en println
		if (list.size()==0) {
			System.out.println("Aucune salle dispo avec les options :" +options_voulues.toString());
		} else {
			System.out.println("\nSalles disponibles avec les options :"+ options_voulues.toString());
			for(int b=0; b<list.size(); b++){
				System.out.println(list.get(b).getNom_salle()+" ");
			}
			try{
			    for(int b=0; b<list.size(); b++){
					// Update de nombre de connexion
					String requete2 = "UPDATE Salles set nb_connection = nb_connection + 1 WHERE Salles.nom_salle ='"+list.get(b).getNom_salle()+"';";
					ma_database.execSQL(requete2);
					// afficher le nouveau nombre de connexions
					String requete3 = "SELECT nb_connection FROM Salles WHERE nom_salle = "+
									"'"+list.get(b).getNom_salle()+"'";
					Cursor c = ma_database.rawQuery(requete3,null);
					c.moveToFirst();
					int nb_connection = c.getInt(c.getColumnIndex("nb_connection"));
					System.out.println("nb connexion après modif:" + nb_connection);
			    }
			}
			catch ( Exception e ) {
			    System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
		return list;
}

//-------------------------------------------------------------------------------------------------------------------

	// OK
	public void add_salle(Site site, Salle nouvelle_salle) {
        Cursor c;
        String requete = "SELECT id_site FROM Sites WHERE nom_site = "+"'"+site.getNom_site()+"'";
        c = ma_database.rawQuery(requete, null);
        c.moveToFirst();
        int id_site = c.getInt(c.getColumnIndex("id_site"));

        requete = "INSERT INTO Salles (id_site, nb_personne_max, nom_salle) VALUES ("+id_site+", "+
                    nouvelle_salle.getNombre_personne_max()+", "+"'"+nouvelle_salle.getNom_salle()+"')";
        ma_database.execSQL(requete);
	}
	
//-------------------------------------------------------------------------------------------------------------------

	public void reserver_salle(String nom_salle, Creneau res, String objet, String nom_personne){
		try{
		    String requete = "SELECT id_salle,nb_requete FROM Salles WHERE nom_salle = "+"'"+nom_salle+"'";
		    Cursor c = ma_database.rawQuery(requete,null);
			c.moveToFirst();
		    int id_salle = c.getInt(c.getColumnIndex("id_salle"));
		    int nb_req = c.getInt(c.getColumnIndex("nb_requete"));
			System.out.println("nb requetes: "+nb_req);
		    System.out.println("id_salle: "+id_salle);
		    
		    String heure_deb = Short.toString(res.getHeure_debut())+"h"+Short.toString(res.getMinute_debut());
		    String h_fin = Short.toString(res.getHeure_fin())+"h"+Short.toString(res.getMinute_fin());
		    String date = res.getDateModel().getAnnee()+"-"+res.getDateModel().getJour()+"-"+res.getDateModel().getMois();
		    System.out.println("API : insertion de la reservation");
		    requete = "INSERT INTO Reservations (id_salle,date_reservation,heure_debut,heure_fin,objet,nom) VALUES ("
					+id_salle+",'"+date+"','"+heure_deb+"','"+h_fin+"','"+objet+"','"+nom_personne+"')";
		    ma_database.execSQL(requete);
			System.out.println("API : insertion de la reservation finie");
		    requete = "UPDATE Salles set nb_requete = nb_requete + 1 WHERE Salles.nom_salle ="+
							"'"+nom_salle+"'";
			ma_database.execSQL(requete);
	
			requete = "SELECT nb_requete FROM Salles WHERE nom_salle = "+"'"+nom_salle+"'";
			c = ma_database.rawQuery(requete,null);
			c.moveToFirst();
			int nb_req2 = c.getInt(c.getColumnIndex("nb_requete"));
			System.out.println("nb requetes: " + nb_req2);
		}
		catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		//System.out.println("Operation done successfully");

	}

//-------------------------------------------------------------------------------------------------------------------

	// OK
	public ArrayList<Creneau> get_liste_reservations(String nomSalle, Date_model date_model_voulue){
		String format = "yyyy-dd-MM";
		String format2 = "dd/MM/yyyy";
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
		java.text.SimpleDateFormat formaterr = new java.text.SimpleDateFormat( format2 );
		java.util.Date datee = new java.util.Date();
		String date_journee=formater.format( datee );
		String date_journee_ok=formaterr.format( datee );
		//System.out.println( date_journee );

		System.out.println("API : Get_list_reservation");

		ArrayList<Creneau> list = new ArrayList<Creneau>();

		Cursor c;
		try{
			c = ma_database.rawQuery("SELECT id_salle FROM Salles where nom_salle='" + nomSalle + "';",null);
			c.moveToFirst();
			int id_salle = c.getInt(0);
			c = ma_database.rawQuery("select heure_debut, heure_fin, date_reservation from Reservations where id_salle='" + id_salle + "'", null);

			while ( c.moveToNext() ) {
				String heure_debut = c.getString(0);
				String heure_fin = c.getString(1);
				String date=c.getString(2);

				String annee=date.substring(0, 4);
				// System.out.println(annee);
				short Annee = Short.parseShort(annee);

				int fin=date.lastIndexOf("-");
				String mois=date.substring(5, fin);
				//System.out.println(mois);
				short Mois = Short.parseShort(mois);

				String jour=date.substring(date.lastIndexOf("-")+1,date.length());
				// System.out.println(jour);
				short Jour = Short.parseShort(jour);

				Date_model d=new Date_model(Mois,Jour,Annee);

				// Si la reservation est le meme jour que celui voulu, on va ajouter cette reservation
				// a la liste
				if (d.equals(date_model_voulue)) {
					System.out.println("API : reservation avec la bonne date trouvee");
					// Minute debut
					fin = heure_debut.indexOf("h");
					String mindeb = heure_debut.substring(fin + 1);
					short Mindeb = Short.parseShort(mindeb);
					// Heure debut
					String hdeb = heure_debut.substring(0, 2);
					short Hdeb = Short.parseShort(hdeb);
					// Minute fin
					fin = heure_fin.indexOf("h");
					String minfin = heure_fin.substring(fin + 1);
					short Minfin = Short.parseShort(minfin);
					// Heure fin
					String hfin = heure_fin.substring(0, 2);
					short Hfin = Short.parseShort(hfin);
					// Ajout de la reservation a la liste
					Creneau cn = new Creneau(Hdeb, Mindeb, Hfin, Minfin, d);
					list.add(cn);
					System.out.println(cn.toString());
				}
			}
		} catch (Exception e) {
			System.out.println("API : nom salle invalide");
			list =null;
			return list;
		}

		if (list == null)
			System.out.println("La liste n'est pas nulle");
		else
			System.out.println("La liste est nulle");
		System.out.println("API : Get_list_reservation fini");

		return list;
	}
	
//-------------------------------------------------------------------------------------------------------------------------

    // OK
	public Site get_ancien_site(String Pseudo) {

        String requete = "SELECT nom_site FROM Sites,Users" +
                        " WHERE Users.pseudo ="+"'"+Pseudo+"'"+
                        " AND Users.id_site = Sites.id_site";
        Cursor c = ma_database.rawQuery(requete,null);
        c.moveToFirst();
        String nom_site = c.getString(c.getColumnIndex("nom_site"));
        System.out.println(("API -> l'ancien site de l'utilisateur est : "+nom_site));
        Site ancien_site = new Site(nom_site);

        return ancien_site;
	}

//------------------------------------------------------------------------------------------------------------------------

	// OK
	public void update_site_user(String pseudo, Site site) {
	        try {
	            String requete = "SELECT id_site FROM Sites WHERE nom_site ="+"'"+site.getNom_site()+"'";
	            Cursor c = ma_database.rawQuery(requete,null);
				c.moveToFirst();
				int id_site = c.getInt(0);
				System.out.println("Id du nouveau site : "+id_site);

	            try {
	                requete = "UPDATE Users set id_site = "+"'"+id_site+"' WHERE pseudo = "+"'"+pseudo+"'" ;
	                ma_database.execSQL(requete);
	            } catch (Exception e) {
	                System.out.println("Update échoué");
	            }
	        }catch ( Exception e ) {
	            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        }
	        System.out.println("Operation done successfully");
	}
		
//---------------------------------------------------------------------------------------------------------------------------
		
    public void update_salle(Salle ancienne_salle, Salle nouvelle_salle) {
		try {
			System.out.println("*****debut update salle");
			Cursor c;
			String requete;
			requete = "SELECT id_salle FROM Salles WHERE nom_salle = "+"'"+ancienne_salle.getNom_salle()+"'";
			c = ma_database.rawQuery(requete, null);
			c.moveToFirst();
			int id_salle = c.getInt(0);
			System.out.println("*****id salle"+id_salle);

			//We first update the salle name and the nb_persone_max
			requete = "UPDATE Salles set nom_salle="+"'"+nouvelle_salle.getNom_salle()+"',"
					+ "nb_personne_max ="+"'"+nouvelle_salle.getNombre_personne_max()+"'"
					+ "WHERE id_salle = "+"'"+id_salle+"'";
			ma_database.execSQL(requete);

			System.out.println("*****test1");

			//Then we update options of the room
			requete = "DELETE FROM Composee_options WHERE id_salle = "+"'"+id_salle+"'";
			ma_database.execSQL(requete);
			if(nouvelle_salle.getOptions().aVisio() == true) {
				System.out.println("*****test2");

				c = ma_database.rawQuery("SELECT id_option FROM Options WHERE nom_option = 'visio'", null);
				c.moveToFirst();
				int id_option = c.getInt(0);
				c= ma_database.rawQuery("SELECT * FROM Composee_options WHERE id_salle= "+"'"+id_salle+"' AND id_option ="+id_option, null);
				if(!c.moveToNext()){
					ma_database.execSQL("INSERT into Composee_options (id_option, id_salle) VALUES ("+"'"+id_option+"', "+"'"+id_salle+"')");
				}
				System.out.println("*****test3");
			}
			if(nouvelle_salle.getOptions().aTelephone() == true) {
				c = ma_database.rawQuery("SELECT id_option FROM Options WHERE nom_option = 'telephone'", null);
				c.moveToFirst();
				int id_option = c.getInt(0);
				c = ma_database.rawQuery("SELECT * FROM Composee_options WHERE id_salle= " + "'" + id_salle + "' AND id_option = " + id_option, null);
				if(!c.moveToNext()){
					ma_database.execSQL("INSERT into Composee_options (id_option, id_salle) VALUES ("+"'"+id_option+"', "+"'"+id_salle+"')");
				}
			}
			if(nouvelle_salle.getOptions().aSecurise() == true) {
				c = ma_database.rawQuery("SELECT id_option FROM Options WHERE nom_option = 'securise'", null);
				c.moveToFirst();
				int id_option = c.getInt(0);
				c = ma_database.rawQuery("SELECT * FROM Composee_options WHERE id_salle= "+"'"+id_salle+"' AND id_option ="+id_option, null);
				if(!c.moveToNext()) {
					ma_database.execSQL("INSERT into Composee_options (id_option, id_salle) VALUES ("+"'"+id_option+"', "+"'"+id_salle+"')");
				}
			}
			if(nouvelle_salle.getOptions().aDigilab() == true) {
				c = ma_database.rawQuery("SELECT id_option FROM Options WHERE nom_option = 'digilab'", null);
				c.moveToFirst();
				int id_option = c.getInt(0);
				c = ma_database.rawQuery("SELECT * FROM Composee_options WHERE id_salle= " + "'" + id_salle+"' AND id_option = "+id_option, null);
				if(!c.moveToNext()){
					ma_database.execSQL("INSERT into Composee_options (id_option, id_salle) VALUES ("+"'"+id_option+"', "+"'"+id_salle+"')");
				}
			}
		}catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}


//----------------------------------------------------------------------------------------------------------------------------

    // OK
    public void update_site(Site ancien_site, Site nouveau_site) {
        System.out.println("Debut de l'update");
        String requete = "UPDATE Sites set nom_site = "+"'"+nouveau_site.getNom_site()+"'"+
                        "WHERE Sites.nom_site ="+"'"+ancien_site.getNom_site()+"'";
        ma_database.execSQL(requete);
        System.out.println("Fin de l'update");
    }


//--------------------------------------------------------------------------------------------------------------------------

    // OK
    public void add_site(Site site) {
        try {
            ma_database.execSQL("INSERT INTO Sites (nom_site) VALUES (" + "'" + site.getNom_site() + "')");
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

//---------------------------------------------------------------------------------------------------------------------------

	// OK
    public void supprimer_site(Site site) {
		try {
			System.out.println("debut");
			Cursor c;
			c = ma_database.rawQuery("SELECT nom_site FROM Sites WHERE Sites.nom_site = " +
					"'" + site.getNom_site() + "'", null);
			c.moveToFirst();
			String nom_site = c.getString(0);
			ma_database.execSQL("DELETE FROM Sites WHERE nom_site =" + "'" + nom_site + "'");

			c = ma_database.rawQuery("SELECT * FROM Sites", null);
			while (c.moveToNext()) {
				System.out.println("\n nom site: "+c.getString(0));
			}
		}catch(Exception e){
			System.out.println(e);
		}
    }

//---------------------------------------------------------------------------------------------------------------------------

	// OK
    public void supprimer_salle(Salle salle) {
		try {
			System.out.println("debut");
			Cursor c;

			c = ma_database.rawQuery("SELECT nom_salle FROM Salles WHERE Salles.nom_salle = " + "'" + salle.getNom_salle() + "'", null);
			System.out.println("****TEST");
			c.moveToFirst();
			String nom_salle = c.getString(0);
			System.out.println("nom Salle :" + nom_salle);
			ma_database.execSQL("DELETE FROM Salles WHERE nom_salle =" + "'" + nom_salle + "'");


			c = ma_database.rawQuery("SELECT * FROM Salles",null);
			while(c.moveToNext()) {
				System.out.println("nom Salle :"+ c.getString(0));
			}


		}catch ( Exception e ) {
			System.out.println("");
		}
		System.out.println("Suppression de salle, done successfully");
    }

//--------------------------------------------------------------------------------------------------------------------
	
	public static void main( String args[] )
	  {
		API_Implemented c = new API_Implemented();
		//boolean ok = c.user_existe("jason", "mdp", true);
		//boolean ok = c.user_existe("jasson", "mddp", true);
		
		//ArrayList<Site> list = c.get_list_sites();		
		//ArrayList<Salle> list = c.get_list_salles("INSA");
		//Options o= c.get_options("Vinci");
		//System.out.println("Telephone:"+o.aTelephone()+" digilab:"+o.aDigilab()+ " securisée:"+o.aSecurise()+ " visio:"+o.aVisio());
		
		//ArrayList<Creneau> lc = c.get_liste_reservation("exam B");  
		
		
		//Options optV=new Options(false,false,false,false);		
		//ArrayList<Salle> Salles_dispo = c.get_list_salles("INSA", optV);
	
	    /*TEST ADD_SALLE
	    Options option1 = new Options(true, false, true, false);
	    
	    Salle nouvelle_salle = new Salle("tartre",option1,(short)222);
	    Site site = new Site("INSA");
	    try{
	    c.add_salle(site, nouvelle_salle);
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }*/
	    //---------------------------------------------------------------------
	    
		//Pour tester reserver_salle

		/*short j=31;
		short m=12;
		short a=2015;
		Date_model d= new Date_model(j,m,a);
		short hd=15;
		short md=0;
		short hf=16;
		short mf=0;
		Creneau cr=new Creneau(hd,md,hf,mf,d);
		c.reserver_salle(new Salle("Vinci",option1,(short)50), cr);
		ArrayList<Creneau> lc = c.get_liste_reservation("Vinci");*/
	    
	  }




	


}
