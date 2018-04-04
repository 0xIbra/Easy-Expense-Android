package fr.ibragim.e_expense.Metier;

/**
 * Created by ibragim.abubakarov on 03/04/2018.
 */

public class Depense {
    private int id;
    private String datePaiement;
    private double MontantRemboursement;
    private String etatValidation;
    private String dateValidation;
    private double montantDepense;


    public Depense(int id, String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense) {
        this.id = id;
        this.datePaiement = datePaiement;
        MontantRemboursement = montantRemboursement;
        this.etatValidation = etatValidation;
        this.dateValidation = dateValidation;
        this.montantDepense = montantDepense;
    }


    public Depense(String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense) {
        this.datePaiement = datePaiement;
        MontantRemboursement = montantRemboursement;
        this.etatValidation = etatValidation;
        this.dateValidation = dateValidation;
        this.montantDepense = montantDepense;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public double getMontantRemboursement() {
        return MontantRemboursement;
    }

    public void setMontantRemboursement(double montantRemboursement) {
        MontantRemboursement = montantRemboursement;
    }

    public String getEtatValidation() {
        return etatValidation;
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
}
