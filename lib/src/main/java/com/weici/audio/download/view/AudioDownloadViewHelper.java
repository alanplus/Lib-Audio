package com.weici.audio.download.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.weici.audio.R;
import com.weici.audio.download.IDownloadConfig;
import com.weici.audio.download.MediaPlayerManager;


/**
 * Created by Mouse on 2019/1/29.
 */
public class AudioDownloadViewHelper implements IAudioDownloadView {

    private Context context;
    private View view;
    private IDownloadConfig iDownloadConfig;

    private AudioDownloadView.OnMediaPlayStateListener onMediaPlayStateListener;

    @Override
    public void init(Context context, View view, AttributeSet attributeSet) {
        this.context = context;
        this.view = view;
        if (attributeSet == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AudioViewStyle);
        int type = typedArray.getInt(R.styleable.AudioViewStyle_audio_type, 0);
        view.setTag(R.id.tag_audio_type, type);
        int resourceId = typedArray.getResourceId(R.styleable.AudioViewStyle_default_backgroud, 0);
        view.setTag(R.id.tag_audio_default_bg, resourceId);
        if (resourceId != 0) view.setBackgroundResource(resourceId);
        int animRes = typedArray.getResourceId(R.styleable.AudioViewStyle_anim, 0);
        view.setTag(R.id.tag_audio_anim, animRes);
        int animType = typedArray.getInt(R.styleable.AudioViewStyle_anim_type, 0);
        view.setTag(R.id.tag_anim_type, animType);
        int loadDrawable = typedArray.getResourceId(R.styleable.AudioViewStyle_loading_drawable, 0);
        if (loadDrawable != 0) {
            view.setTag(R.id.tag_loading, loadDrawable);
        }
        int animBg = typedArray.getResourceId(R.styleable.AudioViewStyle_anim_backgroud, 0);
        typedArray.recycle();
        if (animBg != 0) view.setTag(R.id.tag_audio_anim_bg, animBg);
        view.setOnClickListener(v -> {
            if (view instanceof AudioDownloadView) {
                ((AudioDownloadView) view).onClickEvent();
            } else {
                onClickEvent();
            }
        });
    }

    @Override
    public void onClickEvent() {
        MediaPlayerManager.getInstance(context).play(view, iDownloadConfig, onMediaPlayStateListener);
    }

    @Override
    public void setAudioDownloadConfig(IDownloadConfig iDownloadConfig) {
        this.iDownloadConfig = iDownloadConfig;
    }

    public void setOnMediaPlayStateListener(AudioDownloadView.OnMediaPlayStateListener onMediaPlayStateListener) {
        this.onMediaPlayStateListener = onMediaPlayStateListener;
    }
}
