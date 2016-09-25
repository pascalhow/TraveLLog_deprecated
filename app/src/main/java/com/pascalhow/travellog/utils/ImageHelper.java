/**
 * Created by pascalh on 01/12/2015.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pascalhow.travellog.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageHelper {

    /**
     * This method decodes the sampled bitmap from a file
     *
     * @return  Bitmap decoded from file
     */
    public static Bitmap decodeSampledBitmapFromFile(String path, int sampleWidth, int sampleHeight) {

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > sampleHeight) {
            inSampleSize = Math.round((float) height / (float) sampleHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > sampleWidth) {
            inSampleSize = Math.round((float) width / (float) sampleWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * Calls the Picasso library
     * @param context       Context
     * @param imageView     The ImageView to display
     * @param imagePath     The image file path
     */
    public static void setImage(Context context, ImageView imageView, String imagePath) {

        //  Create the file from the image path
        File filepath = new File(imagePath);

        //  Handle the image with the Picasso library
        Picasso.with(context)
                .load(Uri.fromFile(filepath))
                .resize(150, 150)
                .centerCrop()
                .onlyScaleDown()
                .into(imageView);
    }
}
