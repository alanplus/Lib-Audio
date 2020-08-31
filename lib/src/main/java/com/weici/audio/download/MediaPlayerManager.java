package com.weici.audio.download;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.weici.audio.R;
import com.weici.audio.XmMediaPlayer;
import com.weici.audio.download.view.AudioDownloadView;


/**
 * Created by Mouse on 2019/1/29.
 */
public class MediaPlayerManager implements IMediaStateChangeListener {

    private XmMediaPlayer xmMediaPlayer;
    private View mView;

    private static MediaPlayerManager mediaplayerManager;
    private Context context;

    private AudioDownloadView.OnMediaPlayStateListener onMediaPlayStateListener;

    private MediaPlayerManager(Context context) {
        this.context = context.getApplicationContext();
        xmMediaPlayer = new XmMediaPlayer(this.context);
    }

    public static MediaPlayerManager getInstance(Context context) {
        if (mediaplayerManager == null) {
            mediaplayerManager = new MediaPlayerManager(context);
        }
        return mediaplayerManager;
    }

    public void play(View view, IDownloadConfig iDownloadConfig) {
        this.onMediaPlayStateListener = null;
        String name = iDownloadConfig.getAudioName();
        if (mView == view) {
            if (xmMediaPlayer.isPlaying()) {
                xmMediaPlayer.stop();
            } else {
                xmMediaPlayer.play(name, iDownloadConfig, this);
            }
        } else {
            if (xmMediaPlayer.getState() == IMediaStateChangeListener.STATE_IDLE) {
                this.mView = view;
                xmMediaPlayer.play(name, iDownloadConfig, this);
            } else {
                xmMediaPlayer.stop();
                xmMediaPlayer.setiMediaStateChangeListener(null);
                resetView();
                this.mView = view;
                xmMediaPlayer.play(name, iDownloadConfig, this);
            }
        }
    }

    public void play(View view, IDownloadConfig iDownloadConfig, AudioDownloadView.OnMediaPlayStateListener onMediaPlayStateListener) {
        play(view, iDownloadConfig);
        this.onMediaPlayStateListener = onMediaPlayStateListener;
    }

    public void play(View view, String name) {
        if (mView == view) {
            if (xmMediaPlayer.isPlaying()) {
                xmMediaPlayer.stop();
            } else {
                xmMediaPlayer.play(name, XmMediaPlayer.AUDIO_FILE_TYPE_ASSETS);
            }
        } else {
            if (xmMediaPlayer.getState() == IMediaStateChangeListener.STATE_IDLE) {
                this.mView = view;
                xmMediaPlayer.play(name, XmMediaPlayer.AUDIO_FILE_TYPE_ASSETS);
            } else {
                xmMediaPlayer.stop();
                xmMediaPlayer.setiMediaStateChangeListener(null);
                resetView();
                this.mView = view;
                xmMediaPlayer.play(name, XmMediaPlayer.AUDIO_FILE_TYPE_ASSETS);
            }
        }
    }

    public void play(View view, String name, AudioDownloadView.OnMediaPlayStateListener onMediaPlayStateListener) {
        play(view, name);
        this.onMediaPlayStateListener = onMediaPlayStateListener;
    }

    public void stop() {
        if (null != xmMediaPlayer && xmMediaPlayer.isPlaying()) {
            xmMediaPlayer.stop();
        }
    }

    private void resetView() {
        if (null == mView) return;
        mView.clearAnimation();
        mView.setAlpha(1.0f);
        int bg = (int) this.mView.getTag(R.id.tag_audio_default_bg);
        if (this.mView instanceof ImageView) {
            ((ImageView) this.mView).setImageResource(bg);
        } else {
            this.mView.setBackgroundResource(bg);
        }
    }

