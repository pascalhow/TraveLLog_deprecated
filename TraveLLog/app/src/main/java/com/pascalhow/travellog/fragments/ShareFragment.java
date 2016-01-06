package com.pascalhow.travellog.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.pascalhow.travellog.R;

import com.pascalhow.travellog.model.BitmapItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pascalh on 4/12/2015.
 */
public class ShareFragment extends ListFragment {
    @Bind(R.id.textView_share)
    TextView textView_share;

    private String imageFolderName = "TraveLLog";

    private List<BitmapItem> m_bitmapItemList = new ArrayList<>();

    private int bitmapWidth = 1000;
    private int bitmapHeight = 700;

    private ShareGalleryAdapter m_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        ButterKnife.bind(this, view);

        //  Get the list of images from the specified folder directory
        File folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + imageFolderName);

        m_bitmapItemList = getBitmapItem(folderPath);

        if(m_bitmapItemList.size() > 0)
        {
            //  We have some images to choose from
            m_adapter = new ShareGalleryAdapter();

            setListAdapter(m_adapter);
        }
        else
        {
            textView_share.setText("Sorry no images to select in JuniorTest folder");
        }

        return view;
    }

    /**
     * This method returns a list of BitmapItem.
     * BitmapItem objects contain the image file information and the corresponding bitmap
     * @param folderPath    Folder to look for bitmaps
     * @return  List of BitmapItem
     */
    public List<BitmapItem> getBitmapItem(File folderPath) {
        List<BitmapItem> bitmapItemList = new ArrayList<>();

        if (folderPath.exists()) {
            for (int i = 0; i < folderPath.listFiles().length; i++) {

                BitmapItem bitmapItem = new BitmapItem(folderPath.listFiles()[i].getAbsolutePath(), bitmapWidth, bitmapHeight);

                bitmapItemList.add(bitmapItem);

            }
        }
        return bitmapItemList;
    }

    /**
     * This method shares text or images via the user's sharing apps
     *
     * @param type    Define the type of what is being shared: Text or Images
     * @param body    Description of what is being shared
     * @param subject Subject of what is being shared
     */
    private void Share(ShareType type, String body, String subject, Uri uri) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        switch (type) {
            case TEXT:
                sharingIntent.setType("text/plain");
                break;
            case IMAGE:
                sharingIntent.setType("image/jpeg");
                break;
            case TEXT_AND_IMAGE:
                sharingIntent.setType("*/*");
        }

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);  //  Share subject
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);        //  Share text body
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);                       //  Share image

        //  Start share activity
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Toast.makeText(getActivity(), "" + m_bitmapItemList.get(position), Toast.LENGTH_SHORT).show();

        String shareBody = "Here is the share content body";
        String ShareSubject = "Subject Title";

        //  Share the files and texts
        Share(ShareType.TEXT_AND_IMAGE, shareBody, ShareSubject, Uri.fromFile(m_bitmapItemList.get(position).getFile()));
    }

    public enum ShareType {
        TEXT, IMAGE, TEXT_AND_IMAGE
    }

    public class ShareGalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return m_bitmapItemList.size();
        }

        @Override
        public BitmapItem getItem(int index) {
            return m_bitmapItemList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup parent) {
            if (view == null) {
                //  Inflate our ListView with gallery_adapter_item
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.gallery_adapter_item, null);
            }

            Bitmap bitmap = m_bitmapItemList.get(index).getBitmap();


            //  TODO: This is test code
            File file = m_bitmapItemList.get(index).getFile();
            //  TODO

            if (bitmap != null) {

                //  set the bitmap with the image from JuniorTest folder
                ImageView imageViewGalleryImage = (ImageView) view.findViewById(R.id.gallery_adapter_item_image);
                imageViewGalleryImage.setImageBitmap(bitmap);

                //  TODO: This is test code
                try {

                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());

                    TextView textView_shareImageDescription = (TextView) view.findViewById(R.id.gallery_adapter_item_image_description);
                    textView_shareImageDescription.setText(exif.getAttribute("Image Description"));
                }
                catch(IOException e)
                {}
                //  TODO
            }

            return view;
        }
    }
}
