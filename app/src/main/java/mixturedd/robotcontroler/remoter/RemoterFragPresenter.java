package mixturedd.robotcontroler.remoter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;

import mixturedd.robotcontroler.BaseActivity;
import mixturedd.robotcontroler.model.Config;
import mixturedd.robotcontroler.task.Client;

import static com.google.common.base.Preconditions.checkNotNull;
import static mixturedd.robotcontroler.remoter.RemoterFragment.VIEW_INVISIBLE;
import static mixturedd.robotcontroler.remoter.RemoterFragment.VIEW_VISIBLE;
import static mixturedd.robotcontroler.remoter.RemoterFragment.VIEW_VISIBLE_AUTO;
import static mixturedd.robotcontroler.remoter.RemoterFragment.floatViewVisible;
import static mixturedd.robotcontroler.remoter.RemoterFragment.handVisible;
import static mixturedd.robotcontroler.remoter.RemoterFragment.infoVisible;
import static mixturedd.robotcontroler.remoter.RemoterFragment.toolbarVisible;

/**
 * RemoterFragPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/4/24 17:39.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class RemoterFragPresenter implements RemoterContract.FragPresenter {
    private static final String TAG = "RemoterFragPresenter";
//    private static final String URL = SEVER_URL + ":" + VIDEO_PORT + "/?action=" + VIDEO_MODEL_STREAM;
    private static final String COMM_MOVE_BACKWARDS = "FF000200FF";
    private static final String COMM_MOVE_FORWARD = "FF000100FF";
    private static final String COMM_MOVE_RIGHT = "FF000400FF";
    private static final String COMM_MOVE_LEFT = "FF000300FF";
    private static final String COMM_MOVE_STOP = "FF000000FF";
    private MjpegSurfaceView mMjpegSurfaceView;
    private RemoterContract.InfoPresenter mInfoPresenter;
    private RemoterContract.ToolbarPresenter mToolbarPresenter;
    //    private ClientThread mClient;
    private Client mClient;
    private BaseActivity mActivity;
    private RemoterContract.FragView mFragView;
    private Config mConfig;
//    private DrawTask mDrawTask;

    @Override
    public void init(RemoterContract.FragView fragView, View view) {
        this.mFragView = checkNotNull(fragView, "actView cannot be null!");
        this.mFragView.initView(view);
    }

    void initFragmentsPresenter(RemoterInfoFragment infoFragment, RemoterHandFragment handFragment, RemoterToolbarFragment toolbarFragment) {
        mInfoPresenter = (RemoterInfoPresenter) infoFragment.getPresenters()[0];
        mToolbarPresenter = (RemoterToolbarPresenter) toolbarFragment.getPresenters()[0];

    }

    void setConfig(Config config) {
        mConfig = config;
    }

    @Override
    public void orderSendBySocket(String order) {
/*        if(mClient.isClientThreadRun()){
            Message msg = new Message();
            msg.what = 0x345;
            msg.obj = order;
            mClient.getSendHandler().sendMessage(msg);
        }*/
        try {
            if (mClient.isClientRunning()) {
                mClient.sendOrder(order);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void starSocketClient() {
        mClient = new Client(mInfoPresenter, mToolbarPresenter);
/*        mClient = ClientThread.getInstance().setSendMessageHandler(new MsgReceiveHandler(mInfoPresenter));
        new Thread(mClient).start();*/
        mClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mConfig.getServerConfig());
    }

    private void stopSocketClient() {
        try {
            if (mClient.isClientRunning()) {
                mClient.stopClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
/*        if (mClient != null) {
            mClient.stopSocket();
        }*/
    }

    void setSurfaceView(MjpegSurfaceView view) {
        mMjpegSurfaceView = view;
    }

    @Override
    public BaseActivity getActivity() {
        return mActivity;
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
/*        if (mDrawTask == null){
            mDrawTask = new DrawTask();
            mDrawTask.execute(URL);
        }*/
        String url = mConfig.getVideoConfig().getUrl();
        mClient.openCam(url, mMjpegSurfaceView);
        Log.d(TAG, "video url:" + url);
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
        mMjpegSurfaceView.stopPlayback();
    }

    @Override
    public void refreshInputStream() {

    }

    @Override
    public void toggleFloatView(int visible) {
        switch (visible) {
            case VIEW_VISIBLE_AUTO:
                if (floatViewVisible) {
                    mFragView.hideFloatView();
                } else {
                    mFragView.showFloatView();
                }
                break;
            case VIEW_VISIBLE:
                mFragView.showFloatView();
                break;
            case VIEW_INVISIBLE:
                mFragView.hideFloatView();
                break;
        }
    }

    @Override
    public void toggleToolbar(int visible) {
        switch (visible) {
            case VIEW_VISIBLE_AUTO:
                if (toolbarVisible) {
                    mFragView.hideToolbar();
                } else {
                    mFragView.showToolbar();
                }
                break;
            case VIEW_VISIBLE:
                mFragView.showToolbar();
                break;
            case VIEW_INVISIBLE:
                mFragView.hideToolbar();
                break;
        }
    }

    @Override
    public void toggleInfo(int visible) {
        switch (visible) {
            case VIEW_VISIBLE_AUTO:
                if (infoVisible) {
                    mFragView.hideInfo();
                } else {
                    mFragView.showInfo();
                }
                break;
            case VIEW_VISIBLE:
                mFragView.showInfo();
                break;
            case VIEW_INVISIBLE:
                mFragView.hideInfo();
                break;
        }
    }

    @Override
    public void toggleHand(int visible) {
        switch (visible) {
            case VIEW_VISIBLE_AUTO:
                if (handVisible) {
                    mFragView.hideHand();
                } else {
                    mFragView.showHand();
                }
                break;
            case VIEW_VISIBLE:
                mFragView.showHand();
                break;
            case VIEW_INVISIBLE:
                mFragView.hideHand();
                break;
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
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onStart() {
        starSocketClient();
    }

    @Override
    public void onResume() {
        mToolbarPresenter.setStatePlay();
    }

    @Override
    public void onPause() {
        mToolbarPresenter.setStatePause();
    }

    @Override
    public void onStop() {
        stopSocketClient();
    }

    @Override
    public void onDestroy() {

    }

/*    private class DrawTask extends AsyncTask<String, Void, MjpegInputStream> {
        private static final String TAG = "DrawTask";

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
    }*/
}
