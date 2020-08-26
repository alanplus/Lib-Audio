package com.weici.audio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.weici.audio.download.IDownloadConfig;
import com.weici.audio.download.IMediaStateChangeListener;

import java.util.List;

/**
 * @author Alan
 * 时 间：2020-08-24
 * 简 述：<功能简述>
 */
public class AudioListView extends android.support.v7.widget.AppCompatTextView implements IMediaStateChangeListener {


    private XmMediaPlayer xmMediaPlayer;

    @PlayListStatus
    private int status;

    private static final int STATUS_INIT = 0;
    private static final int STATUS_PLAYING = 1;
    private static final int STATUS_PAUSE = 2;

    private List<IDownloadConfig> configList;
    private int position;

    @Override
    public void onLoadingStateListener() {

    }

    @Override
    public void onLoadFinishStateListener() {

    }

    @Override
    public void onPrepareStateListener() {

    }

    @Override
    public void onStartStateListener() {

    }

    @Override
    public void onPauseStateListener() {

    }

    @Override
    public void onStopStateListener() {

    }

    @Override
    public void onDestroyStateListener() {

    }

    @Override
    public void onIdleStateListener() {

    }

    @Override
    public void onErrorStateListener(int error) {
        position = (position + 1) % configList.size();
        play();
    }

    @Override
    public void complete() {
        position = (position + 1) % configList.size();
        play();
    }

    @interface PlayListStatus {

    }

    private void change() {
        if (null != onPositionChangedListener) {
            onPositionChangedListener.onPositionChangedListener(position);
        }
    }

    private OnPositionChangedListener onPositionChangedListener;


    public AudioListView(Context context) {
        super(context);
    }

    public AudioListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        xmMediaPlayer = new XmMediaPlayer(getContext());
        setOnClickListener(v -> {
            if (status == STATUS_INIT) {
                play();

            } else if (status == STATUS_PLAYING) {
                xmMediaPlayer.pause();
                setStatus(STATUS_PAUSE);
            } else if (status == STATUS_PAUSE) {
                if (xmMediaPlayer.getState() == IMediaStateChangeListener.STATE_PAUSE) {
                    xmMediaPlayer.start();
                } else {
                    play();
                }
                setStatus(STATUS_PLAYING);
            }
        });
    }

    private void play() {
        IDownloadConfig iDownloadConfig = configList.get(position);
        xmMediaPlayer.play(iDownloadConfig.getAudioName(), iDownloadConfig, this);
        setStatus(STATUS_PLAYING);
        change();
    }

    public void setStatus(@PlayListStatus int status) {
        this.status = status;
        setBackgroundResource(this.status == STATUS_INIT || this.status == STATUS_PAUSE ? R.drawable.icon_play_list_play : R.drawable.icon_play_list_pause);
    }


    public void setPosition(int position) {
        this.position = position;
        if (xmMediaPlayer.isPlaying()) {
            xmMediaPlayer.stop();
        }
        play();
    }

    private void setData(@NonNull List<IDownloadConfig> list, int position) {
        this.configList = list;
        this.position = position;
    }

    public void destroy() {
        xmMediaPlayer.destroy();
    }

    public interface OnPositionChangedListener {
        void onPositionChangedListener(int position);
    }

    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
        this.onPositionChangedListener = onPositionChangedListener;
    }
}
