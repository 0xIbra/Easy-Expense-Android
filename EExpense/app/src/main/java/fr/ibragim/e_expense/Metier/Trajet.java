package fr.ibragim.e_expense.Metier;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ibragim.e_expense.Views.ListItem;

/**
 * Created by ibragim.abubakarov on 07/04/2018.
 */

public class Trajet extends Depense implements ListItem{
    private String libelleTrajet;
    private double dureeTrajet;
    private String villeDepart;
    private String villeArrivee;
    private String dateAller;
    private String dateRetour;
    private double distanceKM;
    private int codeFrais;


    public Trajet(){

    }


    public Trajet(JSONObject depense){
        super.initDepense(depense);
        try {
            this.libelleTrajet = depense.getString("libelleTrajet");
            this.dureeTrajet = depense.getDouble("dureeTrajet");
            this.villeDepart = depense.getString("villeDepart");
            this.villeArrivee = depense.getString("villeArrivee");
            this.dateAller = depense.getString("dateAller");
            this.dateRetour = depense.getString("dateRetour");
            this.distanceKM = depense.getDouble("distanceKilometres");
            this.codeFrais = depense.getInt("codeFrais");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toJSON() {
        return "{ "+super.getJSON()+" \"libelleTrajet\" : \""+this.libelleTrajet+"\", \"dureeTrajet\" : "+this.dureeTrajet+", \"villeDepart\" : \""+this.villeDepart+"\", " +
                "\"villeArrivee\" : \""+this.villeArrivee+"\", \"dateAller\" : \""+this.dateAller+"\", \"dateRetour\" : \""+this.dateRetour+"\", " +
                "\"distanceKilometres\" : "+this.distanceKM+", \"codeFrais\" : "+this.codeFrais+"}";
    }

    public Trajet(int id, String libelle, String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense, int codeF, int idU, double dureeTrajet, String villeDepart, String villeArrivee, String dateAller, String dateRetour, double distanceKM, int idDepense, int codeFrais) {
        super(id, datePaiement, montantRemboursement, etatValidation, dateValidation, montantDepense, codeF, idU);
        this.libelleTrajet = libelle;
        this.dureeTrajet = dureeTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateAller = dateAller;
        this.dateRetour = dateRetour;
        this.distanceKM = distanceKM;
        this.codeFrais = codeFrais;
    }

    public Trajet( String libelle, String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense, int codeF, int idU, double dureeTrajet, String villeDepart, String villeArrivee, String dateAller, String dateRetour, double distanceKM, int idDepense, int codeFrais) {
        super(datePaiement, montantRemboursement, etatValidation, dateValidation, montantDepense, codeF, idU);
        this.libelleTrajet = libelle;
        this.dureeTrajet = dureeTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateAller = dateAller;
        this.dateRetour = dateRetour;
        this.distanceKM = distanceKM;
        this.codeFrais = codeFrais;
    }


    public String toString(){
        return this.getLibelleTrajet() +" - " + this.getId();
    }


    public String getLibelleTrajet() {
        return libelleTrajet;
    }

    public void setLibelleTrajet(String libelleTrajet) {
        this.libelleTrajet = libelleTrajet;
    }

    public double getDureeTrajet() {
        return dureeTrajet;
    }

    public void setDureeTrajet(double dureeTrajet) {
        this.dureeTrajet = dureeTrajet;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public String getDateAller() {
        return dateAller;
    }

    public void setDateAller(String dateAller) {
        this.dateAller = dateAller;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public double getDistanceKM() {
        return distanceKM;
    }

    public void setDistanceKM(double distanceKM) {
        this.distanceKM = distanceKM;
    }

    public int getCodeFrais() {
        return codeFrais;
    }

    public void setCodeFrais(int codeFrais) {
        this.codeFrais = codeFrais;
    }

    @Override
    public int getListItemType() {
        return ListItem.Trajet;
    }
}
