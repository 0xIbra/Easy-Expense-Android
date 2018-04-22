package fr.ibragim.e_expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.Session.UserSession;
import fr.ibragim.e_expense.network.ConnectionDetector;
import fr.ibragim.e_expense.network.HttpsGetRequest;

public class LoginActivity extends AppCompatActivity {
    private final String USER_ID = "USER_ID";
    private final String USER_EMAIL = "USER_EMAIL";
    private final String USER_PASS = "USER_PASS";
    private final String USER_JSON = "USER_JSON";

    private String result = "";
    private String userPass = "";
    private String userEmail = "";
    private final String USER_SESSION = "USER_SESSION";
    SharedPreferences userPrefs;
    protected String API_URL;
    protected HttpsGetRequest getRequest;
    private int userID;

    ConnectionDetector connectionDetector;

    // Current user
    JSONObject user;


    //Layout resources
    EditText emailField;
    EditText passField;
    Button btn ;
    Switch rememberSw ;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userPrefs = getSharedPreferences(this.USER_SESSION, MODE_PRIVATE);
        API_URL = "http://easy-expense.tk/public/index.php/api/utilisateur/auth/{mail}/{password}";

        progressBar = findViewById(R.id.progressDialog);

        emailField = findViewById(R.id.loginField);
        passField  = findViewById(R.id.passField);
        btn = findViewById(R.id.btnconnect);
        rememberSw = findViewById(R.id.rememberSwitch);

        SessionCheckAndConnect();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Authentification(view);
                //progressBar.setVisibility(View.VISIBLE);
            }


        });

    }



    public void Authentification(View view){
        connectionDetector = new ConnectionDetector(getApplicationContext());

        if (connectionDetector.isConnected()){
            int userid = 0;
            String userEmail = null;
            String userPass = null;
            try {
                getRequest = new HttpsGetRequest();
                API_URL = API_URL.replace("{mail}", emailField.getText().toString());
                API_URL = API_URL.replace("{password}", passField.getText().toString());
                System.out.println("GET API URL : " + API_URL);
                result = getRequest.execute(API_URL).get();
                Log.v("RETOUR ", result+"");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            try {
                user = new JSONObject(result);
                userEmail = user.getString("mailUtilisateur");
                userPass = user.getString("mdpUtilisateur");
                userid = user.getInt("idUtilisateur");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (userEmail.equals(emailField.getText().toString()) && userPass.equals(passField.getText().toString())){

                if (rememberSw.isChecked()){
                    UserSession.setSharedPrefs(userPrefs, userEmail, userPass);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(USER_JSON, user.toString());
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(USER_JSON, user.toString());
                    startActivity(intent);
                    finish();
                }

            }else{
                Snackbar.make(view, "Email ou Mot de passe incorrects", Snackbar.LENGTH_SHORT).show();
            }

        }else{
            Snackbar.make(view, "Aucune connexion internet détectée", Snackbar.LENGTH_SHORT).show();
        }
    }


    public void SessionCheckAndConnect(){
        boolean SessionCheck = UserSession.CheckSession(userPrefs);
        if (SessionCheck){
            connectionDetector = new ConnectionDetector(getApplicationContext());
            if (connectionDetector.isConnected()){
                Map<String, ?> userS = UserSession.getUserSession(userPrefs);
                String params;
                String sharedEmail = userS.get("USER_EMAIL").toString();
                String sharedPass = userS.get("USER_PASS").toString();


                try {
                    API_URL = API_URL.replace("{mail}", sharedEmail);
                    API_URL = API_URL.replace("{password}", sharedPass);
                    System.out.println("SESSION API URL : " + API_URL);
                    getRequest = new HttpsGetRequest();
                    result = getRequest.execute(API_URL).get();
                    System.out.println("SESSION RETOUR : "+result+"");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    user = new JSONObject(result);
                    userEmail = user.getString("mailUtilisateur");
                    userPass = user.getString("mdpUtilisateur");
                    userID = user.getInt("idUtilisateur");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (userEmail.equals(sharedEmail) && userPass.equals(sharedPass)){
                    Intent reco = new Intent(getApplicationContext(), MainActivity.class);
                    reco.putExtra(USER_JSON, user.toString());
                    startActivity(reco);
                    finish();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Aucune connexion internet détectée", Toast.LENGTH_SHORT).show();
            }


        }
    }

}