package fr.ibragim.e_expense.Views;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ibragim.e_expense.Metier.Frais;
import fr.ibragim.e_expense.R;

/**
 * Created by ibragim.abubakarov on 07/04/2018.
 */

public class ViewHolderFrais extends ViewHolder {

    private ImageView img;
    private TextView title;
    private TextView date;
    private TextView etat;

    ItemClickListener itemClickListener;

    public ViewHolderFrais(View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.justi);
        title = itemView.findViewById(R.id.idNoteFrais);
        date = itemView.findViewById(R.id.dateNoteFrais);
        etat = itemView.findViewById(R.id.etatNoteFrais);
    }

    @Override
    public void bindType(ListItem item, AdapterView.OnItemClickListener listener) {
        title.setText(((Frais) item).getIntituleFrais());
        date.setText(((Frais) item).getDateDepense());
        etat.setText(((Frais) item).getEtatValidation());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TEST DEPENSE", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
