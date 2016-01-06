package com.pascalhow.travellog.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pascalhow.travellog.R;
import com.pascalhow.travellog.UserSettings;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pascalh on 4/12/2015.
 */
public class SettingsFragment extends Fragment {
    @Bind(R.id.textUserSettings)
    TextView textView_userSetting;

    @Bind(R.id.settingsButton)
    Button buttonUserSettings;

    private static final int RESULT_SETTINGS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this, view);

        //  Show current user settings
        showUserSettings();

        buttonUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UserSettings.class);

                //  Launch the settings page
                startActivityForResult(i, RESULT_SETTINGS);
            }
        });
        return view;
    }

    /**
     * This method shows the current user settings
     */
    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String savedUserSettings = "\n Username: " + sharedPrefs.getString("prefUsername", "NULL");
        savedUserSettings += "\n Email address:" + sharedPrefs.getString("prefEmailAddress", "NULL");
        savedUserSettings += "\n Send report:" + sharedPrefs.getBoolean("prefSendReport", false);
        savedUserSettings += "\n Sync Frequency: " + sharedPrefs.getString("prefSyncFrequency", "NULL");

        textView_userSetting.setText(savedUserSettings);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                //  Update user settings
                showUserSettings();
                break;
        }
    }
}
