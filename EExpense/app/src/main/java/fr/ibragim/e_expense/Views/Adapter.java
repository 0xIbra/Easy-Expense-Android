package fr.ibragim.e_expense.Views;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import fr.ibragim.e_expense.AddDepenseActivity;
import fr.ibragim.e_expense.MainActivity;
import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.R;

/**
 * Created by ibragim.abubakarov on 04/04/2018.
 */

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private List<NoteFrais> list;
    private final OnItemClickListener listener;
    //private Context context;

    public Adapter(List<NoteFrais> list, OnItemClickListener listener, Context context) {
        this.list = list;
        this.listener = listener;
        //this.context = context;
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NoteFrais n = list.get(position);
        holder.bind(n, listener);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intentToDepense = new Intent(v.getContext(), AddDepenseActivity.class);
                intentToDepense.putExtra("NOTE_FRAIS_ID", n.getId());
                v.getContext().startActivity(intentToDepense);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
