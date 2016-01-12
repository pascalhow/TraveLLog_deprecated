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

import android.os.Build;
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

import com.pascalhow.travellog.MainActivity;
import com.pascalhow.travellog.MyTripsAdapter;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.model.MyTripsItem;
import com.pascalhow.travellog.model.MyTripsItemBuilder;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyTripsFragment extends Fragment {
    @Bind(R.id.myTrips_list)
    RecyclerView recyclerView;

    @Bind(R.id.textView_myTrips)
    TextView textView_myTrips;

    @Bind(R.id.imageView_myTrips_placeHolder)
    ImageView imageView_myTrips;

    private MyTripsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private String ImageFolderName = "TraveLLog";
    private ArrayList<MyTripsItem> imagesList = new ArrayList<>();
    private File folderPath;
    private int count = 0;
    private int loadLimit = 12;

    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytrips, container, false);

        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        mainActivity.fab.setVisibility(View.GONE);

        folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + ImageFolderName);

        //  New layout manager and display content in reverse order
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mAdapter = new MyTripsAdapter(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);

        //  Remove placeholder image and text if images from folder are loaded successfully
        if(loadImages())
        {
            textView_myTrips.setVisibility(View.INVISIBLE);
            imageView_myTrips.setVisibility(View.INVISIBLE);
        }

//        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
//
//            @Override
//            public void onLoadMore(int current_page) {
//
//                //  Using AsyncTask gave better results than Handler
//                new MyTripsLongOperation().execute("");
//            }
//        });

        return view;
    }

    private boolean loadImages() {
        boolean imageLoaded = false;

        ArrayList<MyTripsItem> images = new ArrayList<>();

        File folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + ImageFolderName);

        if (folderPath.exists()) {
            for (int i = 0; i < folderPath.listFiles().length; i++)
            {
                //  Load all the images in the folder into the images list
                images.add(
                        new MyTripsItemBuilder()
                                .setTitle("Image " + i)
                                .setUrl(folderPath.listFiles()[i].getAbsolutePath())
                                .build()
                );

                imageLoaded = true;
            }
        }
        mAdapter.setItemList(images);

        return imageLoaded;

//        boolean imageLoaded = false;
//
//        if (folderPath.exists())
//        {
//            for (int i = count; i <loadLimit; i++)
//            {
//                if(i < folderPath.listFiles().length) {
//                    //  Load the images in the folder into the images list
//                    imagesList.add(
//                            new MyTripsItemBuilder()
//                                    .setTitle("Image " + i)
//                                    .setUrl(folderPath.listFiles()[i].getAbsolutePath())
//                                    .build()
//                    );
//
//                    mAdapter.notifyItemInserted(imagesList.size());
//
//                }
//                imageLoaded = true;
//                count++;
//            }
//
//            //  Go to next set of images
//            loadLimit = count + 10;
//        }
//
//        mAdapter.notifyDataSetChanged();
//
//        return imageLoaded;
    }

//    private class MyTripsLongOperation extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            //  Put into a try catch because it clashes with some operations in RecyclerView
//            try {
//
//                loadImages();
//            }
//            catch (Exception e)
//            {}
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
////            TextView txt = (TextView) findViewById(R.id.output);
////            txt.setText("Executed"); // txt.setText(result);
//            // might want to change "executed" for the returned string passed
//            // into onPostExecute() but that is upto you
//        }
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected void onProgressUpdate(Void... values) {}
//    }
}
