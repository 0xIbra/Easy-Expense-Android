package fr.ibragim.e_expense.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ibragim.abubakarov on 22/04/2018.
 */

public class HttpsGetRequest extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private HttpURLConnection conn;

    @Override
    protected String doInBackground(String... params) {
        try {
            URL apiUrl = new URL(params[0]);
            conn = (HttpURLConnection) apiUrl.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return getDataFromServer();
    }


    protected String getDataFromServer(){
        String result = null;
        String inputLine;

        try {
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.connect();
            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            bufferedReader.close();
            reader.close();
            result = stringBuilder.toString();
            conn.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
