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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pascalhow.travellog.MainActivity;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.utils.ImageHelper;
import com.pascalhow.travellog.utils.PermissionHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CameraFragment extends Fragment {

    private final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1;
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;
    private final String JPG_exifTag_imageDescription = "UserComment";

    @Bind(R.id.imageView_camera)
    ImageView imageView_cameraPicture;

    @Bind(R.id.textView_caption_label)
    TextView textView_captionLabel;

    @Bind(R.id.textView_imageDescription)
    TextView textView_imageDescription;

    @Bind(R.id.editText_imageDescription)
    EditText editText_imageDescription;

    @Bind(R.id.camera_button_edit_text)
    Button button_editText;

    @Bind(R.id.camera_button_saveImageDescription)
    Button button_saveImageDescription;

    @Bind(R.id.camera_button_cancelImageDescription)
    Button button_cancelImageDescription;
    MainActivity mainActivity;
    private String pictureFilePath;
    private int bitmapWidth = 1000;
    private int bitmapHeight = 700;
    private String ImageFolderName = "TraveLLog";
    private static final String FRAGMENT_MYTRIPS = "mytrips";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setTitle("Camera");
        //  Get the user to give necessary access to the camera
        getAppPermissions();

        //  Create a directory for the camera images
        createImageFolderDirectory(ImageFolderName);

        loadButtons();

//        loadCamera();

        return view;
    }


    /**
     * This method loads the CameraFragment save, cancel and edit buttons
     */
    private void loadButtons() {

        button_saveImageDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard();

                //  Stop showing Edit Text Field and Save button
                editText_imageDescription.setVisibility(View.GONE);
                button_saveImageDescription.setVisibility(View.GONE);
                button_cancelImageDescription.setVisibility(View.GONE);

                //  Make Edit Text Button and Text Image Description visible
                button_editText.setVisibility(View.VISIBLE);
                textView_imageDescription.setVisibility(View.VISIBLE);

                //  Save the text from Edit Text field onto the Text View
                textView_imageDescription.setText(editText_imageDescription.getText().toString());

                //  Save the image description
                saveImageDescription(pictureFilePath, ImageCaptionType.IMAGE_DESCRIPTION, editText_imageDescription.getText().toString());
            }
        });

        button_cancelImageDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard();

                //  Stop showing Edit Text Field and Save button
                editText_imageDescription.setVisibility(View.GONE);
                button_saveImageDescription.setVisibility(View.GONE);
                button_cancelImageDescription.setVisibility(View.GONE);

                //  Make Edit Text Button and Text Image Description visible
                button_editText.setVisibility(View.VISIBLE);
                textView_imageDescription.setVisibility(View.VISIBLE);
            }
        });

        button_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  The Edit Text Field becomes visible
                editText_imageDescription.setVisibility(View.VISIBLE);

                //  Copy content of existing TextView field to the Edit Text field
                editText_imageDescription.setText(textView_imageDescription.getText().toString());

                ShowKeyboard();

                //  Save and Cancel button becomes visible
                button_saveImageDescription.setVisibility(View.VISIBLE);
                button_cancelImageDescription.setVisibility(View.VISIBLE);

                //  Make Edit Text Button and Text Image Description gone
                button_editText.setVisibility(View.GONE);
                textView_imageDescription.setVisibility(View.GONE);
            }
        });
    }

    /**
     * This method hides the soft input keyboard
     */
    private void HideKeyboard() {
        if (getActivity().getWindow().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * This method shows the soft keyboard
     */
    private void ShowKeyboard() {
        //  Set cursor to the end of the Edit Text
        editText_imageDescription.setSelection(editText_imageDescription.getText().length());
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * This method creates the folder to save the camera images
     *
     * @param folderName
     */
    public void createImageFolderDirectory(String folderName) {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);

        //  If folder does not exist then create the directory
        if (!folder.exists()) {
            folder.mkdir();
        }
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

    private void saveImageDescription(String imagePath, ImageCaptionType captionType, String description) {

        File file = new File(imagePath);

        switch (captionType) {
            case IMAGE_DESCRIPTION: {
                try {
                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    exif.setAttribute(JPG_exifTag_imageDescription, description);
                    exif.saveAttributes();

                    //  Set the text for image description
                    textView_imageDescription.setText(exif.getAttribute(JPG_exifTag_imageDescription));

                } catch (IOException e) {
//                    Log.i("CameraFragment.java", "CameraFragment Exception: Image description could not be saved");
                }
                break;
            }
            case LOCATION: {
                break;
            }

            case PEOPLE: {
                break;
            }
            default: {
//                Log.i("CameraFragment.java", captionType.toString() + " - Could not be saved");
                break;
            }
        }
    }


    /**
     * This method is called when the camera activity returns. This is where the image bitmap is set
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //  Here we get the file path from the global variable instead
                //  because the image date and time could have changed by the time we get there
                File file = new File(pictureFilePath);

                //  We need to get the bitmap from the actual directory otherwise "data" will return a small thumbnail
                Bitmap bitmap = ImageHelper.decodeSampledBitmapFromFile(file.getAbsolutePath(), bitmapWidth, bitmapHeight);

                //  Update the ImageView with the bitmap
                imageView_cameraPicture.setImageBitmap(bitmap);

                //  Make editable fields visible
                textView_captionLabel.setVisibility(View.VISIBLE);          //  Caption title

                editText_imageDescription.setVisibility(View.VISIBLE);      //  Edit Text view for image description
                editText_imageDescription.setText("");                      //  Clear the edit text field

                textView_imageDescription.setVisibility(View.GONE);
                textView_imageDescription.setText("");                      //  Clear the text description

                button_editText.setVisibility(View.GONE);
                button_saveImageDescription.setVisibility(View.VISIBLE);    //  Save button
                button_cancelImageDescription.setVisibility(View.VISIBLE);  //  Cancel button

                //  Give focus to the edit text view
                editText_imageDescription.requestFocus();

            } else {
                Toast.makeText(getActivity(), "Picture was not taken!", Toast.LENGTH_SHORT).show();

                //  Load the MyTrips fragment
                mainActivity.loadFragment(new MyTripsFragment(), FRAGMENT_MYTRIPS);
            }
        }
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
            //  Ask for user permission for each ungranted permission needed by the camera
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        //  If app already has all necessary permissions then carry on
//        mainActivity.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                //  This line below launches the google camera
////                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.GoogleCamera");
//
//                //  Create timestamped file for captured images
//                File file = CreateDatedFile();
//
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//
//                //  Start the camera activity
//                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//            }
//        });
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

                if(PermissionHelper.AllPermissionsGranted(permissions, grantResults))
                {
                    // All Permissions Granted so set the camera button onClickListener
//                    mainActivity.fab.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                            //  This line below launches the google camera
////                            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.GoogleCamera");
//
//                            //  Create timestamped file for captured images
//                            File file = CreateDatedFile();
//
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//
//                            //  Start the camera activity
//                            startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                        }
//                    });
                }
                else
                {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Some permissions are denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
    }

    public enum ImageCaptionType {
        IMAGE_DESCRIPTION, LOCATION, PEOPLE
    }
}
