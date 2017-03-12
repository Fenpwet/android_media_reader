package com.example.mediareader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mediareader.adapters.ImageAdapter;
import com.example.mediareader.models.MediaFileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mainListView;

    private void init() {
        setContentView(R.layout.activity_main);
        mainListView = (ListView) findViewById(R.id.main_list_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity Layout
        super.onCreate(savedInstanceState);
        init();

        // Get all Image Media File info
        final List<MediaFileInfo> allImageInfo = getImagesInfo();

        // Add adapter to list View
        mainListView.setAdapter(new ImageAdapter(this, allImageInfo));

        // Add on click listener to list view
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaFileInfo fileInfo = allImageInfo.get(position);
                String fileName = fileInfo.getFileName();

                File file = new File(fileInfo.getFilePath());
                String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName.substring(fileName.lastIndexOf(".") + 1));

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), mime);
                startActivityForResult(intent, 10);
            }
        });
    }

    /**
     * Get device's image library into a list of MediaFileInfo
     * @return
     */
    private List<MediaFileInfo> getImagesInfo() {
        List<MediaFileInfo> resultMediaFileInfoList = new ArrayList<>();
        try {
            // Get a cursor of all image data
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                    null, null);

            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    int fileColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    String path = cursor.getString(fileColumnIndex);
                    String fileName = path.substring(path.lastIndexOf("/") + 1);

                    // Create Media File Info and add it to the list
                    MediaFileInfo mediaFileInfo = new MediaFileInfo();
                    mediaFileInfo.setFilePath(path);
                    mediaFileInfo.setFileName(fileName);
                    resultMediaFileInfoList.add(mediaFileInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMediaFileInfoList;
    }
}
