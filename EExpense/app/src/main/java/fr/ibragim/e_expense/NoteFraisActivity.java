package fr.ibragim.e_expense;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteFraisActivity extends AppCompatActivity {
    private EditText noteDate;
    private EditText noteComment;
    private EditText noteLibelle;
    private Button noteSubmit;
    private final String API_URL = "https://api.ibragim.fr/Android.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_frais);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.customToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        noteLibelle = findViewById(R.id.noteLibelle);
        noteComment = findViewById(R.id.noteComment);
        //noteSubmit = findViewById(R.id.noteSubmit);

        FloatingActionButton noteSubmit = (FloatingActionButton) findViewById(R.id.noteSubmit);
        noteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteFraisActivity.this, DepenseActivity.class);
                startActivity(i);
            }
        });
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
}
