package com.example.mediareader;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mediareader.adapters.ImageAdapter;
import com.example.mediareader.models.MediaFileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mainListView;
    ImageAdapter imageAdapter;
    List<MediaFileInfo> allImageInfo;

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
        allImageInfo = getImagesInfo();

        // Add adapter to list View and register context menu
        imageAdapter = new ImageAdapter(this, allImageInfo);
        mainListView.setAdapter(imageAdapter);
        registerForContextMenu(mainListView);

        // Add on click listener to list view
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaFileInfo fileInfo = allImageInfo.get(position);
                openFile(fileInfo);
            }
        });
    }

    /**
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get necessary info
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;
        MediaFileInfo mediaFileInfo = allImageInfo.get(index);

        // Context Menu Behaviour
        switch (item.getItemId()) {
            case R.id.context_open:
                // Open file
                openFile(mediaFileInfo);
                break;
            case R.id.context_delete:
                // Create and show confirm dialog for deletion
                Dialog dialog = buildConfirmDeleteDialog(mediaFileInfo, index);
                dialog.show();
                break;
            case R.id.context_properties:
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    /**
     * Builds a confirm delete dialog
     *
     * @param filename
     * @param index
     * @return
     */
    private Dialog buildConfirmDeleteDialog(final MediaFileInfo mediaFileInfo, final int index) {
        String fileName = mediaFileInfo.getFileName();
        String message = getResources().getString(R.string.delete_dialog_text, fileName);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.delete_dialog_title);
        alertBuilder.setMessage(message);
        alertBuilder.setNegativeButton(R.string.delete_dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        alertBuilder.setPositiveButton(R.string.delete_dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = getApplicationContext();
                // Delete media and remove view
                File fileToDelete = new File(mediaFileInfo.getFilePath());
                fileToDelete.delete();
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileToDelete)));

                // Check file was deleted
                if (fileToDelete.exists()) {
                    // Send toast to inform user
                    String toastMessage = getResources().getString(R.string.delete_toast_error, mediaFileInfo.getFileName());
                    Toast toast = Toast.makeText(context, toastMessage, 3);
                    toast.show();
                } else {
                    // Send toast to inform user
                    String toastMessage = getResources().getString(R.string.delete_toast_ok, mediaFileInfo.getFileName());
                    Toast toast = Toast.makeText(context, toastMessage, 3);
                    toast.show();

                    // Remove element from view
                    imageAdapter.removeView(index);
                }
            }
        });
        return alertBuilder.create();
    }

    /**
     * @param fileInfo
     */
    private void openFile(MediaFileInfo fileInfo) {
        // Get file name and extension
        String fileName = fileInfo.getFileName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // Get file and mime type
        File file = new File(fileInfo.getFilePath());
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

        // Open file
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        startActivityForResult(intent, 10);
    }

    /**
     * Get device's image library into a list of MediaFileInfo
     *
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
            //TODO Handle exception
            e.printStackTrace();
        }
        return resultMediaFileInfoList;
    }

    public void moreActionsMenu(View view) {
        openContextMenu(view);
    }

}
