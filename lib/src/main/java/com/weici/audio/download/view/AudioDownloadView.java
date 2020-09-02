package com.weici.audio.download.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.weici.audio.SimpleMediaStateListener;
import com.weici.audio.download.IDownloadConfig;
import com.weici.audio.download.IMediaStateChangeListener;
import com.weici.audio.download.MediaPlayerManager;

/**
 * Created by Mouse on 2019/1/29.
 */
public class AudioDownloadView extends android.support.v7.widget.AppCompatTextView implements IAudioDownloadView {

    protected AudioDownloadViewHelper audioDownloadViewHelper;


    protected IDownloadConfig iDownloadConfig;

    protected boolean isPause;

    public AudioDownloadView(Context context) {
        this(context, null);
    }

    public AudioDownloadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(getContext(), this, attrs);
    }

    @Override
    public void init(Context context, View view, AttributeSet attributeSet) {
        audioDownloadViewHelper = new AudioDownloadViewHelper();
        audioDownloadViewHelper.init(context, view, attributeSet);
        isPause = false;
    }

    @Override
    public void onClickEvent() {
        if (isPause) {
            int state = MediaPlayerManager.getInstance(getContext()).getXmMediaPlayer().getState();
            switch (state) {
                case IMediaStateChangeListener.STATE_IDLE:
                case IMediaStateChangeListener.STATE_LOADING:
                case IMediaStateChangeListener.STATE_LOADFINISH:
                case IMediaStateChangeListener.STATE_PREPARE:
                case IMediaStateChangeListener.STATE_STOP:
                case IMediaStateChangeListener.STATE_DESTROY:
                case IMediaStateChangeListener.STATE_ERROR:
                case IMediaStateChangeListener.STATE_COMPLETE:
                    audioDownloadViewHelper.onClickEvent();
                    break;
                case IMediaStateChangeListener.STATE_START:
                    MediaPlayerManager.getInstance(getContext()).getXmMediaPlayer().pause();
                    break;
                case IMediaStateChangeListener.STATE_PAUSE:
                    MediaPlayerManager.getInstance(getContext()).getXmMediaPlayer().start();
                    break;
            }
        } else {
            audioDownloadViewHelper.onClickEvent();
        }


    }

    @Override
    public void setAudioDownloadConfig(IDownloadConfig iDownloadConfig) {
        audioDownloadViewHelper.setAudioDownloadConfig(iDownloadConfig);
        this.iDownloadConfig = iDownloadConfig;
    }

    public void setOnMediaPlayStateListener(AudioDownloadView.OnMediaPlayStateListener onMediaPlayStateListener) {
        audioDownloadViewHelper.setOnMediaPlayStateListener(onMediaPlayStateListener);
    }

    public void autoPlay(int num) {
        MediaPlayerManager.getInstance(getContext()).play(this, iDownloadConfig, new SimpleMediaStateListener() {

            @Override
            public void onComplete() {
                if (num == 1) {
                    return;
                }
                autoPlay(num - 1);
            }
        });
    }

    public void setPauseOrStopWhenPressed(boolean isPause) {
        this.isPause = isPause;
    }

    public interface OnMediaPlayStateListener {
        void onLoadingStateListener();

        void onLoadFinishStateListener();

        void onPrepareStateListener();

        void onStartStateListener();

        void onPauseStateListener();

        void onStopStateListener();

        void onDestroyStateListener();

        void onIdleStateListener();

        void onComplete();

        void onErrorStateListener(@IMediaStateChangeListener.error int error);
    }

    public int getAudioDuration() {
        return MediaPlayerManager.getInstance(getContext()).getXmMediaPlayer().getTotalTime();
    }

    public int getAudioPlayPosition() {
        return MediaPlayerManager.getInstance(getContext()).getXmMediaPlayer().getProgress();
    }
}
