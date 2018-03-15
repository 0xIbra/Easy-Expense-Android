package fr.ibragim.e_expense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.network.HttpGetRequest;

public class LoginActivity extends AppCompatActivity {
    String result = "";
    ArrayList<String> itemList;
    String login;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailField = findViewById(R.id.loginField);
        final EditText passField = findViewById(R.id.passField);
        Button btn = findViewById(R.id.btnconnect);
        final Switch rememberSw = findViewById(R.id.rememberSwitch);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String myUrl = "https://votingsystem95ibra.000webhostapp.com/android.php";

                HttpGetRequest getRequest = new HttpGetRequest();



                try {
                    result = getRequest.execute(myUrl, "login="+emailField.getText().toString() +"&password="+ passField.getText().toString()).get(); // Exécution du Thread pour connexion.
                    Log.v("RETOUR ", result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject user = new JSONObject(result);
                    final String[] items = new String[6];
                    login = user.getString("email");
                    pass = user.getString("password");
                    items[2] = user.getString("nom");
                    items[3] = user.getString("prenom");
                    items[4] = user.getString("adresse");
                    items[5] = user.getString("ville");

                    //login = user.getString("email");
                    //pass = user.getString("password");



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(login);
                System.out.println(pass);

                if (emailField.getText().toString().equals(login) && passField.getText().toString().equals(pass)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    System.out.println("CA MARCHE PAS!!!");
                }


            }
        });

    }
}