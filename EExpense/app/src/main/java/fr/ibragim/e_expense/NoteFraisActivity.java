package fr.ibragim.e_expense;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteFraisActivity extends AppCompatActivity {
    private EditText noteDate;
    private EditText noteComment;
    private EditText noteLibelle;
    private Button noteSubmit;

    private final String url = "https://e-expense.000webhostapp.com/Android.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_frais);
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
}
