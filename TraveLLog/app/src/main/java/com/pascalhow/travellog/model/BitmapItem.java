package com.pascalhow.travellog.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by pascalh on 10/12/2015.
 * The BitmapDecoder class takes as parameters the file path from which we want to create a bitmap
 * Decodes it according to the requested width and height
 */
public class BitmapItem {
    private String bitmapDescription = "";
    private int width = 0;
    private int height = 0;
    private Bitmap bitmap;
    private File file;

    public BitmapItem(String path, int reqWidth, int reqHeight) {
        this.file = new File(path);
        this.width = reqWidth;
        this.height = reqHeight;
        this.bitmap = decodeSampledBitmapFromFile(this.file.getAbsolutePath());
    }

    /**
     * This method returns the decoded bitmap
     * @return  Decoded bitmap
     */
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    /**
     * This method returns the Image file
     * @return  Image file
     */
    public File getFile() {
        return this.file;
    }

    /**
     * This method decodes the sampled bitmap from a file
     *
     * @return
     */
    private Bitmap decodeSampledBitmapFromFile(String path) {

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

    /**
     * This method add a description to the bitmap
     * @param description
     */
    public void AddBitmapDescription(String description)
    {
        this.bitmapDescription = description;

        //  TODO: Add some text description using metadata/database or some other methods
    }

    /**
     * This method returns the bitmap description
     * @return
     */
    public String GetBitmapDescription()
    {
        return this.bitmapDescription;
    }
}

