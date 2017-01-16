package com.example.mediareader.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mediareader.models.MediaFileInfo;
import com.example.mediareader.views.MediaListElement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 14/01/2017.
 */

public class ImageAdapter extends BaseAdapter {

    private static final int THUMBNAIL_SIZE = 64;

    private List<MediaFileInfo> data = new ArrayList<>();

    public ImageAdapter(List<MediaFileInfo> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MediaFileInfo mediaFileInfo = data.get(position);

        MediaListElement mediaListElement = new MediaListElement(parent.getContext());
        mediaListElement.getViewHolder().getTitle().setText(mediaFileInfo.getFileName());

        if (null == mediaListElement.getViewHolder().getThumbnail().getDrawable()) {
            new AsyncTask<MediaListElement.ViewHolder, Void, Bitmap>() {
                private MediaListElement.ViewHolder viewHolder;

                @Override
                protected Bitmap doInBackground(MediaListElement.ViewHolder... params) {
                    try {
                        viewHolder = params[0];
                        FileInputStream inputStream = new FileInputStream(mediaFileInfo.getFilePath());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmap = Bitmap.createScaledBitmap(bitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
                        return bitmap;
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    viewHolder.getThumbnail().setImageBitmap(bitmap);
                }
            }.execute(mediaListElement.getViewHolder());
        }

        return mediaListElement;
    }
}
