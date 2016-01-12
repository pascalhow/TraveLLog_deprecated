package com.pascalhow.travellog.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pascalhow.travellog.EndlessRecyclerOnScrollListener;
import com.pascalhow.travellog.ImportAdapter;
import com.pascalhow.travellog.MainActivity;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.model.MyTripsItem;
import com.pascalhow.travellog.model.MyTripsItemBuilder;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImportFragment extends Fragment {

    @Bind(R.id.import_list)
    RecyclerView recyclerView;

    @Bind(R.id.imageView_import_placeHolder)
    ImageView imageView_import;

    private int imagesPerRow = 3;
    private int count = 0;
    private int loadLimit = 12;

    private ImportAdapter mAdapter;
    private String ImageFolderName = "TraveLLog";
    private GridLayoutManager mLayoutManager;

    private ArrayList<MyTripsItem> imagesList = new ArrayList<>();

    private File folderPath;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        mainActivity.fab.setVisibility(View.GONE);

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
                Toast.makeText(getActivity(), "Some permissions are denied", Toast.LENGTH_SHORT).show();
            }

            //  Go to next set of images
            loadLimit = count + 10;
        }

        mAdapter.notifyDataSetChanged();

        return imageLoaded;
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
