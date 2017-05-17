package mixturedd.robotcontroler.remoter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;
import static mixturedd.robotcontroler.remoter.ControlCode.SEVER_URL;
import static mixturedd.robotcontroler.remoter.ControlCode.VIDEO_MODEL_STREAM;
import static mixturedd.robotcontroler.remoter.ControlCode.VIDEO_PORT;

/**
 * RemoterFragPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/4/24 17:39.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class RemoterFragPresenter implements RemoterContract.FragPresenter {
    private static final String TAG = "RemoterFragPresenter";
    private static final String URL = SEVER_URL + ":" + VIDEO_PORT + "/?action=" + VIDEO_MODEL_STREAM;
    private static final String COMM_MOVE_BACKWARDS = "FF000200FF";
    private static final String COMM_MOVE_FORWARD = "FF000100FF";
    private static final String COMM_MOVE_RIGHT = "FF000400FF";
    private static final String COMM_MOVE_LEFT = "FF000300FF";
    private static final String COMM_MOVE_STOP = "FF000000FF";
    private MjpegSurfaceView mMjpegSurfaceView;
    private RemoterContract.InfoPresenter mInfoPresenter;
    private ClientThread mClient;

    private RemoterContract.FragView mFragView;

    @Override
    public void init(RemoterContract.FragView fragView, View view) {
        this.mFragView = checkNotNull(fragView, "actView cannot be null!");
        this.mFragView.initView(view);
    }

    public void initFragmentsPresenter(RemoterInfoFragment infoFragment, RemoterHandFragment handFragment, RemoterToolbarFragment toolbarFragment){
        mInfoPresenter = (RemoterInfoPresenter) infoFragment.getPresenters()[0];

    }

    @Override
    public void orderSendBySocket(String order) {
        Message msg = new Message();
        msg.what = 0x345;
        msg.obj = order;
        mClient.getRevHandler().sendMessage(msg);
    }

    public void starSocketClient() {
        Handler handler = new MsgReceiveHandler(mInfoPresenter);
        mClient = new ClientThread(handler);
        new Thread(mClient).start();
    }

    public void stopSocketClient() {
        if (mClient != null) {
            mClient.stopSocket();
        }
    }

    public void setSurfaceView(MjpegSurfaceView view) {
        mMjpegSurfaceView = view;
    }

    /**
     * 播放视频
     */
    @Override
    public void startMjpeg() {
        if (mMjpegSurfaceView == null) {
            Log.i(TAG, "视频为空");
            return;
        }
//        toolbarView.videoPlayOnPress(mMjpegSurfaceView);
        new DrawTask().execute(URL);
    }

    /**
     * 停止视频
     */
    @Override
    public void stopMjpeg() {
        if (mMjpegSurfaceView == null) {
            Log.i(TAG, "视频为空");
            return;
        }
//        toolbarView.videoPauseOnPress(mMjpegSurfaceView);
        mMjpegSurfaceView.stopPlayback();
    }

    @Override
    public void toggleToolbar(boolean visible) {
        if (visible) {
            mFragView.hideToolbar();
        } else {
            mFragView.showToolbar();
        }
    }

    @Override
    public void toggleInfo(boolean visible) {
        if (visible) {
            mFragView.hideInfo();
        } else {
            mFragView.showInfo();
        }
    }

    @Override
    public void toggleHand(boolean visible) {
        if (visible) {
            mFragView.hideHand();
        } else {
            mFragView.showHand();
        }
    }

    @Override
    public void forward(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                orderSendBySocket(COMM_MOVE_FORWARD);
                break;
            case MotionEvent.ACTION_UP:
                stop();
                break;
        }
    }

    @Override
    public void backwards(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                orderSendBySocket(COMM_MOVE_BACKWARDS);
                break;
            case MotionEvent.ACTION_UP:
                stop();
                break;
        }
    }

    @Override
    public void turnRight(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                orderSendBySocket(COMM_MOVE_RIGHT);
                break;
            case MotionEvent.ACTION_UP:
                stop();
                break;
        }
    }

    @Override
    public void turnLeft(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                orderSendBySocket(COMM_MOVE_LEFT);
                break;
            case MotionEvent.ACTION_UP:
                stop();
                break;
        }
    }

    @Override
    public void stop() {
        orderSendBySocket(COMM_MOVE_STOP);
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onStart() {
        starSocketClient();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        stopSocketClient();
    }

    @Override
    public void onDestroy() {

    }

    private class DrawTask extends AsyncTask<String, Void, MjpegInputStream> {
        private static final String TAG = "DrawTask";

        @Override
        protected MjpegInputStream doInBackground(String... url) {
            HttpURLConnection conn;
            InputStream inputstream = null;
            Log.d(TAG, "1. Sending http request");
            try {
                URL videoUrl = new URL(url[0]);
                conn = (HttpURLConnection) videoUrl.openConnection();
                //设置输入流
                conn.setDoInput(true);
                //连接
                conn.connect();
                //得到网络返回的输入流
                inputstream = conn.getInputStream();
                Log.d(TAG, "2. Request finished, status = " + conn.getResponseCode());
                if (conn.getResponseCode() == 401) {
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(inputstream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, e.getLocalizedMessage());
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

    private static class MsgReceiveHandler extends Handler{
        private RemoterContract.InfoPresenter mInfoPresenter;

        public MsgReceiveHandler(RemoterContract.InfoPresenter infoPresenter) {
            this.mInfoPresenter = infoPresenter;
        }

        @Override
        public void handleMessage(Message msg) {
            // 如果消息来自于子线程
            if (msg.what == 0x123) {
                String string = msg.obj.toString();
                int colonIndex = string.indexOf(":");
                if (colonIndex == -1) {
                    return;
                }
                StringBuffer stringBuffer = new StringBuffer(string);
                String infoCategory = stringBuffer.substring(0, colonIndex);
                String value = stringBuffer.substring(string.indexOf(":") + 1, string.length());
                switch (infoCategory){
                    case "Distance":
                        mInfoPresenter.postDistance(value);
                        break;
                    case "Humidity":
                        mInfoPresenter.postHumidity(value);
                        break;
                    case "Temperature":
                        mInfoPresenter.postTemperature(value);
                        break;
                    case "Gases":
                        mInfoPresenter.postGas(value);
                        break;
                }
            }
        }
    }
}
