package com.pascalhow.travellog.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pascalhow.travellog.Profile;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.utils.JsonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pascalh on 4/12/2015.
 */
public class AboutMeFragment extends Fragment {
    @Bind(R.id.textView_about_me)
    TextView textView_aboutMe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutme, container, false);

        ButterKnife.bind(this, view);

        String result = JsonUtil.toJSon(LoadProfile());

        //  Update the about me textView
        textView_aboutMe.setText(result);

        return view;
    }

    /**
     * This method loads the profile in Json format
     */
    public Profile LoadProfile()
    {
        Profile profile = new Profile();

        profile.setName("Pascal");
        profile.setSurname("How");

        Profile.Address address = profile.new Address();
        address.setCity("London");
        address.setStreet("Sesame street");
        address.setPostcode("NW1 8UP");

        profile.setAddress(address);

        Profile.ContactDetails contactDetails = profile.new ContactDetails();
        contactDetails.setPhoneNumber("999");
        contactDetails.setEmail("yourEmail@hotmail.com");

        profile.setContactDetails(contactDetails);

        return profile;
    }
}
