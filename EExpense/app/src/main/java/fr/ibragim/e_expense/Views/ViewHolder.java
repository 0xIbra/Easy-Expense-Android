package fr.ibragim.e_expense.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.R;

/**
 * Created by ibragim.abubakarov on 04/04/2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView title;
    private TextView date;
    private TextView etat;

    public ViewHolder(View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.justi);
        title = itemView.findViewById(R.id.idNoteFrais);
        date = itemView.findViewById(R.id.dateNoteFrais);
        etat = itemView.findViewById(R.id.etatNoteFrais);
    }




    public void bind(NoteFrais n){
        title.setText(n.getLibelle());
        date.setText(n.getDateFrais());
        etat.setText("en cours");
    }

}
