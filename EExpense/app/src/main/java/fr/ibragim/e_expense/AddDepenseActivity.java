package fr.ibragim.e_expense;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.Fragments.FraisFragment;
import fr.ibragim.e_expense.Fragments.TrajetFragment;
import fr.ibragim.e_expense.Metier.Depense;
import fr.ibragim.e_expense.Metier.Frais;
import fr.ibragim.e_expense.Metier.Justificatif;
import fr.ibragim.e_expense.Metier.Trajet;
import fr.ibragim.e_expense.Views.FragmentType;
import fr.ibragim.e_expense.Views.ListItem;
import fr.ibragim.e_expense.Widgets.DateFormat;
import fr.ibragim.e_expense.Widgets.ImageCompressor;
import fr.ibragim.e_expense.network.HttpsDeleteRequest;
import fr.ibragim.e_expense.network.HttpsGetRequest;
import fr.ibragim.e_expense.network.HttpsPostRequest;
import fr.ibragim.e_expense.network.HttpsPutRequest;
import fr.ibragim.e_expense.network.ImageLoader;

public class AddDepenseActivity extends AppCompatActivity implements FraisFragment.OnFragmentInteractionListener, TrajetFragment.OnFragmentInteractionListener{

    // CURRENT USER
    private JSONObject currentUser;
    private JSONObject currentDepense;
    private JSONObject currentNote;
    private JSONObject currentJustificatif;

    private Depense Depense;

    private int year;
    private int month;
    private int day;

    private ImageView addPicture;

    private static final int DATE_ALLER = 0;
    private static final int DATE_RETOUR = 1;
    static final int DATE_PICKER_ID = 1111;


    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private static String[] PERMISSIONS_STORAGE_CAMERA = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT
    };


    // Image variables
    private Uri imageUri;
    private String encodedImage = "";


    private String selectedFragmentType;
    private FragmentType test = null;


    //Depense Fields
    private EditText Output;
    private EditText depenseDescriptionField;
    private EditText depenseMontantField;
    private EditText libelleDepense;

    String API = "https://api.ibragim.fr/public/api/";

    ListItem depense = null;
    private FloatingActionButton depenseSubmit, depenseDelete;

    private String existing;


    //@RequiresApi(api = Build.VERSION_CODES.N)
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_depense);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.customToolbar);

        toolbar.setTitle("Ajouter une dépense");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addPicture = findViewById(R.id.addPicture);
        Output =  findViewById(R.id.Output);
        depenseDescriptionField = findViewById(R.id.depenseDescription);
        depenseMontantField = findViewById(R.id.depenseMontantField);
        libelleDepense = findViewById(R.id.libelleDepense);

        depenseSubmit = findViewById(R.id.depenseSubmit);
        depenseDelete = findViewById(R.id.depenseDelete);


        // Get current date by calender
        final java.util.Calendar c = java.util.Calendar.getInstance();

        year = c.get(java.util.Calendar.YEAR);
        month = c.get(java.util.Calendar.MONTH);
        day = c.get(java.util.Calendar.DAY_OF_MONTH);

