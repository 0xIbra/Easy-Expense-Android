package fr.ibragim.e_expense.network;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ibrah on 18/03/2018.
 */

public class HttpsPostData extends AsyncTask<String, Void, Void>{
    public static final String REQUEST_METHOD = "POST";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private HttpURLConnection conn;


    @Override
    protected Void doInBackground(String... strings) {
        String targeturl = strings[0];
        String parametres = strings[1];
        URL url;
        conn = null;
        try{
            url = new URL(targeturl);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
