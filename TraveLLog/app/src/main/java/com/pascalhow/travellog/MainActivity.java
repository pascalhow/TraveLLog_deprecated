package com.pascalhow.travellog;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
<<<<<<< HEAD
<<<<<<< HEAD
import android.net.Uri;
=======
>>>>>>> Reviewed permission request code
=======
>>>>>>> 1138ad541955af42049c1080e84248b02c7363c6
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pascalhow.travellog.fragments.AboutFragment;
import com.pascalhow.travellog.fragments.CameraFragment;
import com.pascalhow.travellog.fragments.ImportFragment;
import com.pascalhow.travellog.fragments.MyTripsFragment;
import com.pascalhow.travellog.utils.PermissionHelper;

<<<<<<< HEAD
<<<<<<< HEAD
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
=======
=======
>>>>>>> 1138ad541955af42049c1080e84248b02c7363c6
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
>>>>>>> Reviewed permission request code


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_CAMERA = "camera";
    private static final String FRAGMENT_MYTRIPS = "mytrips";
    private static final String FRAGMENT_IMPORT = "import";
    private static final String FRAGMENT_ABOUT = "about";

<<<<<<< HEAD
<<<<<<< HEAD
    private final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1;
    private String ImageFolderName = "TraveLLog";
    private String pictureFilePath;

//    public FloatingActionButton fab;
=======
=======
>>>>>>> 1138ad541955af42049c1080e84248b02c7363c6
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;
>>>>>>> Reviewed permission request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //  Load the myTrips fragment for starters
        loadFragment(new MyTripsFragment(), FRAGMENT_MYTRIPS);
    }

    public void loadFragment(Fragment fragment, String tag) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (tag) {

//              MyTrips fragment is the first fragment to be displayed so we don't addToBackStack()
            case FRAGMENT_MYTRIPS:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_content, fragment, tag)
                                //.commitAllowingStateLoss();
                        .commit();
                break;

            case FRAGMENT_CAMERA:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_content, fragment, tag)
//                        .addToBackStack(null)
//                        .commitAllowingStateLoss();
                        .commit();
                break;
            case FRAGMENT_IMPORT:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_content, fragment, tag)
//                        .addToBackStack(null)
//                        .commitAllowingStateLoss();
                        .commit();
                break;

            case FRAGMENT_ABOUT:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_content, fragment, tag)
                        .commit();
                break;

            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            //  TODO: Consider not loading the page if the fragment is currently loaded
            case R.id.action_camera:
                loadCamera();
//                loadFragment(new CameraFragment(), FRAGMENT_CAMERA);
                return true;
            case R.id.action_import:
                loadFragment(new ImportFragment(), FRAGMENT_IMPORT);
                return true;
            case R.id.action_mytrips:
                loadFragment(new MyTripsFragment(), FRAGMENT_MYTRIPS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCamera()
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //  Create timestamped file for captured images
        File file = CreateDatedFile();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        //  Start the camera activity
        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * This method creates the dated file
     *
     * @return File with date formatted name
     */
    public File CreateDatedFile() {
        //  Get the device current date to make each picture name unique
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_ssmmHH");
        String formattedDate = dateFormat.format(calendar.getTime());

        //  Save captured image on SD card
        File file = new File(Environment.getExternalStorageDirectory() + File.separator
                + ImageFolderName + File.separator + "IMG_" + formattedDate + ".jpg");

        //  Save the file path with current date and time
        pictureFilePath = file.getPath();

        return file;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            //  These are the items in the navigation menu
            case R.id.nav_info:
                loadFragment(new AboutFragment(), FRAGMENT_ABOUT);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                String s = data.getStringExtra("ImageCaption");
//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

//                fab.setVisibility(View.VISIBLE);

                //  Load MyTripsFragment again to update the image description in the CardView items
                loadFragment(new MyTripsFragment(), FRAGMENT_MYTRIPS);
            }
        }
<<<<<<< HEAD
<<<<<<< HEAD

        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                loadFragment(new CameraFragment(), FRAGMENT_CAMERA);
            }
        }
=======
>>>>>>> Reviewed permission request code
=======
>>>>>>> 1138ad541955af42049c1080e84248b02c7363c6
    }
}
