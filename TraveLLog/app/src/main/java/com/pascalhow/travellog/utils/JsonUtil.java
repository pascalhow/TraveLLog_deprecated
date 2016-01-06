package com.pascalhow.travellog.utils;

import com.pascalhow.travellog.Profile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pascalh on 13/12/2015.
 */
public class JsonUtil {
    public static String toJSon(Profile profile) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();

            // Set the name and surname
            jsonObj.put("name", profile.getName());
            jsonObj.put("surname", profile.getSurname());

            JSONObject jsonAdd = new JSONObject();

            //  Set the address
            jsonAdd.put("street", profile.getAddress().getStreet());
            jsonAdd.put("city", profile.getAddress().getCity());
            jsonAdd.put("postcode", profile.getAddress().getPostCode());

            // We add the object to the main object
            jsonObj.put("address", jsonAdd);

            JSONObject jsonContact = new JSONObject();

            //  Set the contact details
            jsonContact.put("phonenumber", profile.getContactDetails().getPhoneNumber());
            jsonAdd.put("email", profile.getContactDetails().getEmail());

            // We add the object to the main object
            jsonObj.put("contact", jsonContact);

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
