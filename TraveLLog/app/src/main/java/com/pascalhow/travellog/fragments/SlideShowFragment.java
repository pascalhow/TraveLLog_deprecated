package com.pascalhow.travellog.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pascalhow.travellog.R;
import com.pascalhow.travellog.model.BitmapItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



/**
 * Created by pascalh on 4/12/2015.
 */
public class SlideShowFragment extends Fragment {
    private static int SLIDESHOW_INTERVAL = 1000;
    private final Handler handler = new Handler();
    public int currentImageIndex = 0;
    private BitmapItem bitmapItem;

    @Bind(R.id.imageView_slideShow)
    ImageView imageView_slideShow;
    @Bind(R.id.textView_slideShow)
    TextView textView_slideShow;

    List<File> m_fileList = new ArrayList<>();
    private int bitmapWidth = 700;
    private int bitmapHeight = 1000;
    private String imageFolderName = "TraveLLog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        ButterKnife.bind(this, view);

        //  Get the list of images from the specified folder directory
        File folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + imageFolderName);
        m_fileList = GetFileList(folderPath);

        if (m_fileList.size() > 0)
        {
            //  Make display text invisible
            textView_slideShow.setText("");

            //  We have some images to display in slideshow!
            final Runnable runnable = new Runnable()
            {
                public void run() {
                    //  We need to get the bitmap from the actual directory otherwise "data" will return a small thumbnail
                    bitmapItem = new BitmapItem(m_fileList.get(currentImageIndex).getAbsolutePath(), bitmapWidth, bitmapHeight);
                    Bitmap bitmap = bitmapItem.getBitmap();

                    //  Update the ImageView with the bitmap
                    imageView_slideShow.setImageBitmap(bitmap);

                    //  Move to next image index in the list
                    currentImageIndex++;

                    //  We reached the end of the list so restart
                    if (currentImageIndex == m_fileList.size()) {
                        currentImageIndex = 0;
                    }

                    handler.postDelayed(this, SLIDESHOW_INTERVAL);
                }
            };
            handler.postDelayed(runnable, SLIDESHOW_INTERVAL);
        } else
        {
            textView_slideShow.setText("Sorry no images to display in JuniorTest folder");
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        //  Stop the slideshow handler when the fragment loses focus
        handler.removeMessages(0);
    }

    /**
     * This method gets the list of image paths from the JuniorTest folder
     * @return  The list of image paths from the JuniorTest folder
     */
    private List<File> GetFileList(File folderPath) {
        List<File> fileList = new ArrayList<>();

        if (folderPath.exists()) {
            for (int i = 0; i < folderPath.listFiles().length; i++) {
                //  Add all file path into the fileList
                fileList.add(folderPath.listFiles()[i]);
            }
        }

        return fileList;
    }
}
