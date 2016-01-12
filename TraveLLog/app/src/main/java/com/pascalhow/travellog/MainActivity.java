package com.pascalhow.travellog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pascalhow.travellog.fragments.AboutMeFragment;
import com.pascalhow.travellog.fragments.CameraFragment;
import com.pascalhow.travellog.fragments.ImportFragment;
import com.pascalhow.travellog.fragments.MyTripsFragment;
import com.pascalhow.travellog.fragments.SettingsFragment;
import com.pascalhow.travellog.fragments.ShareFragment;
import com.pascalhow.travellog.fragments.SlideShowFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FloatingActionButton fab;

    private static final String FRAGMENT_CAMERA = "camera";
    private static final String FRAGMENT_MYTRIPS = "mytrips";
    private static final String FRAGMENT_IMPORT = "import";
    private static final String FRAGMENT_SLIDESHOW = "slideshow";
    private static final String FRAGMENT_SETTINGS = "settings";
    private static final String FRAGMENT_SHARE = "share";
    private static final String FRAGMENT_ABOUT = "about";

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //  Load the camera fragment for starters
        loadFragment(new CameraFragment(), FRAGMENT_CAMERA);
    }


//    /**
//     * This method creates the email intent
//     * @param recipient email address of the recipient
//     * @param subject is the email subject
//     * @param message is the body of the email
//     */
//    private void sendEmail(String[] recipient, String subject, String message)
//    {
//        // Use an intent to launch an email app.
//        // Send the order summary in the email body.
//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
//
//        //  Parse all the email information
//        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            //  Start the email activity
//            startActivity(intent);
//        }
//    }

    private void loadFragment(Fragment fragment, String tag) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, fragment, tag)
//                .addToBackStack(null)
                .commitAllowingStateLoss();
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

        switch(id)
        {
            case R.id.action_about:
                loadFragment(new AboutMeFragment(), FRAGMENT_ABOUT);
                return true;

            case R.id.action_settings:
                //  Load standard android settings page
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                return true;

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
            case R.id.nav_camera:
                loadFragment(new CameraFragment(), FRAGMENT_CAMERA);
                break;
            case R.id.nav_mytrips:
                loadFragment(new MyTripsFragment(), FRAGMENT_MYTRIPS);
                break;
            case R.id.nav_import:
                loadFragment(new ImportFragment(), FRAGMENT_IMPORT);
                break;
            case R.id.nav_slideshow:
                loadFragment(new SlideShowFragment(), FRAGMENT_SLIDESHOW);
                break;
            case R.id.nav_settings:
                loadFragment(new SettingsFragment(), FRAGMENT_SETTINGS);
                break;

            //  These are under the Communicate group
            case R.id.nav_share:
                loadFragment(new ShareFragment(), FRAGMENT_SHARE);
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
}
