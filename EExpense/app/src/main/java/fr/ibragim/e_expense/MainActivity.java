package fr.ibragim.e_expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.AdaptListView.CardViewAdapter;
import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.network.HttpsPostRequest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected final String USER_EMAIL = "";
    private final int USER_ID = 0;
    String result = "";
    String login;
    String pass;
    private final String USER_SESSION = "USER_SESSION";
    SharedPreferences userPrefs;
    protected String API_URL = "https://e-expense.000webhostapp.com/Android.php";
    protected HttpsPostRequest getRequest;
    protected String user_email;

    //CURRENT USER
    protected String userid;
    protected String useremail;
    protected String usernom;
    protected String userprenom;
    private ArrayList<NoteFrais> listFrais;
    protected CardViewAdapter adapter;
    private ListView listCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listCards = findViewById(R.id.cardviewTemplate);


        Intent intent = getIntent();
        if (intent != null){
            user_email = intent.getStringExtra(USER_EMAIL);
            getRequest = new HttpsPostRequest();
            try {
                result = getRequest.execute(API_URL, "getUserSession=true&userEmail="+user_email.toString()).get();
                Log.v("RETOUR : ", result);
                JSONObject user = new JSONObject(result);
                userid = user.getString("id");
                usernom = user.getString("nom");
                userprenom = user.getString("prenom");
                useremail = user.getString("email");
                System.out.println(usernom + " - " + userprenom);
                System.out.println(useremail);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        userPrefs = this.getSharedPreferences(USER_SESSION, MODE_PRIVATE);


        getNotes();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Intent i = new Intent(MainActivity.this, NoteFraisActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView navname = headerView.findViewById(R.id.navNameView);
        TextView navemail = headerView.findViewById(R.id.navEmailView);

        navname.setText(userprenom + " " + usernom);
        navemail.setText(useremail);


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.deconnexion) {
            SharedPreferences.Editor editor = userPrefs.edit();
            editor.remove("USER_EMAIL");
            editor.remove("USER_PASS");
            editor.apply();
            Intent deco = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(deco);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public ArrayList<CardView> getNotes(){
        HttpsPostRequest requette = new HttpsPostRequest();
        String params = "getNotes=true&userID="+userid;
        String result = null;
        try {
            result = requette.execute(API_URL, params).get();
            JSONArray listNotesJson = new JSONArray(result);
            final String[] items = new String[listNotesJson.length()];

            for (int i = 0; i < listNotesJson.length(); i++){
                JSONObject note = new JSONObject(listNotesJson.getString(i));
                this.listFrais.add(new NoteFrais(note.getInt("codeFrais"), note.getString("libelleNote"), note.getString("dateFrais"), note.getString("villeFrais"), note.getString("dateSoumission"), note.getString("commentaireFrais"), note.getInt("idUtilisateur"), note.getInt("idClient")));
            }
            //adapter = new CardViewAdapter(this,R.layout.activity_main, R.id.cardviewTemplate, listNotesJson);
            //listCards.setAdapter(adapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
