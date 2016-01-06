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
package com.pascalhow.travellog.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pascalhow.travellog.GalleryAdapter;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.model.GalleryItem;
import com.pascalhow.travellog.model.GalleryItemBuilder;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment {
    @Bind(R.id.gallery_list)
    RecyclerView mList;

    @Bind(R.id.textView_gallery)
    TextView textView_gallery;

    @Bind(R.id.imageView_gallery_placeHolder)
    ImageView imageView_gallery;

    private GalleryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private String ImageFolderName = "TraveLLog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        ButterKnife.bind(this, view);

        //  New layout manager and display content in reverse order
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mAdapter = new GalleryAdapter(getContext());

        mList.setHasFixedSize(true);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(mLayoutManager);

        //  Remove placeholder image and text if images from folder are loaded successfully
        if(loadImages())
        {
            textView_gallery.setVisibility(View.INVISIBLE);
            imageView_gallery.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private boolean loadImages() {
        boolean imageLoaded = false;

        ArrayList<GalleryItem> images = new ArrayList<>();

        File folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + ImageFolderName);

        if (folderPath.exists()) {
            for (int i = 0; i < folderPath.listFiles().length; i++)
            {
                //  Load all the images in the folder into the images list
                images.add(
                        new GalleryItemBuilder()
                                .setTitle("Image " + i)
                                .setUrl(folderPath.listFiles()[i].getAbsolutePath())
                                .build()
                );

                imageLoaded = true;
            }
        }
        mAdapter.setItemList(images);

        return imageLoaded;
    }
}
