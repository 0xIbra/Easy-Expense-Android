package fr.ibragim.e_expense.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.R;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by ibragim.abubakarov on 04/04/2018.
 */

public abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindType(ListItem item, AdapterView.OnItemClickListener listener);

    public abstract void setItemClickListener(ItemClickListener ic);
}
