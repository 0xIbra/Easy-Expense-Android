package fr.ibragim.e_expense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class TypeDepenseActivity extends AppCompatActivity {

    CardView fraisBTN, trajetBTN;
    JSONObject currentNote, currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_depense);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.customToolbar);

        toolbar.setTitle("Type de depense");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fraisBTN = findViewById(R.id.FraisCard);
        trajetBTN = findViewById(R.id.TrajetCard);


        Intent intent = getIntent();
        if (intent != null){
            try {
                currentNote = new JSONObject(intent.getStringExtra("NOTEDEFRAIS"));
                currentUser = new JSONObject(intent.getStringExtra("CURRENT_USER"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        fraisBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddDepenseActivity.class);
                intent.putExtra("TYPE_DEPENSE", "Frais");
                intent.putExtra("EXISTING", "FALSE");
                intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                intent.putExtra("CURRENT_USER", currentUser.toString());
                startActivity(intent);
            }
        });


        trajetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddDepenseActivity.class);
                intent.putExtra("TYPE_DEPENSE", "Trajet");
                intent.putExtra("EXISTING", "FALSE");
                intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                intent.putExtra("CURRENT_USER", currentUser.toString());
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
