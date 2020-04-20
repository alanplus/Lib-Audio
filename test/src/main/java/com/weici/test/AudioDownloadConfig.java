package com.weici.test;


import com.weici.audio.download.IDownloadConfig;

/**
 * Created by Mouse on 2019/1/29.
 */
public class AudioDownloadConfig implements IDownloadConfig {

    private String name;


    public AudioDownloadConfig(String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getDestName(String s) {
        return "/data/data/com.weici.test/audio/" + s;
    }

    @Override
    public String getAudioName() {
        return name;
    }
}
