package fr.ibragim.e_expense.Views;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.R;

/**
 * Created by ibragim.abubakarov on 04/04/2018.
 */

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    List<NoteFrais> list;
    private final OnItemClickListener listener;

    public Adapter(List<NoteFrais> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteFrais n = list.get(position);
        holder.bind(n, listener);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Snackbar.make(v, "test "+pos, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
