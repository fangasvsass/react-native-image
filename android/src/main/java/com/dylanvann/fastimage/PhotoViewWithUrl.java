package com.dylanvann.fastimage;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by okct on 21/03/2018.
 */

class PhotoViewWithUrl extends PhotoView {
    public String defaultSource = "";
    public boolean circle;
    public GlideUrl glideUrl;
    public Priority priority;
    public String resizeMode;

    public PhotoViewWithUrl(Context context) {
        super(context);
        this.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                PhotoViewWithUrl.this.onReceiveNativeEvent();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (PhotoViewWithUrl.this.getScale() > PhotoView.DEFAULT_MIN_SCALE) {
                    PhotoViewWithUrl.this.setScale(PhotoView.DEFAULT_MIN_SCALE, e.getX(), e.getY(), true);
                } else {
                    PhotoViewWithUrl.this.setScale(PhotoView.DEFAULT_MAX_SCALE, e.getX(), e.getY(), true);
                }
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
    }



    public void onReceiveNativeEvent() {
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "onPhotoTapListener",
                null);
    }
}

