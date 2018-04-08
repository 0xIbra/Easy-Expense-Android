package fr.ibragim.e_expense.Metier;

import java.util.ArrayList;

/**
 * Created by ibragim.abubakarov on 03/04/2018.
 */

public class NoteFrais {
    private int id;
    private String libelle;
    private String DateFrais;
    private String Ville;
    private String dateSoumission;
    private String commentaireFrais;
    private String etat;
    private int idUtilisateur;
    private int idClient;
    private ArrayList<Depense> listeDepenses;


    public NoteFrais(int id, String libelle, String dateFrais, String ville, String dateSoumission, String commentaireFrais,int idU) {
        this.id = id;
        this.libelle = libelle;
        DateFrais = dateFrais;
        Ville = ville;
        this.dateSoumission = dateSoumission;
        this.commentaireFrais = commentaireFrais;
        this.idUtilisateur = idU;

    }

    public NoteFrais(int id, String libelle, String dateFrais, String ville, String dateSoumission, String commentaireFrais, String etat, int idU, int idC) {
        this.id = id;
        this.libelle = libelle;
        DateFrais = dateFrais;
        Ville = ville;
        this.dateSoumission = dateSoumission;
        this.commentaireFrais = commentaireFrais;
        this.etat = etat;
        this.idUtilisateur = idU;
        this.idClient = idC;
    }

    public NoteFrais(String libelle, String dateFrais, String ville, String dateSoumission, String commentaireFrais) {
        this.libelle = libelle;
        DateFrais = dateFrais;
        Ville = ville;
        this.dateSoumission = dateSoumission;
        this.commentaireFrais = commentaireFrais;
    }




    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void addDepense(Depense d){
        this.listeDepenses.add(d);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDateFrais() {
        return DateFrais;
    }

    public void setDateFrais(String dateFrais) {
        DateFrais = dateFrais;
    }

    public String getVille() {
        return Ville;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public String getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(String dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getCommentaireFrais() {
        return commentaireFrais;
    }

    public void setCommentaireFrais(String commentaireFrais) {
        this.commentaireFrais = commentaireFrais;
    }

    public ArrayList<Depense> getListeDepenses() {
        return listeDepenses;
    }

    public void setListeDepenses(ArrayList<Depense> listeDepenses) {
        this.listeDepenses = listeDepenses;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }
}

