package com.weici.test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.weici.audio.download.view.AudioDownloadView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudioDownloadView audioDownloadView = findViewById(R.id.audio_view);
        audioDownloadView.setAudioDownloadConfig(new AudioDownloadConfig("altitude.aac"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                audioDownloadView.autoPlay(2);
            }
        },2000);
    }
}
