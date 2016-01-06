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
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageHelper {
    public static void setImage(Context context, ImageView imageView, String imagePath) {

        //  Create the file from the image path
        File filepath = new File(imagePath);

        //  Handle the image with the Picasso library
        Picasso.with(context)
                .load(Uri.fromFile(filepath))
                .resize(300, 300)
                .centerCrop()
                .into(imageView);
    }
}
