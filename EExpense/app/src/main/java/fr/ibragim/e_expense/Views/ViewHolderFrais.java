package fr.ibragim.e_expense.Views;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ibragim.e_expense.AddDepenseActivity;
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
    public void bindType(final ListItem item, AdapterView.OnItemClickListener listener) {
        title.setText(((Frais) item).getIntituleFrais());
        date.setText(((Frais) item).getDateDepense());
        setBackground(((Frais) item).getEtatValidation());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddDepenseActivity.class);
                intent.putExtra("TYPE_DEPENSE", "Frais");
                intent.putExtra("EXISTING", "TRUE");
                intent.putExtra("DEPENSE_DATE", ((Frais) item).getDateDepense());
                intent.putExtra("DEPENSE_LIBELLE", ((Frais) item).getIntituleFrais());
                intent.putExtra("DEPENSE_MONTANT", ((Frais) item).getMontantDepense());
                view.getContext().startActivity(intent);
            }
        });
    }


    public void setBackground(String etat){
        switch (etat){
            case "En cours":
                this.etat.setBackgroundResource(R.drawable.etatbackground);
                this.etat.setText(etat);
                break;
            case "Validé":
                this.etat.setBackgroundResource(R.drawable.etat_validated_background);
                this.etat.setText(etat);
                break;

            case "Refusé":
                this.etat.setBackgroundResource(R.drawable.etat_refused_background);
                this.etat.setText(etat);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
