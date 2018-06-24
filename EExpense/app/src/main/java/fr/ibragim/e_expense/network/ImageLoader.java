package fr.ibragim.e_expense.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by ibragim.abubakarov on 24/06/2018.
 */

public class ImageLoader extends AsyncTask<String, Void, Bitmap>{
    private ImageView addPicture;

    public ImageLoader(ImageView addPicture){
        this.addPicture = addPicture;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urldisplay = params[0];
        Bitmap justif = null;
        URL url;
        InputStream in;

        try {
            url = new URL(urldisplay);
            in = url.openStream();
            justif = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return justif;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        addPicture.setImageBitmap(bitmap);
        addPicture.setRotation(90);
    }
}
