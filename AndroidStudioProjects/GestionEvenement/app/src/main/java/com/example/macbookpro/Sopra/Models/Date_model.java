package com.example.macbookpro.Sopra.Models;

import java.util.Date;

public class Date_model {
	
	private short jour, mois, annee;

	/**
	 * Constructeur.
	 * @param jour
	 * @param mois
	 * @param annee
	 */
	public Date_model(short jour, short mois, short annee){
		this.jour=jour;
		this.mois=mois;
		this.annee=annee;
	}

	public Date_model(Date date) {
		this.jour = (short)date.getDay();
		this.mois = (short)date.getMonth();
		this.mois = (short)date.getYear();
	}
	
	/**
	 * Methode toString.
	 * @return string de Date_model.
	 */
	@Override
	public String toString(){
		return jour+"/"+mois+"/"+annee;		
	}

	/**
	 * Methode equals.
	 * @param date_model_2
	 * @return
	 */
	public boolean equals(Date_model date_model_2) {
		boolean isEqual = true;
		if (this.jour != date_model_2.getJour()) isEqual=false;
		else if (this.mois != date_model_2.getMois()) isEqual=false;
		else if (this.annee != date_model_2.getAnnee()) isEqual=false;
		return isEqual;
	}


	// getters
	public short getJour() { return this.jour; }
	public short getMois() { return this.mois; }
	public short getAnnee() { return  this.annee; }

}
