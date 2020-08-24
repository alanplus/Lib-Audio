package com.weici.audio;

import android.content.Context;
import android.util.AttributeSet;

import com.weici.audio.download.IMediaStateChangeListener;
import com.weici.audio.download.MediaPlayerManager;
import com.weici.audio.download.view.AudioDownloadView;

/**
 * @author Alan
 * 时 间：2020-08-24
 * 简 述：<功能简述>
 */
public class AudioListView extends AudioDownloadView {

    protected OnMediaPlayStateListener onMediaPlayStateListener;

    public AudioListView(Context context) {
        super(context);
    }

    public AudioListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClickEvent() {
        MediaPlayerManager instance = MediaPlayerManager.getInstance(getContext());
        XmMediaPlayer xmMediaPlayer = instance.getXmMediaPlayer();

        if (xmMediaPlayer.isPlaying()) {
            xmMediaPlayer.pause();
            return;
        }
        int state = xmMediaPlayer.getState();
        if (state == IMediaStateChangeListener.STATE_PAUSE) {
            xmMediaPlayer.start();
            return;
        }

        instance.play(this, iDownloadConfig, onMediaPlayStateListener);

    }

    @Override
    public void setOnMediaPlayStateListener(OnMediaPlayStateListener onMediaPlayStateListener) {
        this.onMediaPlayStateListener = onMediaPlayStateListener;
    }
}
