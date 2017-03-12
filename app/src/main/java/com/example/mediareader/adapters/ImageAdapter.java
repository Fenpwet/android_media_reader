package com.example.mediareader.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.mediareader.models.MediaFileInfo;
import com.example.mediareader.views.MediaListElement;

import java.util.List;

/**
 * Adapter to load image MediaFIleInfo into views
 * Created by David on 14/01/2017.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<MediaFileInfo> mediaFileInfos;

    public ImageAdapter(Context context, List<MediaFileInfo> data) {
        this.context = context;
        this.mediaFileInfos = data;
    }

    @Override
    public int getCount() {
        return mediaFileInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaFileInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This is the method that add elements to the View
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MediaFileInfo mediaFileInfo = this.mediaFileInfos.get(position);

        // Add title
        MediaListElement mediaListElement = new MediaListElement(context);
        mediaListElement.getViewHolder().getTitle().setText(mediaFileInfo.getFileName());

        // Add thumbnail
        Glide.with(context)
                .load(mediaFileInfo.getFilePath())
                .into(mediaListElement.getViewHolder().getThumbnail());

        return mediaListElement;
    }
}
