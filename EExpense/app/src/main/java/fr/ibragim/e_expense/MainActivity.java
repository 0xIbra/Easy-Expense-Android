package fr.ibragim.e_expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.Metier.NoteFrais;
import fr.ibragim.e_expense.Views.Adapter;
import fr.ibragim.e_expense.network.HttpsPostRequest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected final String USER_TOKEN = "";
    private final String USER_ID = "USER_ID";
    String result = "";
    String login;
    String pass;
    private final String USER_SESSION = "USER_SESSION";
    SharedPreferences userPrefs;
    protected String API_URL = "http://api.ibragim.fr/Android.php";
    protected HttpsPostRequest getRequest;
    protected String user_email;

    //CURRENT USER
    protected int userid;
    protected String useremail;
    protected String userToken;
    protected String usernom;
    protected String userprenom;

    protected List<NoteFrais> NotesFrais = new ArrayList<NoteFrais>();
    RecyclerView r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        if (intent != null){
            userToken = intent.getStringExtra(USER_TOKEN);
            userid = intent.getIntExtra(USER_ID, 0);

            getRequest = new HttpsPostRequest();
            try {
                String params = "getUserSession=true&token="+userToken+"&userid="+userid;
                result = getRequest.execute(API_URL, params).get();
                JSONObject user = new JSONObject(result);
                userid = user.getInt("id");
                usernom = user.getString("nom");
                userprenom = user.getString("prenom");
                useremail = user.getString("email");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        userPrefs = this.getSharedPreferences(USER_SESSION, MODE_PRIVATE);

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

        getNotes();

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
            editor.remove("USER_TOKEN");
            editor.apply();
            Intent deco = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(deco);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getNotes(){
        HttpsPostRequest newReq = new HttpsPostRequest();
        String res = "";
        String params = "getNotes=true&userID="+userid;
        System.out.println("MY ID "+params);
        try {
            res = newReq.execute(API_URL, params).get();
            Log.v("RETOUR ", res);
            JSONArray cards = new JSONArray(res);
            JSONObject currentCard;
            for (int i = 0; i < cards.length(); i++){
                currentCard = cards.getJSONObject(i);
                int codeFrais = currentCard.getInt("codeFrais");
                String libelleNote = currentCard.getString("libelleNote");
                String dateF = currentCard.getString("dateFrais");
                String ville = currentCard.getString("villeFrais");
                String dateS = currentCard.getString("dateSoumission");
                String comm = currentCard.getString("commentaireFrais");
                int idUtilisateur = currentCard.getInt("idUtilisateur");
                int idClient = currentCard.getInt("idClient");


                NotesFrais.add(new NoteFrais(codeFrais, libelleNote, dateF, ville, dateS, comm, idUtilisateur, idClient));

            }

            r = findViewById(R.id.fragment_main_recycler_view);
            r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            r.setAdapter(new Adapter(NotesFrais, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Snackbar.make(view, "test "+i, Snackbar.LENGTH_SHORT).show();
                }
            }));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
