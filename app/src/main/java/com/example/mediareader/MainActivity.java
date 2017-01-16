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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<MediaFileInfo> allImageInfo = parseAllImages();

        ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setAdapter(new ImageAdapter(allImageInfo));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private List<MediaFileInfo> parseAllImages() {
        List<MediaFileInfo> resultMediaFileInfoList = new ArrayList<>();
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                    null, null);

            int size = cursor.getCount();

            if (size != 0) {
                int thumbId = 0;
                while (cursor.moveToNext()) {
                    int fileColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    String path = cursor.getString(fileColumnIndex);
                    String fileName = path.substring(path.lastIndexOf("/") + 1);

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
