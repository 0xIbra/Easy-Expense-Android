package fr.ibragim.e_expense.Widgets;

import android.graphics.Bitmap;

/**
 * Created by ibragim.abubakarov on 24/06/2018.
 */

public class ImageCompressor {

    public static Bitmap compress(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
