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

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.pascalhow.travellog.CameraActivity;
import com.pascalhow.travellog.MainActivity;
import com.pascalhow.travellog.MyTripsAdapter;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.model.MyTripsItem;
import com.pascalhow.travellog.model.MyTripsItemBuilder;
import com.pascalhow.travellog.utils.PermissionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyTripsFragment extends Fragment {
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;
    @Bind(R.id.myTrips_list)
    RecyclerView recyclerView;
    @Bind(R.id.textView_myTrips)
    TextView textView_myTrips;
    @Bind(R.id.imageView_myTrips_placeHolder)
    ImageView imageView_myTrips;
    MainActivity mainActivity;
    private MyTripsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String ImageFolderName = "TraveLLog";
    private ArrayList<MyTripsItem> imagesList = new ArrayList<>();
    private File folderPath;
    private int count = 0;
    private int loadLimit = 12;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytrips, container, false);

        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setTitle("MyTrips");

        getAppPermissions();

        //  If app already has all necessary permissions then carry on
//        mainActivity.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), CameraActivity.class);
//                startActivity(intent);
//            }
//        });

        folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + ImageFolderName);

        //  New layout manager and display content in reverse order
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mAdapter = new MyTripsAdapter(getContext());

//        mAdapter = new MyTripsAdapter(getContext(), imagesList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);

        //  Remove placeholder image and text if images from folder are loaded successfully
        if (loadImages()) {
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

            //  Do a try for now as user may not have allowed for permission to access external storage
            try {
                for (int i = 0; i < folderPath.listFiles().length; i++) {
                    //  Load all the images in the folder into the images list
                    images.add(
                            new MyTripsItemBuilder()
                                    .setTitle("Image " + (i + 1))
                                    .setUrl(folderPath.listFiles()[i].getAbsolutePath())
                                    .build()
                    );

                    imageLoaded = true;
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), "External storage access needed", Toast.LENGTH_SHORT).show();
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

    /**
     * This method requests for the permissions needed for the Camera functionality to work
     */
    @TargetApi(23)
    private void getAppPermissions() {
        final List<String> permissionsList = new ArrayList<>();

        //  Add permission to permission list if they are not currently granted
        PermissionHelper.addPermission(getActivity(), permissionsList, Manifest.permission.CAMERA);
        PermissionHelper.addPermission(getActivity(), permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
        PermissionHelper.addPermission(getActivity(), permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionsList.size() > 0) {

            //  Ask for user permission for each ungranted permission needed
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        //  TODO: DO something once we get user permissions

    }

    /**
     * Callback with the request from requestPermission(...)
     *
     * @param requestCode  The code referring to the permission requested
     * @param permissions  The list of permissions requested
     * @param grantResults The result of the requested permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (PermissionHelper.AllPermissionsGranted(permissions, grantResults)) {
                    //  TODO: Find a way to disable the camera

//                    mainActivity.fab.setVisibility(View.VISIBLE);
                }
                else {
                    // Not all permissions have been granted
                    Toast.makeText(getActivity(), "Some permissions are denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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

    @Override
    public void onResume() {
        super.onResume();

        //  Remove placeholder image and text if images from folder are loaded successfully
        if (loadImages()) {
            textView_myTrips.setVisibility(View.INVISIBLE);
            imageView_myTrips.setVisibility(View.INVISIBLE);
        } else {
            textView_myTrips.setVisibility(View.VISIBLE);
            imageView_myTrips.setVisibility(View.VISIBLE);
        }
    }
}
