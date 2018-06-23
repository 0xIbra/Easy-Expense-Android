package fr.ibragim.e_expense.Metier;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ibragim.abubakarov on 23/06/2018.
 */

public class Justificatif {
    private int idJustificatif;
    private String titreJustificatif;
    private String slug;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    private String urlJustiifcatif;
    private int idDepense;
    private int codeFrais;


    public Justificatif(){

    }


    public Justificatif(JSONObject justificatif){
        try {
            this.idJustificatif = justificatif.getInt("idJustificatif");
            this.titreJustificatif = justificatif.getString("titreJustificatif");
            this.slug = justificatif.getString("slug");
            this.urlJustiifcatif = justificatif.getString("urlJustiifcatif");
            this.idDepense = justificatif.getInt("idDepense");
            this.codeFrais = justificatif.getInt("codeFrais");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Justificatif(int id, String titre, String slug, String url, int idD, int codeF){
        this.idJustificatif = id;
        this.titreJustificatif = titre;
        this.slug = slug;
        this.urlJustiifcatif = url;
        this.idDepense = idD;
        this.codeFrais = codeF;
    }


    public String toJSON(){
        return "{ \"idJustificatif\": "+this.idJustificatif+", \"titreJustificatif\": \""+this.titreJustificatif+"\", \"slug\": \""+this.slug+"\", " +
                "\"urlJustificatif\": \""+this.urlJustiifcatif+"\"," +
                " \"idDepense\": "+this.idDepense+", \"codeFrais\": "+this.codeFrais+" }";
    }


    public int getIdJustificatif() {
        return idJustificatif;
    }

    public void setIdJustificatif(int idJustificatif) {
        this.idJustificatif = idJustificatif;
    }

    public String getTitreJustificatif() {
        return titreJustificatif;
    }

    public void setTitreJustificatif(String titreJustificatif) {
        this.titreJustificatif = titreJustificatif;
    }

    public String getUrlJustiifcatif() {
        return urlJustiifcatif;
    }

    public void setUrlJustiifcatif(String urlJustiifcatif) {
        this.urlJustiifcatif = urlJustiifcatif;
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
}
