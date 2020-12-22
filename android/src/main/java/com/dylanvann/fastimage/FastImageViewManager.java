package com.dylanvann.fastimage;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;

import javax.annotation.Nullable;


class FastImageViewManager extends SimpleViewManager<FastImageViewWithUrl>  {
    private static final String REACT_CLASS = "FastImageView";
    private static RequestManager requestManager = null;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected FastImageViewWithUrl createViewInstance(ThemedReactContext reactContext) {
        if (requestManager == null) {
            requestManager = Glide.with(reactContext.getApplicationContext());
        }
        return new FastImageViewWithUrl(reactContext);
    }

    @ReactProp(name = "source")
    public void setSrc(FastImageViewWithUrl view, @Nullable ReadableMap source) {
        // Get the GlideUrl which contains header info.
        final FastImageSource imageSource = FastImageViewConverter.getImageSource(view.getContext(), source);
        view.glideUrl = imageSource;

    }

    @ReactProp(name = ViewProps.RESIZE_MODE)
    public void setResizeMode(FastImageViewWithUrl view, String resizeMode) {
        view.resizeMode = resizeMode;
    }


    @Override
    protected void onAfterUpdateTransaction(FastImageViewWithUrl view) {
        RequestOptions requestOptions = new RequestOptions();

        FastImageViewWithUrl.ScaleType scaleType = FastImageViewConverter.getScaleType(view.resizeMode);
        view.setScaleType(scaleType);


        if (TextUtils.isEmpty(view.defaultSource)) {

            if(view.dontTransform){
                requestManager
                        .load(view.glideUrl.getSourceForLoad())
                        .dontTransform()
                        .apply(requestOptions)
                        .into(view);
            }else{
                requestManager
                        .load(view.glideUrl.getSourceForLoad())
                        .apply(requestOptions)
                        .into(view);
            }

        } else{

            if(view.dontTransform){
                requestManager
                        .load(view.glideUrl.getSourceForLoad())
                        .dontTransform()
                        .thumbnail(requestManager.load(view.defaultSource))
                        .apply(requestOptions)
                        .into(view);
            }else{
                requestManager
                        .load(view.glideUrl.getSourceForLoad())
                        .thumbnail(requestManager.load(view.defaultSource))
                        .apply(requestOptions)
                        .into(view);
            }
        }

        super.onAfterUpdateTransaction(view);
    }


    @ReactProp(name = "defaultSource")
    public void setDefaultSource(FastImageViewWithUrl view, @Nullable ReadableMap defaultSource) {
        try {
            view.defaultSource = defaultSource.getString("uri");
        } catch (Exception e) {
        }
    }

    @ReactProp(name = "dontTransform")
    public void dontTransform(FastImageViewWithUrl view, @Nullable ReadableMap map) {
        try {
            view.dontTransform = map.getBoolean("dontTransform");
        } catch (Exception e) {
        }
    }


    @Override
    public void onDropViewInstance(FastImageViewWithUrl view) {
        // This will cancel existing requests.
        requestManager.clear(view);
        super.onDropViewInstance(view);
    }


}
