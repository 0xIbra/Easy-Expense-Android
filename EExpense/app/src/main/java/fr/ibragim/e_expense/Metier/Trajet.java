package fr.ibragim.e_expense.Metier;

import fr.ibragim.e_expense.Views.ListItem;

/**
 * Created by ibragim.abubakarov on 07/04/2018.
 */

public class Trajet extends Depense implements ListItem{
    private double dureeTrajet;
    private String villeDepart;
    private String villeArrivee;
    private String dateAller;
    private String dateRetour;
    private double distanceKM;
    private int idDepense;
    private int codeFrais;


    public Trajet(int id, String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense, int codeF, int idU, double dureeTrajet, String villeDepart, String villeArrivee, String dateAller, String dateRetour, double distanceKM, int idDepense, int codeFrais) {
        super(id, datePaiement, montantRemboursement, etatValidation, dateValidation, montantDepense, codeF, idU);
        this.dureeTrajet = dureeTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateAller = dateAller;
        this.dateRetour = dateRetour;
        this.distanceKM = distanceKM;
        this.idDepense = idDepense;
        this.codeFrais = codeFrais;
    }

    public Trajet(String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense, int codeF, int idU, double dureeTrajet, String villeDepart, String villeArrivee, String dateAller, String dateRetour, double distanceKM, int idDepense, int codeFrais) {
        super(datePaiement, montantRemboursement, etatValidation, dateValidation, montantDepense, codeF, idU);
        this.dureeTrajet = dureeTrajet;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateAller = dateAller;
        this.dateRetour = dateRetour;
        this.distanceKM = distanceKM;
        this.idDepense = idDepense;
        this.codeFrais = codeFrais;
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

    public int getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(int idDepense) {
        this.idDepense = idDepense;
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
