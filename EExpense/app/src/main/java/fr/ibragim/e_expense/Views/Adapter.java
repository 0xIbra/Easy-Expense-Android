package fr.ibragim.e_expense.Views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.R;

/**
 * Created by ibragim.abubakarov on 04/04/2018.
 */

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    List<NoteFrais> list;

    public Adapter(List<NoteFrais> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteFrais n = list.get(position);
        holder.bind(n);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
