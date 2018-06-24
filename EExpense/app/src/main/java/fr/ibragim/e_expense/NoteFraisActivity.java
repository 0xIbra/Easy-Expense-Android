package fr.ibragim.e_expense;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.DatePicker;
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
import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.Metier.Trajet;
import fr.ibragim.e_expense.Views.FraisAdapter;
import fr.ibragim.e_expense.Views.ListItem;
import fr.ibragim.e_expense.Widgets.DateFormat;
import fr.ibragim.e_expense.network.HttpsDeleteRequest;
import fr.ibragim.e_expense.network.HttpsGetRequest;
import fr.ibragim.e_expense.network.HttpsPostRequest;
import fr.ibragim.e_expense.network.HttpsPutRequest;

import static fr.ibragim.e_expense.AddDepenseActivity.DATE_PICKER_ID;

public class NoteFraisActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private final String USER_JSON = "USER_JSON";
    private final String NOTEFRAIS_JSON = "NOTEFRAIS_JSON";


    private EditText noteDate;
    private EditText noteComment;
    private EditText noteLibelle;
    private Button noteSubmit;
    private String API_URL = "https://api.ibragim.fr/public/api/";
    private HttpsPostRequest request;

    private String selectedType;

    private int year;
    private int month;
    private int day;

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

    private NoteFrais currentNoteFrais = null;

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

        // Get current date by calender
        final java.util.Calendar c = java.util.Calendar.getInstance();

        year = c.get(java.util.Calendar.YEAR);
        month = c.get(java.util.Calendar.MONTH);
        day = c.get(java.util.Calendar.DAY_OF_MONTH);

        noteDate.setText(new StringBuilder().append((day<10?("0"+day):(day)))
                .append("/").append((month<10?("0"+month):(month))).append("/").append(year));

        noteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID);
            }
        });


        FloatingActionButton addDepense = (FloatingActionButton) findViewById(R.id.addDepense);
        FloatingActionButton deleteNoteFrais = findViewById(R.id.notefraisDelete);

        addDepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteFraisActivity.this, TypeDepenseActivity.class);
                i.putExtra("NOTEDEFRAIS", currentNote.toString());
                i.putExtra("CURRENT_USER", currentUser.toString());
                startActivity(i);
            }
        });


        // Validation de la note de frais
        FloatingActionButton noteSubmit = findViewById(R.id.noteSubmit);
        noteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNoteFrais();
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
                this.getDepensesForNote();
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
            noteDate.setText(DateFormat.parseDMY(currentNote.getString("dateFrais"), DateFormat.DATE_DASH_FORMAT, DateFormat.DATE_FORMAT));
            if (!currentNote.getString("commentaireFrais").equals("")){
                noteComment.setText(currentNote.getString("commentaireFrais"));
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
                    deleteNoteFrais.hide();
                    Toast.makeText(this, "La note de frais a déjà été traitée.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        deleteNoteFrais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (currentNote.getString("etat").equals("En Cours")){
                        DeleteNoteDeFrais();
                    }else{
                        Toast.makeText(view.getContext(), "Note de frais déjà traitée", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



//        validateDepensesIfNoteFraisValide();


    }


    public void updateNoteFrais(){
        String NoteFraisAPI = API_URL + "notesdefrais/put";
        String result = "";
        HttpsPutRequest request;

        currentNoteFrais = new NoteFrais(currentNote);

        if (!noteLibelle.getText().toString().isEmpty() && !noteDate.getText().toString().isEmpty()){
            currentNoteFrais.setLibelle(noteLibelle.getText().toString());
            currentNoteFrais.setDateFrais(noteDate.getText().toString());
            currentNoteFrais.setCommentaireFrais(noteComment.getText().toString());

            request = new HttpsPutRequest();

            try {
                result = request.execute(NoteFraisAPI, currentNoteFrais.toJSON()).get();
                JSONObject response = new JSONObject(result);
                if (response.getBoolean("update")){
                    Intent intent = new Intent(NoteFraisActivity.this, MainActivity.class);
                    intent.putExtra("USER_JSON", currentUser.toString());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "Modification de la note de frais a echoué.", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Merci de bien vouloir remplir tous les champs.", Toast.LENGTH_SHORT).show();
        }
    }


    public void validateDepensesIfNoteFraisValide(){
        try {
            System.out.println("ETAT_TEST : " + this.currentNote.getString("etat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (this.currentNote.getString("etat").equals("Validé")) {
                String fraisApi = this.API_URL + "notesdefrais/depenses/frais/put";
                String trajetApi = this.API_URL + "notesdefrais/depenses/trajet/put";
                String result = "";
                for ( ListItem depense : DepensesList) {
                    switch (depense.getListItemType()){
                        case ListItem.Frais:
                            if (((Frais) depense).getEtatValidation().equals("En cours")){
                                HttpsPutRequest request = new HttpsPutRequest();
                                System.out.println("API URL : " + fraisApi);
                                try {
                                    result = request.execute(fraisApi, ((Frais) depense).toJSON()).get();
                                    Log.v("Depense_RESPONSE ", result);
                                    JSONObject response = new JSONObject(result);
                                    if (response.getBoolean("update")){
                                        this.getDepensesForNote();
                                    }else{
                                        Log.v("ERROR ", "ERRRRRRRR");
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case ListItem.Trajet:

                            break;
                    }
                }
            }else if(this.currentNote.getString("etat").equals("Refusé")){
                String fraisApi = this.API_URL + "notesdefrais/depenses/frais/put";
                String trajetApi = this.API_URL + "notesdefrais/depenses/trajet/put";
                String result = "";
                if (!this.DepensesList.isEmpty()){
                    for ( ListItem depense : DepensesList) {
                        switch (depense.getListItemType()){
                            case ListItem.Frais:
                                System.out.println("JSON_TEST : " + ((Frais) depense).getEtatValidation() + ((Frais) depense).getMontantDepense());
                                switch (((Frais) depense).getEtatValidation()){
                                    case "En cours":
                                        HttpsPutRequest request = new HttpsPutRequest();
                                        System.out.println("JSON testtttt : " + ((Frais) depense).toJSON());
                                        try {
                                            ((Frais) depense).setEtatValidation("Refusé");
                                            result = request.execute(fraisApi, ((Frais) depense).toJSON()).get();
                                            Log.v("Depense_RESPONSE ", result);
        //                                    JSONObject response = new JSONObject(result);
        //                                    if (response.getBoolean("update")){
        //                                        this.getDepensesForNote();
        //                                    }else{
        //                                        Log.v("ERROR ", "ERRRRRRRR");
        //                                    }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                        break;
                                }
                                break;
                            case ListItem.Trajet:

                                break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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


    public void getDepensesForNote() {
        HttpsGetRequest request = new HttpsGetRequest();
        String result = null;
        String API = API_URL;
        try {
            API = API+"depenses/get/"+currentNote.getInt("codeFrais");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            System.out.println("DEPENSEURL : " + API);
            result = request.execute(API).get();
            Log.v("RETOUR DEPENSES", result+"");
            JSONObject depenseArray = new JSONObject(result);
            JSONArray FraisType = depenseArray.getJSONArray("Frais");
            //System.out.println("FRAIS ARRAY : "+ FraisType.toString()) ;
            JSONArray Trajet = depenseArray.getJSONArray("Trajet");
            //System.out.println("TRAJET ARRAY : " + Trajet.toString());
            JSONObject currentDepense;
            Depense currentD;

            DepensesList = new ArrayList<ListItem>();

            for (int i = 0; i < FraisType.length(); i++){
                currentDepense = FraisType.getJSONObject(i);
                System.out.println("JSON-"+i + "  -  " + currentDepense.toString());
                currentD = new Frais(currentDepense);
                currentD.setMontantDepense(currentDepense.getDouble("montantDepense"));
                currentD.setEtatValidation(currentDepense.getString("etatValidation"));


                this.DepensesList.add((Frais)currentD);
            }

            for (int i = 0; i < Trajet.length(); i++){
                currentDepense = Trajet.getJSONObject(i);
                currentD = new Trajet(currentDepense);
                currentD.setMontantDepense(currentDepense.getDouble("montantDepense"));
                currentD.setEtatValidation(currentDepense.getString("etatValidation"));

                this.DepensesList.add((Trajet)currentD);
            }

            this.depenseRecyclerview = findViewById(R.id.depenseRecycler);
            depenseRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            depenseRecyclerview.setAdapter(new FraisAdapter(DepensesList, null, this.currentNote, this.currentUser));

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
//            Log.v("RESPONSE", response);
            JSONObject JsonResponse = new JSONObject(response);
            if (JsonResponse.getBoolean("response")){
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


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            noteDate.setText(new StringBuilder().append((day<10?("0"+day):(day)))
                    .append("/").append((month<10?("0"+month):(month))).append("/").append(year));
        }

    };


    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
    }
}
