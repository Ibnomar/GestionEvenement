package com.example.macbookpro.Sopra.Models;

/**
 * Created by jason on 04/12/15.
 */
public class Options {
    Boolean Visio;
    Boolean Telephone;
    Boolean Securise;
    Boolean Digilab;

    public Options(Boolean Visio, Boolean Telephone, Boolean Securise, Boolean Digilab) {
        this.Visio = Visio;
        this.Telephone = Telephone;
        this.Securise = Securise;
        this.Digilab = Digilab;
    }

    public Options(Options options_a_copier) {
        this.Visio = options_a_copier.aVisio();
        this.Telephone = options_a_copier.aTelephone();
        this.Securise = options_a_copier.aSecurise();
        this.Digilab = options_a_copier.aDigilab();
    }

    public Boolean aVisio() {
        return Visio;
    }

    public void setVisio(Boolean aVisio) {
        this.Visio = aVisio;
    }

    public Boolean aTelephone() {
        return Telephone;
    }

    public void setTelephone(Boolean aTelephone) {
        this.Telephone = aTelephone;
    }

    public Boolean aSecurise() {
        return Securise;
    }

    public void setSecurise(Boolean aSecurise) { this.Securise = aSecurise; }

    public Boolean aDigilab() {
        return Digilab;
    }

    public void setDigilab(Boolean aDigilab) {
        this.Digilab = aDigilab;
    }
    
    public boolean equals(Options opt_voulue){
        boolean retour=true;
        if(opt_voulue.Digilab && !this.Digilab)
                retour=false;
        else if(opt_voulue.Securise && !this.Securise)
                retour=false;
        else if(opt_voulue.Telephone && !this.Telephone)
                retour=false;
        else if(opt_voulue.Visio && !this.Visio)
                retour=false;
        return retour;
    }

    /**
     * Savoir si les options en parametres sont comprises dans les options de reference.
     * @param opt_voulue
     * @return
     */
    public boolean comprendLesOptions(Options opt_voulue){
        boolean retour = true;
        if (!this.Visio && opt_voulue.aVisio()) retour = false;
        if (!this.Telephone && opt_voulue.aTelephone()) retour = false;
        if (!this.Securise && opt_voulue.aSecurise()) retour = false;
        if (!this.aDigilab() && opt_voulue.aDigilab()) retour = false;
        return retour;
    }
	    
    public String toString (){
        String retour="";
        if(this.Digilab)
                retour=retour+" Digilab ";
        if(this.Securise)
                retour=retour+" Securise ";
        if(this.Telephone)
                retour=retour+" Telephone ";
        if(this.Visio)
                retour=retour+" Visio ";
        if (!this.Digilab & !this.Securise & !this.Telephone & !this.Visio)
                retour=retour+" Aucune option";
        return retour;
    }
}
