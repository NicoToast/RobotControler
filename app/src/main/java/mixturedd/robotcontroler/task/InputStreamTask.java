package mixturedd.robotcontroler.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import mixturedd.robotcontroler.remoter.MjpegSurfaceView;
import mixturedd.robotcontroler.remoter.MjpegInputStream;

/**
 * InputStreamTask.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/29 21:00.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class InputStreamTask extends AsyncTask<String, Void, MjpegInputStream> {
    private static final String TAG = "InputStreamTask";
    private MjpegSurfaceView mMjpegSurfaceView;

    public InputStreamTask(MjpegSurfaceView mjpegSurfaceView) {
        this.mMjpegSurfaceView = mjpegSurfaceView;
    }

    @Override
    protected MjpegInputStream doInBackground(String... url) {
        HttpURLConnection conn;
        InputStream inputstream;
        Log.d(TAG, "1. Sending http request");
        try {
            URL videoUrl = new URL(url[0]);
            conn = (HttpURLConnection) videoUrl.openConnection();
            //设置输入流
            conn.setDoInput(true);
            //连接
            conn.connect();
            conn.setConnectTimeout(5000);
            //得到网络返回的输入流
            inputstream = conn.getInputStream();
            Log.d(TAG, "2. Request finished, status = " + conn.getResponseCode());
            if (conn.getResponseCode() == 401) {
                //You must turn off camera User Access Control before this will work
                return null;
            }
            return new MjpegInputStream(inputstream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(MjpegInputStream result) {
        mMjpegSurfaceView.setSource(result);
        mMjpegSurfaceView.setDisplayMode(MjpegSurfaceView.SIZE_BEST_FIT);
        mMjpegSurfaceView.showFps(false);
    }
}


