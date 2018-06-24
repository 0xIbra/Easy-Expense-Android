package fr.ibragim.e_expense.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import fr.ibragim.e_expense.network.HttpsGetRequest;


public class MainFragment extends Fragment implements MainActivityFragmentType{
    //CURRENT USER
    protected JSONObject user;

    protected List<NoteFrais> NotesFrais = new ArrayList<NoteFrais>();
    RecyclerView r;

    protected View v;

    protected SwipeRefreshLayout swipeRefresh;


    public MainFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.fragment_main, container, false);

        swipeRefresh = v.findViewById(R.id.refreshSwiper);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNotes();
            }
        });

        if (this.NotesFrais.isEmpty()){
            requestNotes();
        }

        getNotes();
        return v;
    }


    public void setNotes(List<NoteFrais> list){
        this.NotesFrais = list;
    }

    public void setUser(JSONObject user){
        this.user = user;
    }

    public void getNotes(){
        r = this.v.findViewById(R.id.fragment_main_recycler_view);

        r.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        r.setAdapter(new Adapter(NotesFrais, this.user, null));

        hideRefresh();
    }



    public void requestNotes(){
        NotesFrais = null;
        swipeRefresh.setRefreshing(true);
        HttpsGetRequest getRequest = new HttpsGetRequest();
        String res = "";
        try {
            String API = "https://api.ibragim.fr/public/api/";
            API = API+"notesdefrais/get/"+user.getInt("idUtilisateur");
            System.out.println("API URL : " + API);
            res = getRequest.execute(API).get();
            Log.v("RETOUR ", res+"");
            JSONArray cards = new JSONArray(res);
            JSONObject currentCard;
            NoteFrais noteFrais;
            NotesFrais = new ArrayList<NoteFrais>();
            for (int i = 0; i < cards.length(); i++){
                currentCard = cards.getJSONObject(i);
                noteFrais = new NoteFrais(currentCard);
                NotesFrais.add(noteFrais);
            }

            this.setNotes(NotesFrais);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getNotes();
    }


    public void hideRefresh(){
        swipeRefresh.setRefreshing(false);
    }



    @Override
    public int getMenuType() {
        return MainActivityFragmentType.MainFragment;
    }

    @Override
    public void init(int USERID) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
