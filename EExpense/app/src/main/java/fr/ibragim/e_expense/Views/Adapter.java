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
import fr.ibragim.e_expense.NoteFraisActivity;
import fr.ibragim.e_expense.R;

/**
 * Created by ibragim.abubakarov on 04/04/2018.
 */

public class Adapter extends RecyclerView.Adapter<BasicViewHolder> {

    private List<NoteFrais> list;
    private final OnItemClickListener listener;
    //private Context context;

    public Adapter(List<NoteFrais> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
        //this.context = context;
    }





    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new BasicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        final NoteFrais n = list.get(position);
        holder.bind(n, listener);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intentToDepense = new Intent(v.getContext(), NoteFraisActivity.class);
                intentToDepense.putExtra("NOTE_FRAIS_ID", n.getId());
                intentToDepense.putExtra("NOTE_FRAIS_LIBELLE", n.getLibelle());
                intentToDepense.putExtra("NOTE_FRAIS_COMMENTAIRE", n.getCommentaireFrais());
                intentToDepense.putExtra("EXISTING", true);
                v.getContext().startActivity(intentToDepense);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
