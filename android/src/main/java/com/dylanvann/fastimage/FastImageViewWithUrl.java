package com.dylanvann.fastimage;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.model.GlideUrl;

class FastImageViewWithUrl extends ImageView {

    public FastImageSource glideUrl;

    public String defaultSource;

    public String resizeMode;


    public FastImageViewWithUrl(Context context) {
        super(context);
    }
}
