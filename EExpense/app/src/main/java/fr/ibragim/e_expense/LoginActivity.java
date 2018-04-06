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
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fr.ibragim.e_expense.Session.UserSession;
import fr.ibragim.e_expense.network.HttpsPostRequest;

public class LoginActivity extends AppCompatActivity {
    private final String USER_TOKEN = "";
    private final String USER_ID = "USER_ID";

    private String result = "";
    private String authToken = "";
    private String userEmail = "";
    private String AuthHeader;
    private final String USER_SESSION = "USER_SESSION";
    SharedPreferences userPrefs;
    protected String API_URL;
    protected HttpsPostRequest getRequest;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userPrefs = getSharedPreferences(this.USER_SESSION, MODE_PRIVATE);
        API_URL = "https://api.ibragim.fr/Android.php";
        boolean SessionCheck = UserSession.CheckSession(userPrefs);

        if (SessionCheck){
            Map<String, ?> userS = UserSession.getUserSession(userPrefs);
            String params;
            String sharedToken = userS.get("USER_TOKEN").toString();
            String sharedEmail = userS.get("USER_EMAIL").toString();

            byte[] tokenBytes = sharedToken.getBytes();

            byte[] decoded = Base64.decode(tokenBytes, Base64.NO_WRAP);
            String decodedTest2 = new String(decoded);
            userEmail = decodedTest2.substring(0, sharedEmail.length());
            String pass = decodedTest2.substring(sharedEmail.length() + 1, decodedTest2.length());

            params = "AuthToken=true&token="+sharedToken+"&mail="+userEmail+"&password="+pass;
            try {
                getRequest = new HttpsPostRequest();
                result = getRequest.execute(API_URL, params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            try {
                JSONObject user = new JSONObject(result);
                authToken = user.getString("AuthToken");
                userID = user.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (sharedToken.equals(authToken)){
                Intent reco = new Intent(getApplicationContext(), MainActivity.class);
                reco.putExtra(USER_TOKEN, authToken);
                reco.putExtra(USER_ID, userID);
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
                int userid = 0;
                try {
                    getRequest = new HttpsPostRequest();
                    String base = emailField.getText().toString() + ":" + passField.getText().toString();
                    AuthHeader = Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
                    //byte[] test = AuthHeader.getBytes();
                    //String test2 = new String(test);
                    //byte[] decoded = Base64.decode(test2, Base64.NO_WRAP);
                    //String decodedTest2 = new String(decoded);
                    //userEmail = decodedTest2.substring(0, emailField.getText().length());
                    //String pass = decodedTest2.substring(emailField.getText().length() + 1, decodedTest2.length());
                    result = getRequest.execute(API_URL,"AuthToken=true&token="+AuthHeader+"&mail="+emailField.getText().toString() +"&password="+ passField.getText().toString()).get(); // Exécution du Thread pour connexion.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject user = new JSONObject(result);
                    authToken = user.getString("AuthToken").toString();
                    userid = user.getInt("id");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (AuthHeader.equals(authToken)){

                    if (rememberSw.isChecked()){
                        UserSession.setSharedPrefs(userPrefs, authToken, userEmail);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(USER_TOKEN, authToken);
                        intent.putExtra(USER_ID, userid);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(USER_TOKEN, authToken);
                        intent.putExtra(USER_ID, userid);
                        startActivity(intent);
                        finish();
                    }

                }else{
                    Snackbar.make(view, "Email ou Mot de passe incorrects", Snackbar.LENGTH_SHORT).show();
                }


            }
        });

    }
}