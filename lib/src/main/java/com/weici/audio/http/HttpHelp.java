package com.weici.audio.http;

import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Alan
 * 时 间：2020-04-19
 * 简 述：<功能简述>
 */
public class HttpHelp {

    public static void download(String url, Callback callback) {
        OkHttpClient client = initClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        client.newCall(request).enqueue(callback);
    }


    public static String handlerResponse(Response response, File destFile) throws Exception {
        if (response.isSuccessful()) {
            InputStream is = null;
            byte[] buf = new byte[2048];

            FileOutputStream fos = null;
            try {
                if (destFile.exists()) {
                    destFile.delete();
                }
                destFile.createNewFile();
                is = response.body().byteStream();
                fos = new FileOutputStream(destFile);
                int len;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                return destFile.getAbsolutePath();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    Log.e("error", Log.getStackTraceString(e));
                }
            }
        }
        throw new Exception("request is error");
    }

    private static OkHttpClient initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        builder.connectTimeout(1, TimeUnit.MINUTES);
        return builder.build();
    }

}