/**
 * Created by James Coggan on 01/12/2015.
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
package com.pascalhow.travellog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pascalhow.travellog.model.MyTripsItem;
import com.pascalhow.travellog.utils.ImageHelper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MyTripsItem> itemList;

    public ImportAdapter(Context context, ArrayList<MyTripsItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.import_adapter_item, parent, false);

        vh = new ImageViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MyTripsItem item = itemList.get(position);
        ImageHelper.setImage(context, ((ImageViewHolder) holder).mImageView, item.getImagePath());

        //  Pass item to view holder
        ((ImageViewHolder) holder).setItem(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Context getContext()
    {
        return this.context;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        private MyTripsItem item;
        private Activity activity;
        private final String tag_selectedImage = "SelectedImage";

        public ImageViewHolder(View v) {
            super(v);
            mImageView = (ImageView) itemView.findViewById(R.id.import_adapter_item_image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            String ImageFolderName = "TraveLLog";
             activity = (Activity)view.getContext();

            //  Copy file from Camera folder to TraveLLog folder
            File source = new File(item.getImagePath());

            //  User could have deleted the image after having loaded our ImportFragment
            if(source.exists()) {
                //  We only want jpg and png
                if ((FilenameUtils.getExtension(source.getName()).contains("jpg") || (FilenameUtils.getExtension(source.getName()).contains("png")))) {
                    //  Generate a filename based in our local format based on the file metadata date
                    String datedFileName = generateDatedFileName(item.getImagePath());

                    File dest = new File(Environment.getExternalStorageDirectory() + File.separator + ImageFolderName + File.separator + datedFileName);

                    //  Check if file exists first
                    //  TODO: Otherwise ask user
                    if (!dest.exists()) {

                        try {
                            FileUtils.copyFile(source, dest);
                            Intent intent = new Intent(view.getContext(), SelectedImage.class);
                            intent.putExtra(tag_selectedImage, dest);
                            activity.startActivityForResult(intent, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "Cannot copy file", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "File already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "File not jpg or png", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(view.getContext(), "File does not exist", Toast.LENGTH_SHORT).show();
            }
        }

        public void setItem(MyTripsItem item) {
            this.item = item;
        }

        /**
         * This method reads the metadata and forms a date string for file naming convention
         *
         * @param imagePath The original image file path
         * @return A date string based on the date the original image was taken
         */
        private String generateDatedFileName(String imagePath) {
            String dateString = "";
            String year = "";
            String month = "";
            String day = "";
            String hour = "";
            String minute = "";
            String second = "";

            try {
                //  Get the date time from the image metadata
                ExifInterface exif = new ExifInterface(imagePath);
                dateString = exif.getAttribute("DateTime");

                //  Exif date format - 2013:06:08 18:16:02
                year = dateString.substring(0, 4);
                month = dateString.substring(5, 7);
                day = dateString.substring(8, 10);

                hour = dateString.substring(11, 13);
                minute = dateString.substring(14, 16);
                second = dateString.substring(17, 19);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //  Image filename  format - IMG_yyyyMMdd_ssmmHH.jpg
            dateString = "IMG_" + year + month + day + "_" + second + minute + hour + ".jpg";

            return dateString;
        }
    }

}