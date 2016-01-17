package com.pascalhow.travellog;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.pascalhow.travellog.fragments.CameraFragment;
import com.pascalhow.travellog.fragments.ImportFragment;
import com.pascalhow.travellog.fragments.MyTripsFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_CAMERA = "camera";
    private static final String FRAGMENT_MYTRIPS = "mytrips";
    private static final String FRAGMENT_IMPORT = "import";
    public FloatingActionButton fab;

    private final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1;
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
        getAppPermissions();

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

    private void loadFragment(Fragment fragment, String tag) {

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            //  These are under the Single group
            case R.id.nav_mytrips:
                loadFragment(new MyTripsFragment(), FRAGMENT_MYTRIPS);
                break;
            case R.id.nav_camera:
                loadFragment(new CameraFragment(), FRAGMENT_CAMERA);
                break;
            case R.id.nav_import:
                loadFragment(new ImportFragment(), FRAGMENT_IMPORT);
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
    }

    /**
     * This method requests for the permissions needed for the Camera functionality to work
     */
    @TargetApi(23)
    private void getAppPermissions() {
        final List<String> permissionsList = new ArrayList<>();

        //  Add the user permissions
        addPermission(permissionsList, Manifest.permission.CAMERA);
        addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
        addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionsList.size() > 0) {

            try {
                //  Ask for user permission for each ungranted permission needed by the camera

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            catch(Exception e){}
            return;
        }

        fab.setVisibility(View.VISIBLE);

    }

    /**
     * This method adds the permission string to a permission list if they are not currently granted
     *
     * @param permissionsList
     * @param permission
     */
    private void addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
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
                Map<String, Integer> perms = new HashMap<>();

                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                // Check if all permissions have been granted
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    fab.setVisibility(View.VISIBLE);

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some permissions are denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
