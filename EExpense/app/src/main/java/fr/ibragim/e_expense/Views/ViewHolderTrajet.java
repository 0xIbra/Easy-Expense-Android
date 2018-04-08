package fr.ibragim.e_expense.Views;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ibragim.e_expense.Metier.Trajet;
import fr.ibragim.e_expense.R;

/**
 * Created by ibragim.abubakarov on 07/04/2018.
 */

public class ViewHolderTrajet extends ViewHolder {
    private ImageView img;
    private TextView title;
    private TextView date;
    private TextView etat;

    private ItemClickListener itemClickListener;

    public ViewHolderTrajet(View itemView) {
        super(itemView);


        img = itemView.findViewById(R.id.justi);
        title = itemView.findViewById(R.id.idNoteFrais);

        date = itemView.findViewById(R.id.dateNoteFrais);
        etat = itemView.findViewById(R.id.etatNoteFrais);
    }

    @Override
    public void bindType(ListItem item, final AdapterView.OnItemClickListener listener) {
        title.setText(((Trajet) item).getDateDepense()+" - "+((Trajet) item).getDateAller());
        date.setText(((Trajet) item).getDateDepense());
        etat.setText(((Trajet) item).getEtatValidation());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TEST DEPENSE", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view ,getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }

}
