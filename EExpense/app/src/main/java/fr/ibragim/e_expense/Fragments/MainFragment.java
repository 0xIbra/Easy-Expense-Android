package fr.ibragim.e_expense.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.R;
import fr.ibragim.e_expense.Views.Adapter;
import fr.ibragim.e_expense.Views.MainActivityFragmentType;


public class MainFragment extends Fragment implements MainActivityFragmentType{
    //CURRENT USER
    protected int userid;

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


    public void setNotes(List<NoteFrais> list){
        this.NotesFrais = list;
    }

    public void getNotes(){
            r = this.v.findViewById(R.id.fragment_main_recycler_view);
            r.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            r.setAdapter(new Adapter(NotesFrais, null));
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