//        final Calendar c = Calendar.getInstance();
//
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);

        Output.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year));


        Intent intent = getIntent();
        if (intent != null){
            selectedFragmentType = intent.getStringExtra("TYPE_DEPENSE");
            String intentEx = intent.getStringExtra("EXISTING");
            existing = intentEx;
            if (intentEx.equals("TRUE")){
                try {
                    currentNote = new JSONObject(intent.getStringExtra("NOTEFRAIS_JSON"));
                    currentUser = new JSONObject(intent.getStringExtra("CURRENT_USER"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (selectedFragmentType.equals("Trajet")){
                    try {
                        currentDepense = new JSONObject(intent.getStringExtra("DEPENSE_JSON"));
                        getJustificatif();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {

                        ifExistsInitDepense(selectedFragmentType);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else if (selectedFragmentType.equals("Frais")){
                    try {
                        currentDepense = new JSONObject(intent.getStringExtra("DEPENSE_JSON"));
                        getJustificatif();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        ifExistsInitDepense(selectedFragmentType);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }else if(intentEx.equals("FALSE")){
                try {
                    currentNote = new JSONObject(intent.getStringExtra("NOTEFRAIS_JSON"));
                    currentUser = new JSONObject(intent.getStringExtra("CURRENT_USER"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //System.out.println("DEPENSE_LIBELLE " + depenseLibelle);


        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();



        switch (selectedFragmentType){
            case "Frais":
                test = new FraisFragment();
                //transaction.add(R.id.middleContainer, (Fragment) test);
                break;
            case "Trajet":
                this.depenseDescriptionField.setVisibility(View.GONE);
                test = new TrajetFragment();
                ((TrajetFragment) test).initCurrentDepense(currentDepense);
                transaction.add(R.id.middleContainer, (Fragment) test);
                break;
        }
        transaction.commit();


        Output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID);
            }
        });


        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());
        Log.v("Path image", this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath());
        addPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Capture d'écran en cours", Toast.LENGTH_LONG).show();
                int permission = ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // Nous n'avons pas la permission de stockée dans le
                    // PackageManager pour READ et WRITE sur external storage + Utiliser LA CAMERA
                    ActivityCompat.requestPermissions(
                            AddDepenseActivity.this,
                            PERMISSIONS_STORAGE_CAMERA,
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
                    );
                } else {
                    takePictureIntent();
                }

            }
        });

        if (existing.equals("TRUE")){
            try {
                if (currentDepense.getString("etatValidation").equals("Validé") || currentDepense.getString("etatValidation").equals("Refusé")){
                    libelleDepense.setEnabled(false);
                    Output.setEnabled(false);
                    depenseDescriptionField.setEnabled(false);
                    depenseMontantField.setEnabled(false);
                    depenseSubmit.hide();
                    depenseDelete.hide();
                    Toast.makeText(this, "La dépense a déjà été traitée.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            depenseDelete.hide();
        }


        // Validation de la depense
        depenseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (existing.equals("TRUE")){
                    try {
                        updateDepense();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (existing.equals("FALSE")){
                    ValidateDepense();
                }
            }
        });

        depenseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (existing.equals("TRUE")){
                        deleteDepense();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void getJustificatif() throws JSONException, ExecutionException, InterruptedException {
        String result = "";
        HttpsGetRequest request;
        String J_API = API + "justificatifs/" + currentDepense.getInt("idDepense");
        System.out.println("JUSTIF API " + J_API);

        request = new HttpsGetRequest();
        result = request.execute(J_API).get();
        System.out.println("JUSTIF " + result);
        JSONObject response = new JSONObject(result);
        if (response.getBoolean("response")){
            this.encodedImage = "";
            currentJustificatif = response.getJSONObject("result");
        }
    }


    public void deleteDepense() throws JSONException, ExecutionException, InterruptedException {
        String result = "";
        HttpsDeleteRequest request;

        String DeleteAPI = API;

        if (selectedFragmentType.equals("Frais")){

            if (!this.currentDepense.getString("etatValidation").equals("Validé") || !this.currentDepense.getString("etatValidation").equals("Refusé")){
                DeleteAPI = DeleteAPI + "depenses/frais/delete";
                request = new HttpsDeleteRequest();
                result = request.execute(DeleteAPI, currentDepense.toString()).get();
                JSONObject response = new JSONObject(result);
                if (response.getBoolean("response")){
                    Intent intent = new Intent(this, NoteFraisActivity.class);
                    intent.putExtra("EXISTING", true);
                    intent.putExtra("USER_JSON", currentUser.toString());
                    intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "Suppression a échoué", Toast.LENGTH_SHORT).show();
                }
            }

        }else if (selectedFragmentType.equals("Trajet")){
            if (!this.currentDepense.getString("etatValidation").equals("Validé") || !this.currentDepense.getString("etatValidation").equals("Refusé")){
                DeleteAPI = DeleteAPI + "depenses/trajet/delete";
                request = new HttpsDeleteRequest();
                result = request.execute(DeleteAPI, currentDepense.toString()).get();
                JSONObject response = new JSONObject(result);
                if (response.getBoolean("response")){
                    Intent intent = new Intent(this, NoteFraisActivity.class);
                    intent.putExtra("EXISTING", true);
                    intent.putExtra("USER_JSON", currentUser.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "Suppression a échoué", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    public void ValidateDepense(){
        HttpsPostRequest request;
        String result = "";

        if (selectedFragmentType.equals("Frais")){
            request = new HttpsPostRequest();
            String API_URL  = API + "depenses/frais/add";

            if (!depenseMontantField.getText().toString().isEmpty() && !libelleDepense.getText().toString().isEmpty() && !Output.getText().toString().isEmpty()){
                String libelle = this.libelleDepense.getText().toString();
                String date = this.Output.getText().toString();
                String description = this.depenseDescriptionField.getText().toString();
                String montantStr = this.depenseMontantField.getText().toString();
                double montant = Double.parseDouble(montantStr);
                Frais frais = new Frais();
                frais.setIntituleFrais(libelle);
                frais.setDateFrais(date);
                frais.setDateDepense(date);
                frais.setDetailsFrais(description);
                frais.setMontantDepense(montant);
                try {
                    frais.setCodeFrais(currentNote.getInt("codeFrais"));
                    frais.setIdUtilisateur(currentNote.getInt("idUtilisateur"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
//                    Log.v("JSONOBJECT ", frais.toJSON());
                    result = request.execute(API_URL, frais.toJSON()).get();
//                    Log.v("RESPONSE ADD ", result);

                    JSONObject response = new JSONObject(result);
                    if (response.getBoolean("response")){

                        if (!this.encodedImage.equals("")){
                            Justificatif justificatif = new Justificatif();
                            justificatif.setTitreJustificatif(frais.getIntituleFrais());
                            justificatif.setCodeFrais(frais.getCodeFrais());
                            justificatif.setIdDepense(response.getInt("insertedId"));
                            justificatif.setUrlJustiifcatif(this.encodedImage);
                            String slug = justificatif.getIdDepense() + justificatif.getTitreJustificatif() + frais.getDateDepense() + currentUser.getString("nomUtilisateur") + currentUser.getString("prenomUtilisateur");
                            justificatif.setSlug(slug);

                            String J_API = API + "justificatifs/post";
                            System.out.println(J_API);
                            HttpsPostRequest postRequest = new HttpsPostRequest();


                            String res = "";

                            res = postRequest.execute(J_API, justificatif.toJSON()).get();
                            JSONObject respons = new JSONObject(res);
                            if (respons.getBoolean("response")){
                                Toast.makeText(this, "Enregistrement du justificatif réussi.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Ajout du justificatif echoué", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Intent intent = new Intent(this, NoteFraisActivity.class);
                        intent.putExtra("EXISTING", true);
                        intent.putExtra("USER_JSON", currentUser.toString());
                        intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(this, "Ajout de la depense echoué", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(this, "Merci de bien vouloir remplir tous les champs", Toast.LENGTH_SHORT).show();
            }

        }else if (selectedFragmentType.equals("Trajet")){
            String TRAJETAPI  = this.API + "depenses/trajet/add";

            if (!depenseMontantField.getText().toString().isEmpty() && !libelleDepense.getText().toString().isEmpty() && !Output.getText().toString().isEmpty() &&
                    !((TrajetFragment) test).getDistanceField().isEmpty() && !((TrajetFragment) test).getDureeField().isEmpty() && !((TrajetFragment) test).getDateAller().isEmpty() &&
                    !((TrajetFragment) test).getDateRetour().isEmpty() && !((TrajetFragment) test).getVilleDepart().isEmpty() && !((TrajetFragment) test).getVilleArrivee().isEmpty()){
                String libelle = this.libelleDepense.getText().toString();
                String date = this.Output.getText().toString();
                String description = this.depenseDescriptionField.getText().toString();
                String montantStr = this.depenseMontantField.getText().toString();
                double montant = Double.parseDouble(montantStr);
                double distanceKm = Double.parseDouble(((TrajetFragment) test).getDistanceField());
                double duree = Double.parseDouble(((TrajetFragment) test).getDureeField());
                String dateAller = ((TrajetFragment) test).getDateAller();
                String dateRetour = ((TrajetFragment) test).getDateRetour();
                String villeDepart = ((TrajetFragment) test).getVilleDepart();
                String villeArrivee = ((TrajetFragment) test).getVilleArrivee();

                Trajet trajet = new Trajet();
                try {
                    trajet.setLibelleTrajet(libelle);
                    trajet.setCodeFrais(currentNote.getInt("codeFrais"));
                    trajet.setDateDepense(date);
                    trajet.setMontantDepense(montant);
                    trajet.setDistanceKM(distanceKm);
                    trajet.setDureeTrajet(duree);
                    trajet.setDateAller(dateAller);
                    trajet.setDateRetour(dateRetour);
                    trajet.setVilleDepart(villeDepart);
                    trajet.setVilleArrivee(villeArrivee);
                    trajet.setIdUtilisateur(currentNote.getInt("idUtilisateur"));
                    trajet.setCodeFrais(currentNote.getInt("codeFrais"));
                    System.out.println("OUTPUTJSON " + trajet.toJSON());

                    HttpsPostRequest postRequest = new HttpsPostRequest();
                    result = postRequest.execute(TRAJETAPI, trajet.toJSON()).get();
                    JSONObject response = new JSONObject(result);
                    if (response.getBoolean("response")){


                        if (!this.encodedImage.equals("")){
                            Justificatif justificatif = new Justificatif();
                            justificatif.setTitreJustificatif(trajet.getLibelleTrajet());
                            justificatif.setCodeFrais(trajet.getCodeFrais());
                            justificatif.setIdDepense(response.getInt("insertedId"));
                            justificatif.setUrlJustiifcatif(this.encodedImage);
                            String slug = justificatif.getIdDepense() + justificatif.getTitreJustificatif() + trajet.getDateDepense() + currentUser.getString("nomUtilisateur") + currentUser.getString("prenomUtilisateur");
                            justificatif.setSlug(slug);

                            String J_API = API + "justificatifs/post";
                            System.out.println(J_API);
                            HttpsPostRequest postRequest1 = new HttpsPostRequest();


                            String res = "";

                            res = postRequest1.execute(J_API, justificatif.toJSON()).get();
                            JSONObject respons = new JSONObject(res);
                            if (respons.getBoolean("response")){
                                Toast.makeText(this, "Enregistrement du justificatif réussi.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Enregistrement du justificatif echoué", Toast.LENGTH_SHORT).show();
                            }
                        }


                        Intent intent = new Intent(this, NoteFraisActivity.class);
                        intent.putExtra("EXISTING", true);
                        intent.putExtra("USER_JSON", currentUser.toString());
                        intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this, "Erreur lors de l'ajout de la depense", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(this, "Merci de bien vouloir remplir tous les champs.", Toast.LENGTH_SHORT).show();
            }
        }


    }






    public void updateDepense() throws JSONException {
        HttpsPutRequest request;
        String result = "";

        if (selectedFragmentType.equals("Frais")){
            String FRAISAPI = this.API + "notesdefrais/depenses/frais/put";
            request = new HttpsPutRequest();

            if (!depenseMontantField.getText().toString().isEmpty() && !libelleDepense.getText().toString().isEmpty() && !Output.getText().toString().isEmpty()){
                String libelle = this.libelleDepense.getText().toString();
                String date = this.Output.getText().toString();
                String description = this.depenseDescriptionField.getText().toString();
                String montantStr = this.depenseMontantField.getText().toString();
                double montant = Double.parseDouble(montantStr);
                this.Depense = new Frais(currentDepense);
                ((Frais) Depense).setIntituleFrais(libelle);
                ((Frais) Depense).setDateFrais(date);
                ((Frais) Depense).setDateDepense(date);
                ((Frais) Depense).setDetailsFrais(description);
                ((Frais) Depense).setMontantDepense(montant);
                try {
                    ((Frais) Depense).setIdUtilisateur(currentNote.getInt("idUtilisateur"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    ((Frais) Depense).setId(Integer.parseInt(currentDepense.getString("idDepense")));
                    ((Frais) Depense).setCodeFrais(currentNote.getInt("codeFrais"));
                    ((Frais) Depense).setIdUtilisateur(currentNote.getInt("idUtilisateur"));

                    result = request.execute(FRAISAPI, ((Frais) Depense).toJSON()).get();
                    JSONObject response = new JSONObject(result);
                    if (response.getBoolean("update")){


                        if (!this.encodedImage.equals("")){
                            Justificatif justificatif = new Justificatif();
                            justificatif.setTitreJustificatif(((Frais) Depense).getIntituleFrais());
                            justificatif.setCodeFrais(((Frais) Depense).getCodeFrais());
                            justificatif.setIdDepense(((Frais) Depense).getId());
                            justificatif.setUrlJustiifcatif(this.encodedImage);
                            String slug = justificatif.getIdDepense() + justificatif.getTitreJustificatif() + ((Frais) Depense).getDateDepense() + currentUser.getString("nomUtilisateur") + currentUser.getString("prenomUtilisateur");
                            justificatif.setSlug(slug);

                            String J_API = API + "justificatifs/put";
                            System.out.println(J_API);
                            HttpsPutRequest putRequest = new HttpsPutRequest();


                            String res = "";

                            res = putRequest.execute(J_API, justificatif.toJSON()).get();
                            JSONObject respons = new JSONObject(res);
                            if (respons.getBoolean("response")){
                                Toast.makeText(this, "Enregistrement du justificatif réussi.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Enregistrement du justificatif echoué", Toast.LENGTH_SHORT).show();
                            }
                        }


                        Intent intent = new Intent(this, NoteFraisActivity.class);
                        intent.putExtra("EXISTING", true);
                        intent.putExtra("USER_JSON", currentUser.toString());
                        intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this, "Modification de la depense echoué.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        }else if (selectedFragmentType.equals("Trajet")){
            if (!depenseMontantField.getText().toString().isEmpty() && !libelleDepense.getText().toString().isEmpty() && !Output.getText().toString().isEmpty() &&
                    !((TrajetFragment) test).getDistanceField().isEmpty() && !((TrajetFragment) test).getDureeField().isEmpty() && !((TrajetFragment) test).getDateAller().isEmpty() &&
                    !((TrajetFragment) test).getDateRetour().isEmpty() && !((TrajetFragment) test).getVilleDepart().isEmpty() && !((TrajetFragment) test).getVilleArrivee().isEmpty()){

                String APITRAJET = this.API + "notesdefrais/depenses/trajet/put";

                String libelle = this.libelleDepense.getText().toString();
                String date = this.Output.getText().toString();
                String description = this.depenseDescriptionField.getText().toString();
                String montantStr = this.depenseMontantField.getText().toString();
                double montant = Double.parseDouble(montantStr);
                double distanceKm = Double.parseDouble(((TrajetFragment) test).getDistanceField());
                double duree = Double.parseDouble(((TrajetFragment) test).getDureeField());
                String dateAller = ((TrajetFragment) test).getDateAller();
                String dateRetour = ((TrajetFragment) test).getDateRetour();
                String villeDepart = ((TrajetFragment) test).getVilleDepart();
                String villeArrivee = ((TrajetFragment) test).getVilleArrivee();

                this.Depense = new Trajet(currentDepense);
                ((Trajet) Depense).setLibelleTrajet(libelle);
                ((Trajet) Depense).setDateDepense(date);
                ((Trajet) Depense).setMontantDepense(montant);
                ((Trajet) Depense).setDistanceKM(distanceKm);
                ((Trajet) Depense).setDureeTrajet(duree);
                ((Trajet) Depense).setDateAller(dateAller);
                ((Trajet) Depense).setDateRetour(dateRetour);
                ((Trajet) Depense).setVilleDepart(villeDepart);
                ((Trajet) Depense).setVilleArrivee(villeArrivee);
                ((Trajet) Depense).setIdUtilisateur(currentNote.getInt("idUtilisateur"));

                request = new HttpsPutRequest();
                System.out.println("TRAJET JSON  "  +  ((Trajet) Depense).toJSON());
                try {
                    result = request.execute(APITRAJET, ((Trajet) Depense).toJSON()).get();
                    JSONObject response = new JSONObject(result);
                    if (response.getBoolean("update")){


                        if (!this.encodedImage.equals("")){
                            Justificatif justificatif = new Justificatif();
                            justificatif.setTitreJustificatif(((Trajet) Depense).getLibelleTrajet());
                            justificatif.setCodeFrais(((Trajet) Depense).getCodeFrais());
                            justificatif.setIdDepense(((Trajet) Depense).getId());
                            justificatif.setUrlJustiifcatif(this.encodedImage);
                            String slug = justificatif.getIdDepense() + justificatif.getTitreJustificatif() + ((Trajet) Depense).getDateDepense() + currentUser.getString("nomUtilisateur") + currentUser.getString("prenomUtilisateur");
                            justificatif.setSlug(slug);

                            String J_API = API + "justificatifs/put";
                            System.out.println(J_API);
                            HttpsPutRequest putRequest = new HttpsPutRequest();


                            String res = "";

                            res = putRequest.execute(J_API, justificatif.toJSON()).get();
                            JSONObject respons = new JSONObject(res);
                            if (respons.getBoolean("response")){
                                Toast.makeText(this, "Enregistrement du justificatif réussi.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Enregistrement du justificatif echoué", Toast.LENGTH_SHORT).show();
                            }
                        }


                        Intent intent = new Intent(this, NoteFraisActivity.class);
                        intent.putExtra("EXISTING", true);
                        intent.putExtra("USER_JSON", currentUser.toString());
                        intent.putExtra("NOTEFRAIS_JSON", currentNote.toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this, "Modification a echoué", Toast.LENGTH_SHORT).show();
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
    }

    public void ifExistsInitDepense(String typeFragment) throws JSONException {

        if (currentJustificatif != null){

            ImageLoader imageLoader = new ImageLoader(addPicture);

            imageLoader.execute(currentJustificatif.getString("urlJustificatif"));


        }

        switch (typeFragment){
            case "Frais":
                this.Output.setText(currentDepense.getString("dateDepense"));
                depenseDescriptionField.setText(currentDepense.getString("detailsFrais"));
                try {
                    depenseMontantField.setText(String.valueOf(currentDepense.getDouble("montantDepense")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Output.setText(currentDepense.getString("dateDepense"));
                    libelleDepense.setText(currentDepense.getString("libelleFrais"));
                    depenseDescriptionField.setText(currentDepense.getString("detailsFrais"));
                    Log.v("EXISTINGVALUE ", currentDepense.toString());
                    depenseMontantField.setText(currentDepense.getString("montantDepense"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "Trajet":

                libelleDepense.setText(currentDepense.getString("libelleTrajet"));
                Output.setText(DateFormat.parseDMY(currentDepense.getString("dateDepense"), "yyyy-mm-dd", "dd/mm/yyyy"));
                depenseMontantField.setText(String.valueOf(currentDepense.getDouble("montantDepense")));
                break;
        }


    }


    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.v("Path image", this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath());
        imageUri = Uri.fromFile(new
                File(this.getExternalFilesDir(Environment.DIRECTORY_DCIM), "justificatif_" +
                String.valueOf(System.currentTimeMillis()) + ".jpg"));
        takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                retrieveCapturedPicture();
            }
        }
    }

    private void retrieveCapturedPicture() {
        // display picture on ImageView or write path of image file into database SQLite
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
        addPicture.setImageBitmap(bitmap);
        addPicture.setRotation(90);

        ImageEncode encoder = new ImageEncode(bitmap);

        try {
            this.encodedImage = encoder.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month, day);

            case DATE_ALLER:
                return new DatePickerDialog(this, dateAllerListener, year, month, day);

            case DATE_RETOUR:
                return new DatePickerDialog(this, dateRetourListener, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth + 1;
            day = selectedDay;

            // Show selected date
            Output.setText(new StringBuilder().append((day<10?("0"+day):(day)))
                    .append("/").append((month<10?("0"+month):(month))).append("/").append(year));
        }

    };


    private DatePickerDialog.OnDateSetListener dateAllerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth + 1;
            day = selectedDay;

            // Show selected date
            ((TrajetFragment) test).setDateAllerString(new StringBuilder().append((day<10?("0"+day):(day)))
                    .append("/").append((month<10?("0"+month):(month))).append("/").append(year));
        }

    };


    private DatePickerDialog.OnDateSetListener dateRetourListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth + 1;
            day = selectedDay;

            // Show selected date
            ((TrajetFragment) test).setDateRetourString(new StringBuilder().append((day<10?("0"+day):(day)))
                    .append("/").append((month<10?("0"+month):(month))).append("/").append(year));
        }

    };


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ImageEncode extends AsyncTask<Void, Void, String> {
        private Bitmap bitmap;

        public ImageEncode(Bitmap bitmap){
            this.bitmap = bitmap;
            this.bitmap = ImageCompressor.compress(bitmap, 720);
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);



            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }




}

