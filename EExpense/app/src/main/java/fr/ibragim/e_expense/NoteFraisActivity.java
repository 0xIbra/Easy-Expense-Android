package fr.ibragim.e_expense;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import fr.ibragim.e_expense.Metier.Frais;
import fr.ibragim.e_expense.Metier.Trajet;
import fr.ibragim.e_expense.Views.FraisAdapter;
import fr.ibragim.e_expense.Views.ListItem;
import fr.ibragim.e_expense.network.HttpsPostRequest;

public class NoteFraisActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText noteDate;
    private EditText noteComment;
    private EditText noteLibelle;
    private Button noteSubmit;
    private final String API_URL = "https://api.ibragim.fr/Android.php";
    private HttpsPostRequest request;

    private String selectedType;

    private int CurrentNoteFrais;
    private List<ListItem> DepensesList = new ArrayList<ListItem>();

    private RecyclerView depenseRecyclerview;

    private String noteFraisLibelle = null;
    private String noteFraisCommentaire = null;

    // Verif si la note de frais est existante ou pas
    private boolean existing;

    private int currentUserId;
    private String currentNoteFraisTitle;
    private int countNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_frais);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.customToolbar);
        toolbar.setTitle("Ma note de frais");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Menu menu = findViewById(R.menu.note_frais_menu);

        noteLibelle = findViewById(R.id.noteLibelle);
        noteComment = findViewById(R.id.noteComment);
        //noteSubmit = findViewById(R.id.noteSubmit);

        noteLibelle.setSelected(false);
        noteComment.setSelected(false);


        FloatingActionButton addDepense = (FloatingActionButton) findViewById(R.id.addDepense);
        addDepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteFraisActivity.this, TypeDepenseActivity.class);
                startActivity(i);
            }
        });


        // Validation de la note de frais
        FloatingActionButton noteSubmit = findViewById(R.id.noteSubmit);
        noteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Intent intentToDepense = getIntent();
        if (intentToDepense != null){
            existing = intentToDepense.getBooleanExtra("EXISTING", false);
            this.CurrentNoteFrais = intentToDepense.getIntExtra("NOTE_FRAIS_ID", 0);
            noteFraisLibelle = intentToDepense.getStringExtra("NOTE_FRAIS_LIBELLE");
            noteLibelle.setText(noteFraisLibelle);
            this.currentNoteFraisTitle = intentToDepense.getStringExtra("INIT_NOTE_FRAIS");
            this.currentUserId = intentToDepense.getIntExtra("USER_ID", 0);
            noteFraisCommentaire = intentToDepense.getStringExtra("NOTE_FRAIS_COMMENTAIRE");
            noteComment.setText(noteFraisCommentaire);
        }

        //System.out.println("USER ID : " + currentUserId);


        if (this.CurrentNoteFrais != 0){
            if (this.existing == true){
                this.getDepensesForNote();
            }
        }else {
            initNewNoteFrais();
        }

        getCountNote();

        currentNoteFraisTitle = currentNoteFraisTitle+" #"+countNote;
        this.noteLibelle.setText(currentNoteFraisTitle);
        System.out.println("LIBELLE TEST : " + currentNoteFraisTitle);

    }




    public void getCountNote(){
        request = new HttpsPostRequest();

        String params = "getCountFrais=true&idUtilisateur="+currentUserId;
        String res ;

        try {
            res = request.execute(API_URL, params).get();
            Log.v("RETOUR ", res);
            JSONObject count = new JSONObject(res);
            countNote = count.getInt("nbNotesFrais");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initNewNoteFrais(){
        System.out.println("USER ID : " + currentUserId);
        System.out.println("LIBELLE : " + currentNoteFraisTitle);
        request = new HttpsPostRequest();
        String params = "putNoteFrais=true&libelleNote="+currentNoteFraisTitle+"&idUtilisateur="+currentUserId;
        System.out.println("PARAMS " +params);
        try {
            request.execute(API_URL, params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    public void getDepensesForNote(){
        request = new HttpsPostRequest();
        String result = null;
        String params = "getDepensesForNoteFrais=true&codeFrais="+this.CurrentNoteFrais;

        try{
            result = request.execute(API_URL, params).get();
            Log.v("RETOUR DEPENSES", result);
            JSONObject depenseArray = new JSONObject(result);
            JSONArray FraisType = depenseArray.getJSONArray("Frais");
            JSONArray Trajet = depenseArray.getJSONArray("Trajet");
            JSONObject currentDepense;
            for (int i = 0; i < FraisType.length(); i++){
                currentDepense = FraisType.getJSONObject(i);
                int idD = currentDepense.getInt("idDepense");
                String dateD = currentDepense.getString("dateDepense");
                double montantR = currentDepense.getDouble("montantRemboursement");
                String etat = currentDepense.getString("etatValidation");
                String dateValidation = currentDepense.getString("dateValidation");
                double montantD = currentDepense.getDouble("montantDepense");
                int codeF = currentDepense.getInt("codeFrais");
                int idU = currentDepense.getInt("idUtilisateur");
                String libelle = currentDepense.getString("libelleFrais");
                String details = currentDepense.getString("detailsFrais");
                String dateF = currentDepense.getString("dateFrais");


                this.DepensesList.add(new Frais(idD, dateD, montantR, etat, dateValidation, montantD, codeF, idU, libelle, details, dateF, idD, codeF));
            }

            for (int i = 0; i < Trajet.length(); i++){
                currentDepense = Trajet.getJSONObject(i);
                int idD = currentDepense.getInt("idDepense");
                String libelle = currentDepense.getString("libelleTrajet");
                String dateD = currentDepense.getString("dateDepense");
                double montantR = currentDepense.getDouble("montantRemboursement");
                String etat = currentDepense.getString("etatValidation");
                String dateValidation = currentDepense.getString("dateValidation");
                double montantD = currentDepense.getDouble("montantDepense");
                int codeF = currentDepense.getInt("codeFrais");
                int idU = currentDepense.getInt("idUtilisateur");
                double dureeT = currentDepense.getDouble("dureeTrajet");
                String villeD = currentDepense.getString("villeDepart");
                String villeA = currentDepense.getString("villeArrivee");
                String dateA = currentDepense.getString("dateAller");
                String dateR = currentDepense.getString("dateRetour");
                double distance = currentDepense.getDouble("distanceKilometres");

                this.DepensesList.add(new Trajet(idD, libelle, dateD, montantR, etat, dateValidation, montantD, codeF, idU, dureeT, villeD, villeA, dateA, dateR, distance, idD, codeF));
            }

            this.depenseRecyclerview = findViewById(R.id.depenseRecycler);
            depenseRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            depenseRecyclerview.setAdapter(new FraisAdapter(DepensesList, null, getApplicationContext()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_frais_menu, menu);

        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {




        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;


            case R.id.action_delete:
                Toast.makeText(getApplicationContext(), "SUPPRIMER", Toast.LENGTH_SHORT).show();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Snackbar.make(view, adapterView.getItemAtPosition(i).toString(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
