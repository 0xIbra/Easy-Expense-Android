package fr.ibragim.e_expense.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ibragim.abubakarov on 15/03/2018.
 */

public class HttpsPostRequest extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "POST";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private HttpURLConnection conn;


    @Override
    protected String doInBackground(String... strings) {

        String targetUrl = strings[0];
        String parametres = strings[1];
        URL url;
        HttpURLConnection conn = null;
        try{
            url = new URL(targetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);

            conn.setUseCaches(true);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(parametres);
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
            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
