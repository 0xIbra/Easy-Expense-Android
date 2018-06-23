package fr.ibragim.e_expense.Metier;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ibragim.abubakarov on 03/04/2018.
 */

public abstract class Depense {
    private int id;
    private String dateDepense;
    private double MontantRemboursement;
    private String etatValidation;
    private String dateValidation;
    private double montantDepense;
    private int idUtilisateur;


    public Depense(){

    }


    public Depense(JSONObject depense){
        try {
            this.id = depense.getInt("idDepense");
            this.dateDepense = depense.getString("dateDepense");
            this.MontantRemboursement = depense.getDouble("montantRemboursement");
            this.etatValidation = depense.getString("etatValidation");
            this.dateValidation = depense.getString("dateValidation");
            this.montantDepense = depense.getDouble("montantDepense");
            this.idUtilisateur = depense.getInt("idUtilisateur");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initDepense(JSONObject depense){
        try {
            this.id = depense.getInt("idDepense");
            this.dateDepense = depense.getString("dateDepense");
            this.MontantRemboursement = depense.getDouble("montantRemboursement");
            this.etatValidation = depense.getString("etatValidation");
            this.dateValidation = depense.getString("dateValidation");
            this.montantDepense = depense.getDouble("montantDepense");
            this.idUtilisateur = depense.getInt("idUtilisateur");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJSON(){
        return "\"idDepense\" : "+this.id+", \"dateDepense\" : \""+this.dateDepense+"\", \"montantRemboursement\" : "+this.MontantRemboursement+", " +
                "\"etatValidation\" : \""+this.etatValidation+"\", \"dateValidation\" : \""+this.dateValidation+"\", \"montantDepense\" : \""+this.montantDepense+"\"," +
                " \"idUtilisateur\" : "+this.idUtilisateur+",";
    }


    public Depense(int id, String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense,int codeF, int idU) {
        this.id = id;
        this.dateDepense = datePaiement;
        MontantRemboursement = montantRemboursement;
        this.etatValidation = etatValidation;
        this.dateValidation = dateValidation;
        this.montantDepense = montantDepense;
        this.idUtilisateur = idU;
    }


    public Depense(String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense,int codeF, int idU) {
        this.dateDepense = datePaiement;
        MontantRemboursement = montantRemboursement;
        this.etatValidation = etatValidation;
        this.dateValidation = dateValidation;
        this.montantDepense = montantDepense;
        this.idUtilisateur = idU;
    }

    public abstract String toJSON();



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(String dateDepense) {
        this.dateDepense = dateDepense;
    }

    public double getMontantRemboursement() {
        return MontantRemboursement;
    }

    public void setMontantRemboursement(double montantRemboursement) {
        MontantRemboursement = montantRemboursement;
    }

    public String getEtatValidation() {
        return this.etatValidation;
    }

    public void setEtatValidation(String etatValidation) {
        this.etatValidation = etatValidation;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    public double getMontantDepense() {
        return montantDepense;
    }

    public void setMontantDepense(double montantDepense) {
        this.montantDepense = montantDepense;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }
}
