package com.weici.audio.download;

import android.support.annotation.IntDef;

/**
 * Created by Mouse on 2019/1/29.
 */
public interface IMediaStateChangeListener {


    int STATE_IDLE = 0;
    int STATE_LOADING = 1;
    int STATE_LOADFINISH = 2;
    int STATE_PREPARE = 3;
    int STATE_START = 4;
    int STATE_PAUSE = 5;
    int STATE_STOP = 6;
    int STATE_DESTROY = 7;
    int STATE_ERROR = 8;
    int STATE_COMPLETE = 9;

    int ERROR_NO_NET = 0;
    int ERROR_MEDIA = 1;
    int ERROR_OTHER = 2;
    int ERROR_CANNOT_PLAY = 3;

    @IntDef({ERROR_NO_NET, ERROR_MEDIA, ERROR_OTHER})
    @interface error {
    }

    void onLoadingStateListener();

    void onLoadFinishStateListener();

    void onPrepareStateListener();

    void onStartStateListener();

    void onPauseStateListener();

    void onStopStateListener();

    void onDestroyStateListener();

    void onIdleStateListener();

    void onErrorStateListener(@error int error);

    void complete();
}
