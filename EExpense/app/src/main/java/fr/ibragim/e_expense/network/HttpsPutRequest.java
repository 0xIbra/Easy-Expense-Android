package fr.ibragim.e_expense.network;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ibragim.abubakarov on 18/06/2018.
 */

public class HttpsPutRequest extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "PUT";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private HttpsURLConnection conn;


    @Override
    protected String doInBackground(String... strings) {

        String targetUrl = strings[0];
        JSONObject json = null;
        try {
            json = new JSONObject(strings[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URL url;
        conn = null;
        try{
            url = new URL(targetUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = reader.readLine()) != null){
                response.append(line);
                response.append('\r');
            }
            reader.close();

            conn.disconnect();
            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
