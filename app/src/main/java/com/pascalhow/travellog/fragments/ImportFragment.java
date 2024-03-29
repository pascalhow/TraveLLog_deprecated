package com.pascalhow.travellog.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pascalhow.travellog.EndlessRecyclerOnScrollListener;
import com.pascalhow.travellog.ImportAdapter;
import com.pascalhow.travellog.MainActivity;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.model.MyTripsItem;
import com.pascalhow.travellog.model.MyTripsItemBuilder;
import com.pascalhow.travellog.utils.PermissionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImportFragment extends Fragment {

    @Bind(R.id.import_list)
    RecyclerView recyclerView;

    @Bind(R.id.imageView_import_placeHolder)
    ImageView imageView_import;

    private int imagesPerRow = 3;
    private int count = 0;
    private int loadLimit = 20;

    private ImportAdapter mAdapter;
    private String ImageFolderName = "TraveLLog";
    private GridLayoutManager mLayoutManager;

    private ArrayList<MyTripsItem> imagesList = new ArrayList<>();
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;
    private File folderPath;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setTitle("Import");

        getAppPermissions();

//        folderPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Facebook");
        folderPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Camera");
//        folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + "Whatsapp/Media/Whatsapp Images");
//        folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + ImageFolderName);


        //  New layout manager and display content in reverse order
        mLayoutManager = new GridLayoutManager(getActivity(), imagesPerRow);

        mAdapter = new ImportAdapter(getContext(), imagesList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);

        if(loadImages())
        {
            imageView_import.setVisibility(View.INVISIBLE);
        }

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {

                //  Using AsyncTask gave better results than Handler
                new LongOperation().execute("");
            }
        });

        return view;
    }

    /**
     * This method loads images in batches to avoid hogging the UI too much
     * @return
     */
    private boolean loadImages() {

        boolean imageLoaded = false;

        if (folderPath.exists())
        {
            try {
                for (int i = count; i < loadLimit; i++) {
                    if (i < folderPath.listFiles().length) {
                        //  Load the images in the folder into the images list
                        imagesList.add(
                                new MyTripsItemBuilder()
                                        .setUrl(folderPath.listFiles()[i].getAbsolutePath())
                                        .build()
                        );

                        mAdapter.notifyItemInserted(imagesList.size());

                    }
                    imageLoaded = true;
                    count++;
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getActivity(), "External storage access needed", Toast.LENGTH_SHORT).show();
            }

            //  Go to next set of images
            loadLimit = count + 10;
        }

        mAdapter.notifyDataSetChanged();

        return imageLoaded;
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
                if(!PermissionHelper.AllPermissionsGranted(permissions, grantResults)) {

                    // Not all permissions have been granted
                    Toast.makeText(getActivity(), "Some permissions are denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //  Put into a try catch because it clashes with some operations in RecyclerView
            try {

                loadImages();
            }
            catch (Exception e)
            {}
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
//            TextView txt = (TextView) findViewById(R.id.output);
//            txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
