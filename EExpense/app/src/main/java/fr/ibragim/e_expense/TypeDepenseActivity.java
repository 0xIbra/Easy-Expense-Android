package fr.ibragim.e_expense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

public class TypeDepenseActivity extends AppCompatActivity {

    CardView fraisBTN, trajetBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_depense);

        fraisBTN = findViewById(R.id.FraisCard);
        trajetBTN = findViewById(R.id.TrajetCard);


    }
}
