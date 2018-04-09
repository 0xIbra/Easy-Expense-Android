package fr.ibragim.e_expense.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.R;
import fr.ibragim.e_expense.Views.Adapter;
import fr.ibragim.e_expense.Views.MainActivityFragmentType;
import fr.ibragim.e_expense.network.ConnectionDetector;
import fr.ibragim.e_expense.network.HttpsPostRequest;


public class MainFragment extends Fragment implements MainActivityFragmentType{
    protected final String USER_TOKEN = "";
    private final String USER_ID = "USER_ID";
    String result = "";
    String login;
    String pass;
    private final String USER_SESSION = "USER_SESSION";
    SharedPreferences userPrefs;
    protected String API_URL = "https://api.ibragim.fr/Android.php";
    protected HttpsPostRequest getRequest;
    protected ConnectionDetector connectionDetector;
    protected String user_email;

    //CURRENT USER
    protected int userid;
    protected String useremail;
    protected String userToken;
    protected String usernom;
    protected String userprenom;

    protected List<NoteFrais> NotesFrais = new ArrayList<NoteFrais>();
    RecyclerView r;

    View v;


    public MainFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.v = inflater.inflate(R.layout.fragment_main, container, false);
        getNotes();
        return v;
    }




    public void getNotes(){
        HttpsPostRequest newReq = new HttpsPostRequest();
        String res = "";
        String params = "getNotes=true&userID="+userid;
        System.out.println("MY ID "+params);
        try {
            res = newReq.execute(API_URL, params).get();
            Log.v("RETOUR ", res);
            JSONArray cards = new JSONArray(res);
            JSONObject currentCard;
            for (int i = 0; i < cards.length(); i++){
                currentCard = cards.getJSONObject(i);
                int codeFrais = currentCard.getInt("codeFrais");
                String libelleNote = currentCard.getString("libelleNote");
                String dateF = currentCard.getString("dateFrais");
                String ville = currentCard.getString("villeFrais");
                String dateS = currentCard.getString("dateSoumission");
                String comm = currentCard.getString("commentaireFrais");
                String etatN = currentCard.getString("etat");
                int idUtilisateur = currentCard.getInt("idUtilisateur");
                int idClient = currentCard.getInt("idClient");


                NotesFrais.add(new NoteFrais(codeFrais, libelleNote, dateF, ville, dateS, comm, etatN, idUtilisateur, idClient));

            }


            r = this.v.findViewById(R.id.fragment_main_recycler_view);
            r.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            r.setAdapter(new Adapter(NotesFrais, null));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getMenuType() {
        return MainActivityFragmentType.MainFragment;
    }

    public void init(int USERID) {
        this.userid = USERID;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
