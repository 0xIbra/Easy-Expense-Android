package fr.ibragim.e_expense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.Session.UserSession;
import fr.ibragim.e_expense.network.HttpsPostRequest;

public class LoginActivity extends AppCompatActivity {
    protected final String USER_EMAIL = "";
    String result = "";
    String login;
    String pass;
    private final String USER_SESSION = "USER_SESSION";
    SharedPreferences userPrefs;
    protected String API_URL;
    protected HttpsPostRequest getRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userPrefs = getSharedPreferences(this.USER_SESSION, MODE_PRIVATE);
        API_URL = "http://api.ibragim.fr/Android.php";
        getRequest = new HttpsPostRequest();
        boolean SessionCheck = UserSession.CheckSession(userPrefs);

        if (SessionCheck){
            Map<String, ?> userS = UserSession.getUserSession(userPrefs);
            String params;
            params = "login=true&mail="+userS.get("USER_EMAIL")+"&password="+userS.get("USER_PASS");
            try {
                result = getRequest.execute(API_URL, params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            try {
                JSONObject user = new JSONObject(result);
                login = user.getString("email");
                pass = user.getString("password");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (userS.get("USER_EMAIL").equals(login) && userS.get("USER_PASS").equals(pass)){
                Intent reco = new Intent(getApplicationContext(), MainActivity.class);
                reco.putExtra(USER_EMAIL, login);
                startActivity(reco);
                finish();
            }

        }

        final EditText emailField = findViewById(R.id.loginField);
        final EditText passField = findViewById(R.id.passField);
        Button btn = findViewById(R.id.btnconnect);
        final Switch rememberSw = findViewById(R.id.rememberSwitch);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







                try {
                    result = getRequest.execute(API_URL,"login=true&mail="+emailField.getText().toString() +"&password="+ passField.getText().toString()).get(); // Exécution du Thread pour connexion.
                    //Log.v("RETOUR ", result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject user = new JSONObject(result);
                    login = user.getString("email").toString();
                    pass = user.getString("password").toString();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (emailField.getText().toString().equals(login) && passField.getText().toString().equals(pass)){

                    if (rememberSw.isChecked()){
                        UserSession.setSharedPrefs(userPrefs, login, pass);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(USER_EMAIL, login);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(USER_EMAIL, login);
                        startActivity(intent);
                        finish();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Email ou Mot de passe incorrects", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}