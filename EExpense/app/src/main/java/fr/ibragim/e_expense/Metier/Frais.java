package fr.ibragim.e_expense.Metier;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ibragim.e_expense.Views.ListItem;

/**
 * Created by ibragim.abubakarov on 07/04/2018.
 */

public class Frais extends Depense implements ListItem{
    private String intituleFrais;
    private String detailsFrais;
    private String dateFrais;
    private int idDepense;
    private int codeFrais;


    public Frais(JSONObject depense){
        super(depense);
        try {
            this.intituleFrais = depense.getString("libelleFrais");
            this.detailsFrais = depense.getString("detailsFrais");
            this.dateFrais = depense.getString("dateFrais");
            this.idDepense = depense.getInt("idDepense");
            this.codeFrais = depense.getInt("codeFrais");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toJSON() {
        return "{ "+super.getJSON()+" \"libelleFrais\" : \""+this.intituleFrais+"\", \"detailsFrais\" : \""+this.detailsFrais+"\", \"dateFrais\" : \""+this.dateFrais+"\", " +
                "\"idDepense\" : \""+this.idDepense+"\", \"codeFrais\" : \""+this.codeFrais+"\"}";
    }


    public Frais(int id, String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense, int codeF, int idU, String intituleFrais, String detailsFrais, String dateFrais, int idDepense, int codeFrais) {
        super(id, datePaiement, montantRemboursement, etatValidation, dateValidation, montantDepense, codeF, idU);
        this.intituleFrais = intituleFrais;
        this.detailsFrais = detailsFrais;
        this.dateFrais = dateFrais;
        this.idDepense = idDepense;
        this.codeFrais = codeFrais;
    }

    public Frais(String datePaiement, double montantRemboursement, String etatValidation, String dateValidation, double montantDepense, int codeF, int idU, String intituleFrais, String detailsFrais, String dateFrais, int idDepense, int codeFrais) {
        super(datePaiement, montantRemboursement, etatValidation, dateValidation, montantDepense, codeF, idU);
        this.intituleFrais = intituleFrais;
        this.detailsFrais = detailsFrais;
        this.dateFrais = dateFrais;
        this.idDepense = idDepense;
        this.codeFrais = codeFrais;
    }


    public String getIntituleFrais() {
        return intituleFrais;
    }

    public void setIntituleFrais(String intituleFrais) {
        this.intituleFrais = intituleFrais;
    }

    public String getDetailsFrais() {
        return detailsFrais;
    }

    public void setDetailsFrais(String detailsFrais) {
        this.detailsFrais = detailsFrais;
    }

    public String getDateFrais() {
        return dateFrais;
    }

    public void setDateFrais(String dateFrais) {
        this.dateFrais = dateFrais;
    }

    //public int getIdDepense() {
     //   return idDepense;
    //}

   /// public void setIdDepense(int idDepense) {
    //    this.idDepense = idDepense;
   // }

    @Override
    public int getCodeFrais() {
        return codeFrais;
    }

    @Override
    public void setCodeFrais(int codeFrais) {
        this.codeFrais = codeFrais;
    }

    @Override
    public int getListItemType() {
        return ListItem.Frais;
    }
}
