package com.pascalhow.travellog.utils;

<<<<<<< HEAD
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
=======
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
>>>>>>> Reviewed permission request code
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pascalh on 18/01/2016.
 */
public class PermissionHelper {

    private static boolean allPermissionsGranted = false;
    private static Map<String, Integer> perms = new HashMap<>();

    /**
     * This method adds the permission string to a permission list if they are not currently granted
     *
     * @param context           The context calling this method
     * @param permissionsList   List of outstanding permissions to be requested
     * @param permission        The actual permission string required
     */
    public static void addPermission(Context context, List<String> permissionsList, String permission) {
        //  Add required permission to hash map for later verification
        perms.put(permission, PackageManager.PERMISSION_GRANTED);

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            //  If permission has not been granted yet, then add it to list of permissions that are still needed
            permissionsList.add(permission);
        }
    }

<<<<<<< HEAD
    /**
     * Performs a check whether all permissions for the app have been granted
     * To be used in onRequestPermissionsResult(...)
     * @param permissions   List of permissions requested by the app
     * @param grantResults  Results of the permissions based on user
     * @return  true if all permissions granted
     */
=======
>>>>>>> Reviewed permission request code
    public static boolean AllPermissionsGranted(String[] permissions, int[] grantResults)
    {
        // Fill with permission results
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);

            //  Checking for the following:
            //  (perms.get(Manifest.permission.CAMERA == PackageManager.PERMISSION_GRANTED
            //      && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            //      && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

            if(grantResults[i] == PackageManager.PERMISSION_DENIED) {

                return allPermissionsGranted = false;
            }
            else {
                allPermissionsGranted = true;
            }
        }

        return allPermissionsGranted;
    }
}
