package fr.ibragim.e_expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.Fragments.AccountFragment;
import fr.ibragim.e_expense.Fragments.MainFragment;
import fr.ibragim.e_expense.Fragments.StatFragment;
import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.Views.MainActivityFragmentType;
import fr.ibragim.e_expense.network.ConnectionDetector;
import fr.ibragim.e_expense.network.HttpsGetRequest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String USER_ID = "USER_ID";
    private final String USER_JSON = "USER_JSON";
    String result = "";
    String login;

    private final String USER_SESSION = "USER_SESSION";
    SharedPreferences userPrefs;
    protected String API_URL = "https://api.ibragim.fr/public/api/";
    protected HttpsGetRequest getRequest;
    protected ConnectionDetector connectionDetector;
    protected String user_email;

    //CURRENT USER
    protected int userid;
    protected String useremail;
    protected String usernom;
    protected String userprenom;
    protected String userpass;

    protected List<NoteFrais> NotesFrais = new ArrayList<NoteFrais>();
    RecyclerView r;

    //protected NoteFrais currentNote;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private FloatingActionButton fab;


    // FRAGMENTS
    private MainActivityFragmentType mainF;

    private ProgressBar progressBar;

    // Current user
    JSONObject user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.mainProgress);
        //progressBar.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        if (intent != null){
            try {
                user = new JSONObject(intent.getStringExtra(USER_JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        userPrefs = this.getSharedPreferences(USER_SESSION, MODE_PRIVATE);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.mesNotesFrais);


        View headerView = navigationView.getHeaderView(0);

        TextView navname = headerView.findViewById(R.id.navNameView);
        TextView navemail = headerView.findViewById(R.id.navEmailView);

        try {
            navname.setText(user.getString("prenomUtilisateur") + " " + user.getString("nomUtilisateur"));
            navemail.setText(user.getString("mailUtilisateur"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Intent i = new Intent(getApplicationContext(), NoteFraisActivity.class);
                i.putExtra("EXISTING", false);
                i.putExtra(USER_JSON, user.toString());
                startActivity(i);
            }
        });



        if (this.NotesFrais.isEmpty()){
            this.getNotes();
        }



        displaySelectedNavigation(R.id.mesNotesFrais);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void displaySelectedNavigation(int id){

        mainF = null;


        switch (id){
            case R.id.mesNotesFrais:
                mainF = new MainFragment();
                if (this.NotesFrais.isEmpty()){
                    this.getNotes();
                }
                ((MainFragment) mainF).setNotes(this.NotesFrais);
                ((MainFragment) mainF).setUser(this.user);
                fab.show();
                break;

            case R.id.statistics:
                mainF = new StatFragment();
                fab.hide();
                break;

            case R.id.nav_manage:
                mainF = new AccountFragment();
                fab.hide();
                break;

            case R.id.contact:

                break;

            case R.id.deconnexion:
                SharedPreferences.Editor editor = userPrefs.edit();
                editor.remove("USER_EMAIL");
                editor.remove("USER_PASS");
                editor.apply();
                Intent deco = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(deco);
                finish();
                break;
        }

        if (mainF != null){
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.mainContainer, (Fragment) mainF);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedNavigation(id);

        return true;
    }

    public void getNotes(){
        getRequest = new HttpsGetRequest();
        String res = "";
        try {
            String API = API_URL;
            API = API+"notesdefrais/get/"+user.getInt("idUtilisateur");
            System.out.println("API URL : " + API);
            res = getRequest.execute(API).get();
            Log.v("RETOUR ", res+"");
            JSONArray cards = new JSONArray(res);
            JSONObject currentCard;
            NoteFrais noteFrais;
            for (int i = 0; i < cards.length(); i++){
                currentCard = cards.getJSONObject(i);
                noteFrais = new NoteFrais(currentCard);
                NotesFrais.add(noteFrais);
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
