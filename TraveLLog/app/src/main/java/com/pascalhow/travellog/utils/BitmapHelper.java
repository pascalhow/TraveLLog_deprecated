package com.pascalhow.travellog.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by pascalh on 31/12/2015.
 */
public class BitmapHelper {
    private int width = 0;
    private int height = 0;

    public BitmapHelper(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * This method decodes the sampled bitmap from a file
     *
     * @return
     */
    public Bitmap decodeSampledBitmapFromFile(String path) {

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > this.height) {
            inSampleSize = Math.round((float) height / (float) this.height);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > this.width) {
            inSampleSize = Math.round((float) width / (float) this.width);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}
