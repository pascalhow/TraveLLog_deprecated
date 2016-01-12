package com.pascalhow.travellog;

import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pascalhow.travellog.utils.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectedImage extends AppCompatActivity {

    private final String JPG_exifTag_imageDescription = "UserComment";
    private final String tag_selectedImage = "SelectedImage";

    private int width = 700;
    private int height = 700;

    private ImageView imageView_selectedImage;
    private TextView textView_selectedImage;

    //  TODO: Consider creating a galleryItem object
    private File mLoadedFile;
    private String selectedImageDescription;

    private EditText editText_selectedImage;
    private Button selectedImage_button_saveImageDescription;
    private Button selectedImage_button_cancelImageDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_selectedImage);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mLoadedFile = LoadImageContents();

        Bitmap bitmap = ImageHelper.decodeSampledBitmapFromFile(mLoadedFile.getAbsolutePath(), width, height);

        imageView_selectedImage = (ImageView) findViewById(R.id.imageView_selectedImage);
        imageView_selectedImage.setImageBitmap(bitmap);

        textView_selectedImage = (TextView) findViewById(R.id.textView_selectedImage);
        editText_selectedImage = (EditText) findViewById(R.id.editText_selectedImage);
        selectedImage_button_saveImageDescription = (Button) findViewById(R.id.selectedImage_button_saveImageDescription);
        selectedImage_button_cancelImageDescription = (Button) findViewById(R.id.selectedImage_button_cancelImageDescription);

        LoadButtons();

        try {
            ExifInterface exif = new ExifInterface(mLoadedFile.getAbsolutePath());

            //  Retrieve the Selected Image description
            selectedImageDescription = exif.getAttribute(JPG_exifTag_imageDescription);
            textView_selectedImage.setText(selectedImageDescription);
        } catch (IOException ex) {
        }
    }

    /**
     * Load File extras from MyTripsAdapter.java
     *
     * @return
     */
    private File LoadImageContents() {
        Bundle bundle = getIntent().getExtras();

        return (File) bundle.get(tag_selectedImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_selected_item, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit:

                //  The Edit Text Field becomes visible
                editText_selectedImage.setVisibility(View.VISIBLE);

                //  Copy content of existing TextView field to the Edit Text field
                editText_selectedImage.setText(textView_selectedImage.getText().toString());

                editText_selectedImage.requestFocus();

                ShowKeyboard();

                //  Save and Cancel button becomes visible
                selectedImage_button_saveImageDescription.setVisibility(View.VISIBLE);
                selectedImage_button_cancelImageDescription.setVisibility(View.VISIBLE);

                //  Make Edit Text Button and Text Image Description gone
                textView_selectedImage.setVisibility(View.GONE);
                return true;

            case R.id.action_share:
                String shareBody = selectedImageDescription;
                String shareSubject = "My Trip";

                ShareMyTrip(shareBody, shareSubject, Uri.fromFile(mLoadedFile));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method hides the soft input keyboard
     */
    private void HideKeyboard() {
        if (getWindow().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * This method shows the soft keyboard
     */
    private void ShowKeyboard() {
        //  Set cursor to the end of the Edit Text
        editText_selectedImage.setSelection(editText_selectedImage.getText().length());
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * This method loads the buttons in the SelectedImage Activity
     */
    private void LoadButtons() {
        selectedImage_button_saveImageDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard();

                //  Stop showing Edit Text Field and Save button
                editText_selectedImage.setVisibility(View.GONE);
                selectedImage_button_saveImageDescription.setVisibility(View.GONE);
                selectedImage_button_cancelImageDescription.setVisibility(View.GONE);

                //  Make Text Image Description visible
                textView_selectedImage.setVisibility(View.VISIBLE);

                //  Save the text from Edit Text field onto the Text View
                textView_selectedImage.setText(editText_selectedImage.getText().toString());

                //  Save the image description
                saveImageDescription(mLoadedFile.getAbsolutePath(), ImageCaptionType.IMAGE_DESCRIPTION, editText_selectedImage.getText().toString());
            }
        });

        selectedImage_button_cancelImageDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard();

                //  Stop showing Edit Text Field and Save button
                editText_selectedImage.setVisibility(View.GONE);
                selectedImage_button_saveImageDescription.setVisibility(View.GONE);
                selectedImage_button_cancelImageDescription.setVisibility(View.GONE);

                //  Make Text Image Description visible
                textView_selectedImage.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * This method saves the image description based on the caption type
     * It stores it at a jpeg metadata location specified by the reference tag
     *
     * @param imagePath   Image file path
     * @param captionType Type of info being saved to jpeg metadata
     * @param description The actual user input image description
     */
    private void saveImageDescription(String imagePath, ImageCaptionType captionType, String description) {

        File file = new File(imagePath);

        switch (captionType) {
            case IMAGE_DESCRIPTION: {
                try {
                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    exif.setAttribute(JPG_exifTag_imageDescription, description);
                    exif.saveAttributes();

                    //  TODO Test code
                    selectedImageDescription = exif.getAttribute(JPG_exifTag_imageDescription);

                    //  Set the text for image description
                    textView_selectedImage.setText(exif.getAttribute(JPG_exifTag_imageDescription));

                } catch (IOException e) {
                    Log.i("SelectedImage.java", "SelectedImage Exception: Image description could not be saved");
                }
                break;
            }
            case LOCATION: {
                //  TODO: Insert location tag and method
                break;
            }

            case PEOPLE: {
                //  TODO: Can we tag people?
                break;
            }
            default: {
                Log.i("SelectedImage.java", captionType.toString() + " - Could not be saved");
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        //  This bit is not necessary since onActivityResult(...) in MainActivity
        //  MainActivity intercepts this regardless and loads the MyTripsFragment again
        //  TODO: Keep it for now as it can be handy
        intent.putExtra("ImageCaption", selectedImageDescription);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
        finish();
    }

    /**
     * This method creates the sharing intent based on the sharing type
     *
     * @param type    Sharing type
     * @param body    Message body
     * @param subject Message subject
     * @param uri     Message Uri (Image) if any
     * @return An intent with all the corresponding extras
     */
    private Intent SharingIntent(ShareType type, String body, String subject, Uri uri) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        switch (type) {
            case TEXT:
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);  //  Share subject
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);        //  Share text body
                break;

            case IMAGE:
                sharingIntent.setType("image/jpeg");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);                       //  Share image
                break;

            case TEXT_AND_IMAGE:
                sharingIntent.setType("*/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);  //  Share subject
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);        //  Share text body
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);                       //  Share image
                break;

            case MESSAGE_RFC822:
                sharingIntent.setType("message/rfc822");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);  //  Share subject
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);        //  Share text body
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);                       //  Share image
        }

        return sharingIntent;
    }


    /**
     * This method handles all the sharing intents and packages them before sending
     * depending on the apps available on the user's device
     *
     * @param messageBody    The message body
     * @param messageSubject The message subject
     * @param ImageFileUri   The file uri
     */
    public void ShareMyTrip(String messageBody, String messageSubject, Uri ImageFileUri) {

        Intent emailIntent = SharingIntent(ShareType.MESSAGE_RFC822, messageBody, messageSubject, ImageFileUri);

        PackageManager packageManager = getPackageManager();

        Intent sendIntent = SharingIntent(ShareType.TEXT, "", "", null);

        Intent openInChooser = Intent.createChooser(emailIntent, "Share via");

        List<ResolveInfo> resInfoList = packageManager.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();

        for (int i = 0; i < resInfoList.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo resInfo = resInfoList.get(i);
            String packageName = resInfo.activityInfo.packageName;

            if (packageName.contains(ShareApps.ANDROID_EMAIL.toString())) {
                emailIntent.setPackage(packageName);
            } else if (packageName.contains(ShareApps.TWITTER.toString()) || packageName.contains(ShareApps.FACEBOOK.toString())
                    || packageName.contains(ShareApps.MMS.toString()) || packageName.contains(ShareApps.WHATSAPP.toString())) {
                Intent intent = new Intent();

                if (packageName.contains(ShareApps.MMS.toString())) {
                    intent = SharingIntent(ShareType.TEXT, messageBody, messageSubject, null);
                } else if (packageName.contains(ShareApps.TWITTER.toString())) {
                    intent = SharingIntent(ShareType.TEXT, messageBody, messageSubject, null);
                } else if (packageName.contains(ShareApps.FACEBOOK.toString())) {
                    //  Facebook does not allow both Text and Image yet so we only send an image
                    //  We may need to access the Facebook SDK but it does not allow user to choose how to share
                    intent = SharingIntent(ShareType.IMAGE, "", "", Uri.fromFile(mLoadedFile));
                } else if (packageName.contains(ShareApps.WHATSAPP.toString())) {
                    intent = SharingIntent(ShareType.TEXT_AND_IMAGE, messageBody, messageSubject, Uri.fromFile(mLoadedFile));
                }

                //  set the sharing component icon onto the list
                intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));

                intentList.add(new LabeledIntent(intent, packageName, resInfo.loadLabel(packageManager), resInfo.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        //  Attach the extra intents to original openInChooser intent
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);

        //  Start the activity
        startActivity(openInChooser);
    }

    /**
     * Enums for various sharing apps
     * To be used for MyTrip sharing
     */
    public enum ShareApps {
        MMS("mms"),
        ANDROID_EMAIL("android.email"),
        TWITTER("twitter"),
        FACEBOOK("facebook"),
        WHATSAPP("whatsapp");

        private final String text;

        /**
         * @param text
         */
        private ShareApps(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Enums for various jpeg tag type
     */
    public enum ImageCaptionType {
        IMAGE_DESCRIPTION, LOCATION, PEOPLE
    }

    /**
     * Enums for Share types
     */
    public enum ShareType {
        TEXT, IMAGE, TEXT_AND_IMAGE, MESSAGE_RFC822
    }
}
