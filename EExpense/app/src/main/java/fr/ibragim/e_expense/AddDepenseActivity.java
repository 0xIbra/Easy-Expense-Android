package fr.ibragim.e_expense;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.Locale;

import fr.ibragim.e_expense.Fragments.FraisFragment;
import fr.ibragim.e_expense.Fragments.TrajetFragment;
import fr.ibragim.e_expense.Metier.Depense;
import fr.ibragim.e_expense.Metier.Trajet;
import fr.ibragim.e_expense.Views.FragmentType;
import fr.ibragim.e_expense.Views.ListItem;

public class AddDepenseActivity extends AppCompatActivity implements FraisFragment.OnFragmentInteractionListener, TrajetFragment.OnFragmentInteractionListener{


    private int year;
    private int month;
    private int day;

    private ImageView addPicture;
    static final int DATE_PICKER_ID = 1111;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private static String[] PERMISSIONS_STORAGE_CAMERA = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT
    };
    private Uri imageUri;


    private String selectedFragmentType;
    private FragmentType test = null;


    //Depense Fields
    private EditText Output;
    EditText depenseDescriptionField;
    EditText depenseMontantField;



    //DEPENSE VARIABLES
    private int depenseId;
    private String depenseLibelle;
    private String depenseDate;
    private String depenseEtat;
    private double depenseDistance;
    private double depenseDuree;
    private String depenseDateAller;
    private String depenseDateRetour;
    private String depenseVilleDepart;
    private String depenseVilleArrivee;
    private int codeFrais;
    private double depenseMontant;
    private String depenseDetails;


    ListItem depense = null;


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

        // Get current date by calender
        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Output.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year));


        Intent intent = getIntent();
        if (intent != null){
            selectedFragmentType = intent.getStringExtra("TYPE_DEPENSE");
            String intentEx = intent.getStringExtra("EXISTING");
            System.out.println("TEST INTENT "+selectedFragmentType);
            System.out.println("SELECTEDTYPE "+ selectedFragmentType);
            System.out.println("EXISTING "+intentEx);
            if (intentEx.equals("TRUE")){
                if (selectedFragmentType.equals("Trajet")){
                    depenseId = intent.getIntExtra("DEPENSE_ID", 0);
                    codeFrais = intent.getIntExtra("DEPENSE_CODEFRAIS", 0);
                    depenseLibelle = intent.getStringExtra("DEPENSE_LIBELLE");
                    depenseDate = intent.getStringExtra("DEPENSE_DATE");
                    depenseEtat = intent.getStringExtra("DEPENSE_ETAT");
                    depenseDistance = intent.getDoubleExtra("DEPENSE_DISTANCE", 0);
                    depenseDuree = intent.getDoubleExtra("DEPENSE_DUREE", 0);
                    depenseDateAller = intent.getStringExtra("DEPENSE_DATE_ALLER");
                    depenseDateRetour = intent.getStringExtra("DEPENSE_DATE_RETOUR");
                    depenseVilleDepart = intent.getStringExtra("DEPENSE_VILLE_DEPART");
                    depenseVilleArrivee = intent.getStringExtra("DEPENSE_VILLE_ARRIVEE");
                    depenseMontant = intent.getDoubleExtra("DEPENSE_MONTANT", 0);
                    this.depense = new Trajet(depenseId, depenseLibelle, depenseDate, 0, depenseEtat, null, depenseMontant, codeFrais, 0, depenseDuree, depenseVilleDepart, depenseVilleArrivee, depenseDateAller, depenseDateRetour, depenseDistance, depenseId, codeFrais);
                    System.out.println("TOSTRING " + this.depense.toString());
                }else if (selectedFragmentType.equals("Frais")){
                    depenseLibelle = intent.getStringExtra("DEPENSE_LIBELLE");
                    depenseDetails = intent.getStringExtra("DEPENSE_DETAILS");
                    depenseDate = intent.getStringExtra("DEPENSE_DATE");
                    ifExistsInitDepense(selectedFragmentType);
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
                test = new TrajetFragment();
                ((TrajetFragment) test).initCurrentDepense((Trajet) this.depense);
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
    }

    //public void setTrajetFields(TrajetFragment fragment){
    //    fragment.fillTrajetFieldsByObject(depenseDistance, depenseDuree, depenseDateAller, depenseDateRetour, depenseVilleDepart, depenseVilleArrivee);
    //}


    public void ifExistsInitDepense(String typeFragment){
        switch (typeFragment){
            case "Frais":
                this.Output.setText(depenseDate);
                depenseDescriptionField.setText(depenseDetails);
                depenseMontantField.setText(String.valueOf(depenseMontant));
                break;

            case "Trajet":
                Output.setText(depenseDate);
                depenseDescriptionField.setText("");
                depenseMontantField.setText(String.valueOf(depenseMontant));
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
        Locale.setDefault(Locale.FRANCE);
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
            Output.setText(new StringBuilder().append(day)
                    .append("/").append(month + 1).append("/").append(year)
                    .append(" "));
        }

    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

