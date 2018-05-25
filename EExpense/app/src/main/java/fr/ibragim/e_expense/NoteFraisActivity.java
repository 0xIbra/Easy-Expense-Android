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

import fr.ibragim.e_expense.Metier.Depense;
import fr.ibragim.e_expense.Metier.Frais;
import fr.ibragim.e_expense.Metier.Trajet;
import fr.ibragim.e_expense.Views.FraisAdapter;
import fr.ibragim.e_expense.Views.ListItem;
import fr.ibragim.e_expense.network.HttpsDeleteRequest;
import fr.ibragim.e_expense.network.HttpsGetRequest;
import fr.ibragim.e_expense.network.HttpsPostRequest;

public class NoteFraisActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private final String USER_JSON = "USER_JSON";
    private final String NOTEFRAIS_JSON = "NOTEFRAIS_JSON";


    private EditText noteDate;
    private EditText noteComment;
    private EditText noteLibelle;
    private Button noteSubmit;
    private String API_URL = "http://api.ibragim.fr/public/api/";
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

    private JSONObject currentNote = null;
    private JSONObject currentUser = null;


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
        noteDate = findViewById(R.id.noteDate);
        noteComment = findViewById(R.id.noteComment);
        //noteSubmit = findViewById(R.id.noteSubmit);

        noteLibelle.setSelected(false);
        noteComment.setSelected(false);


        noteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DATE
            }
        });


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
            String json = intentToDepense.getStringExtra(USER_JSON);
            System.out.println("EXTRAJSON : "  + json);
            try {
                currentUser = new JSONObject(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (existing){
                try {
                    currentNote = new JSONObject(intentToDepense.getStringExtra(NOTEFRAIS_JSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println("USER ID : " + currentUserId);


        if (this.currentNote != null){
            if (this.existing == true){
                try {
                    this.getDepensesForNote();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (currentUser != null){
                try {
                    initNewNoteFrais();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        try {
            noteLibelle.setText(currentNote.getString("libelleNote"));
            noteDate.setText(currentNote.getString("dateFrais"));
            if (noteComment.getText() == null || noteComment.getText().equals("") || noteComment.equals("null")){
                noteComment.setText("merci de modifier ceci");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (currentUser != null){
            try {
                if (currentNote.getString("etat").equals("Validé") || currentNote.getString("etat").equals("Refusé")){
                    noteLibelle.setEnabled(false);
                    noteDate.setEnabled(false);
                    noteComment.setEnabled(false);
                    noteSubmit.hide();
                    addDepense.hide();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void initNewNoteFrais() throws JSONException {
        String result = null;
        request = new HttpsPostRequest();
        String API = API_URL;
        API = API + "notesdefrais/add";
        JSONObject note = new JSONObject();
        String libelle = currentUser.getString("prenomUtilisateur") + " "+ currentUser.getString("nomUtilisateur");
        try {
            note.put("libelleNote", libelle);
            note.put("idUtilisateur", currentUser.getString("idUtilisateur"));
            result = request.execute(API, note.toString()).get();
            currentNote = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    public void getDepensesForNote() throws JSONException {
        HttpsGetRequest request = new HttpsGetRequest();
        String result = null;
        String API = API_URL;
        API = API+"depenses/get/"+currentNote.getInt("codeFrais");
        try{
            result = request.execute(API).get();
            Log.v("RETOUR DEPENSES", result+"");
            JSONObject depenseArray = new JSONObject(result);
            JSONArray FraisType = depenseArray.getJSONArray("Frais");
            //System.out.println("FRAIS ARRAY : "+ FraisType.toString()) ;
            JSONArray Trajet = depenseArray.getJSONArray("Trajet");
            //System.out.println("TRAJET ARRAY : " + Trajet.toString());
            JSONObject currentDepense;
            Depense currentD;

            for (int i = 0; i < FraisType.length(); i++){
                currentDepense = FraisType.getJSONObject(i);
                currentD = new Frais(currentDepense);
                this.DepensesList.add((Frais) currentD);
            }

            for (int i = 0; i < Trajet.length(); i++){
                currentDepense = Trajet.getJSONObject(i);
                currentD = new Trajet(currentDepense);

                this.DepensesList.add((Trajet) currentD);
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
                try {
                    if (this.currentNote.getString("etat").equals("En Cours")){
                        this.DeleteNoteDeFrais();
                    }else{
                        Toast.makeText(this, "Note de frais déjà traitée", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    public void DeleteNoteDeFrais(){
        API_URL += "notesdefrais/delete";
        HttpsDeleteRequest deleteRequest = new HttpsDeleteRequest();
        String response = "";

        try {
            response = deleteRequest.execute(API_URL, currentNote.toString()).get();
            JSONObject JsonResponse = new JSONObject(response);
            if (JsonResponse.getBoolean("response") == true){
                Toast.makeText(getApplicationContext(), "Note de frais supprimée", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_JSON", currentUser.toString());
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(this, "Erreur de suppréssion !", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
