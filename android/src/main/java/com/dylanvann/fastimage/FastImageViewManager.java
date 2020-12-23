package com.dylanvann.fastimage;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

import static com.dylanvann.fastimage.FastImageRequestListener.REACT_ON_ERROR_EVENT;
import static com.dylanvann.fastimage.FastImageRequestListener.REACT_ON_LOAD_END_EVENT;
import static com.dylanvann.fastimage.FastImageRequestListener.REACT_ON_LOAD_EVENT;


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
                        .listener(new FastImageRequestListener())
                        .into(view);
            }else{
                requestManager
                        .load(view.glideUrl.getSourceForLoad())
                        .apply(requestOptions)
                        .listener(new FastImageRequestListener())
                        .into(view);
            }

        } else{

            if(view.dontTransform){
                requestManager
                        .load(view.glideUrl.getSourceForLoad())
                        .dontTransform()
                        .thumbnail(requestManager.load(view.defaultSource))
                        .apply(requestOptions)
                        .listener(new FastImageRequestListener())
                        .into(view);
            }else{
                requestManager
                        .load(view.glideUrl.getSourceForLoad())
                        .thumbnail(requestManager.load(view.defaultSource))
                        .apply(requestOptions)
                        .listener(new FastImageRequestListener())
                        .into(view);
            }
        }

        super.onAfterUpdateTransaction(view);
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(REACT_ON_LOAD_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_EVENT))
                .put(REACT_ON_ERROR_EVENT, MapBuilder.of("registrationName", REACT_ON_ERROR_EVENT))
                .put(REACT_ON_LOAD_END_EVENT, MapBuilder.of("registrationName", REACT_ON_LOAD_END_EVENT))
                .build();
    }


    @ReactProp(name = "defaultSource")
    public void setDefaultSource(FastImageViewWithUrl view, @Nullable ReadableMap defaultSource) {
        try {
            view.defaultSource = defaultSource.getString("uri");
        } catch (Exception e) {
        }
    }

    @ReactProp(name = "dontTransform")
    public void dontTransform(FastImageViewWithUrl view, @Nullable Boolean dontTransform) {
        try {
            view.dontTransform = dontTransform;
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
