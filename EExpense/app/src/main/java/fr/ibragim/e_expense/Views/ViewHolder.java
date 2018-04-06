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

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView img;
    private TextView title;
    private TextView date;
    private TextView etat;

    ItemClickListener itemClickListener;

    public ViewHolder(View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.justi);
        title = itemView.findViewById(R.id.idNoteFrais);
        date = itemView.findViewById(R.id.dateNoteFrais);
        etat = itemView.findViewById(R.id.etatNoteFrais);

        itemView.setOnClickListener(this);
    }




    public void bind(final NoteFrais n, final OnItemClickListener listener){
        title.setText(n.getLibelle());
        date.setText(n.getDateFrais());
        etat.setText("en cours");
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }

}
