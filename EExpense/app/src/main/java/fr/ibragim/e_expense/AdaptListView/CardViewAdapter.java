package fr.ibragim.e_expense.AdaptListView;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

import fr.ibragim.e_expense.MainActivity;

/**
 * Created by ibragim.abubakarov on 03/04/2018.
 */

public class CardViewAdapter extends BaseAdapter {
    ArrayList<CardView> listNotes;
    Activity context;

    public CardViewAdapter(Activity context, ArrayList<CardView> listCardViews){
        super();
        this.listNotes = listCardViews;
        this.context = context;
    }



    private class ViewHolder{
        CardView noteView;
    }


    public ArrayList<CardView> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<CardView> listNotes) {
        this.listNotes = listNotes;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

}
