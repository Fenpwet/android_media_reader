package com.example.mediareader.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediareader.R;

/**
 * Created by David on 14/01/2017.
 * View of a media list element
 */
public class MediaListElement extends LinearLayout {

    private ViewHolder viewHolder;

    private void init() {
        inflate(getContext(), R.layout.list_element, this);
        viewHolder = new ViewHolder();
        viewHolder.thumbnail = (ImageView) findViewById(R.id.list_element_thumbnail);
        viewHolder.title = (TextView) findViewById(R.id.list_element_title);
        viewHolder.text1 = (TextView) findViewById(R.id.list_element_text1);
        this.setTag(viewHolder);
    }

    public MediaListElement(Context context) {
        super(context);
        init();
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public static class ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView text1;

        public ImageView getThumbnail() {
            return thumbnail;
        }

        public TextView getTitle() {
            return title;
        }
    }

}
