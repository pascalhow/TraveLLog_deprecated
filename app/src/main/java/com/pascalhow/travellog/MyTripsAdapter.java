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
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pascalhow.travellog.model.MyTripsItem;
import com.pascalhow.travellog.utils.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MyTripsAdapter extends RecyclerView.Adapter<MyTripsAdapter.ViewHolder> {

    private final String JPG_exifTag_imageDescription = "UserComment";
    private final String tag_selectedImage = "SelectedImage";

    private ArrayList<MyTripsItem> itemList;
    private Context context;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        int itemType;
        CardView cardView;
        TextView title;
        ImageView image;
        TextView imageDescription;

        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);
            this.itemType = ViewType;
            cardView = (CardView) itemView.findViewById(R.id.gallery_cardView);
            title = (TextView) itemView.findViewById(R.id.gallery_adapter_item_title);
            image = (ImageView) itemView.findViewById(R.id.gallery_adapter_item_image);
            imageDescription = (TextView) itemView.findViewById(R.id.gallery_adapter_item_image_description);
        }
    }

    public MyTripsAdapter(Context context) {
        this.itemList = new ArrayList<>();
        this.context = context;
    }

    public void setItemList(ArrayList<MyTripsItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public MyTripsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytrips_adapter_item, parent, false);
        return new ViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(final MyTripsAdapter.ViewHolder holder, final int position) {
        final MyTripsItem item = itemList.get(position);
        holder.title.setText(item.getTitle());
        ImageHelper.setImage(context, holder.image, item.getImagePath());

        //  We do this because context does not implement startActivityForResult(...)
        this.activity = (Activity) context;

        try {
            ExifInterface exif = new ExifInterface(item.getImagePath());
            holder.imageDescription.setText(exif.getAttribute(JPG_exifTag_imageDescription));
        } catch (IOException ex) {
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(item.getImagePath());

                Intent intent = new Intent(context, SelectedImageActivity.class);
                intent.putExtra(tag_selectedImage, file);
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}


//public class MyTripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private final String JPG_exifTag_imageDescription = "UserComment";
//    private final String tag_selectedImage = "SelectedImageActivity";
//
//    private ArrayList<MyTripsItem> itemList;
//    private Context context;
//    private Activity activity;
//
//    public static class ImageViewHolder extends RecyclerView.ViewHolder
//    {
//        CardView cardView;
//        TextView title;
//        ImageView image;
//        TextView imageDescription;
//
//        public ImageViewHolder(View v) {
//            super(v);
//            cardView = (CardView)itemView.findViewById(R.id.gallery_cardView);
//            title = (TextView) itemView.findViewById(R.id.gallery_adapter_item_title);
//            image = (ImageView) itemView.findViewById(R.id.gallery_adapter_item_image);
//            imageDescription = (TextView) itemView.findViewById(R.id.gallery_adapter_item_image_description);
//        }
//    }
//
//    public MyTripsAdapter(Context context, ArrayList<MyTripsItem> itemList) {
//        this.itemList = itemList;
//        this.context = context;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder vh;
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytrips_adapter_item, parent, false);
//
//        vh = new ImageViewHolder(v);
//
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        final MyTripsItem item = itemList.get(position);
//        ((ImageViewHolder) holder).title.setText(item.getTitle());
//        ImageHelper.setImage(context, ((ImageViewHolder) holder).image, item.getImagePath());
//
//        //  We do this because context does not implement startActivityForResult(...)
//        this.activity = (Activity) context;
//
//        try
//        {
//            ExifInterface exif = new ExifInterface(item.getImagePath());
//            ((ImageViewHolder) holder).imageDescription.setText(exif.getAttribute(JPG_exifTag_imageDescription));
//        }
//        catch (IOException ex) {
//        }
//
//        ((ImageViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                File file = new File(item.getImagePath());
//
//                Intent intent= new Intent(context, SelectedImageActivity.class);
//                intent.putExtra(tag_selectedImage, file);
//                activity.startActivityForResult(intent, 1);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return itemList.size();
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//}