    public void startViewAnimation() {

        try {
            mView.clearAnimation();
            Object tag3 = mView.getTag(R.id.tag_anim_type);
            // 0 背景 帧动画
            int animType = 0;
            if (tag3 instanceof Integer) {
                animType = (int) tag3;
            }
            Object tag = mView.getTag(R.id.tag_audio_anim);
            if (null == tag) {
                return;
            }
            if (animType == 0) {
                mView.setBackgroundResource((Integer) tag);
                AnimationDrawable animationDrawable = (AnimationDrawable) mView.getBackground();
                animationDrawable.start();
            } else if (animType == 1) {
                Object tag1 = mView.getTag(R.id.tag_audio_anim_bg);
                if (tag1 instanceof Integer) {
                    mView.setBackgroundResource((Integer) tag1);
                }
                mView.startAnimation(AnimationUtils.loadAnimation(context, (Integer) tag));
            }
        } catch (Exception e) {
            Log.e("error", Log.getStackTraceString(e));
        }

    }

    @Override
    public void onLoadingStateListener() {
        if (null != onMediaPlayStateListener) {
            onMediaPlayStateListener.onLoadingStateListener();
        }
        Object tag = this.mView.getTag(R.id.tag_loading);
        if (null == tag) return;
        int drawable = (int) tag;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        animation.setInterpolator(new LinearInterpolator());
        this.mView.setBackgroundResource(drawable);
        this.mView.startAnimation(animation);
    }

    @Override
    public void onLoadFinishStateListener() {
        if (null != onMediaPlayStateListener) {
            onMediaPlayStateListener.onLoadFinishStateListener();
        }
    }

    @Override
    public void onPrepareStateListener() {
        if (null != onMediaPlayStateListener) {
            onMediaPlayStateListener.onPrepareStateListener();
        }
    }

    @Override
    public void onStartStateListener() {

        if (null == mView) {
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onStartStateListener();
            }
            return;
        }
        mView.post(new Runnable() {
            @Override
            public void run() {
                startViewAnimation();
                if (null != onMediaPlayStateListener) {
                    onMediaPlayStateListener.onStartStateListener();
                }
            }
        });
    }

    @Override
    public void onPauseStateListener() {

        if (null == mView) {
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onPauseStateListener();
            }
            return;
        }
        mView.post(() -> {
            resetView();
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onPauseStateListener();
            }
        });
    }

    @Override
    public void onStopStateListener() {

        if (null == mView) {
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onStopStateListener();
            }
            return;
        }
        mView.post(new Runnable() {
            @Override
            public void run() {
                resetView();
                if (null != onMediaPlayStateListener) {
                    onMediaPlayStateListener.onStopStateListener();
                }
            }
        });
    }

    @Override
    public void onDestroyStateListener() {

        if (null == mView) {
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onDestroyStateListener();
            }
            return;
        }
        mView.post(new Runnable() {
            @Override
            public void run() {
                resetView();
                if (null != onMediaPlayStateListener) {
                    onMediaPlayStateListener.onDestroyStateListener();
                }
            }
        });
    }

    @Override
    public void onIdleStateListener() {

        if (null == mView) {
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onIdleStateListener();
            }
            return;
        }
        mView.post(() -> {
            resetView();
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onIdleStateListener();
            }
        });
    }

    @Override
    public void onErrorStateListener(int error) {

        if (null == mView) {
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onErrorStateListener(error);
            }
            return;
        }
        mView.post(() -> {
            resetView();
            if (error == IMediaStateChangeListener.ERROR_NO_NET) {
                Toast.makeText(context, "网络无效，请重试", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "播放失败", Toast.LENGTH_LONG).show();
            }
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onErrorStateListener(error);
            }
        });
    }

    @Override
    public void complete() {
        if (null == mView) {
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onComplete();
            }
            return;
        }
        mView.post(() -> {
            resetView();
            if (null != onMediaPlayStateListener) {
                onMediaPlayStateListener.onComplete();
            }
        });
    }

    public void destroy() {
        try {
            mView = null;
            xmMediaPlayer.destroy();
            mediaplayerManager = null;
        } catch (Exception e) {
            Log.e("audio_error", Log.getStackTraceString(e));
        }
    }

    public XmMediaPlayer getXmMediaPlayer(){
        return xmMediaPlayer;
    }

}
