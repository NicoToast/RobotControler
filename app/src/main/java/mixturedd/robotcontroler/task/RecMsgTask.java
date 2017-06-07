package mixturedd.robotcontroler.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import mixturedd.robotcontroler.remoter.RemoterContract;

/**
 * RecMsgTask.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/29 11:27.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class RecMsgTask extends AsyncTask<Void, String[], Void> {
    public static final String TAG = "RecMsgTask";
/*    private static final int MESSAGE_POST_SENSOR_DATA = 0x110;
    private static final int MESSAGE_POST_SENSOR_ERROR = 0x111;*/

    private BufferedReader br;
//    private Handler mRecHandler;
    private RemoterContract.InfoPresenter mInfoPresenter;


    public RecMsgTask(Socket socket, RemoterContract.InfoPresenter infoPresenter) throws IOException {
        this.mInfoPresenter = infoPresenter;
//        mRecHandler = new MsgReceiveHandler(infoPresenter);
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (NullPointerException e) {
            br = null;
        }
    }

    @Override
    protected void onProgressUpdate(String[]... values) {
        postData(values);
        Log.d(TAG, values[0][0] + ":" + values[0][1]);
    }

    @Override
    protected Void doInBackground(Void... params) {
        String content;
        if (br == null) {
            Log.d(TAG, "BufferedReader is null");
            return null;
        }
        try {
            while ((content = br.readLine()) != null) {
                int colonIndex = content.indexOf(":");
                if (colonIndex == -1) {
                    return null;
                }
                StringBuilder stringBuilder = new StringBuilder(content);
                String infoCategory = stringBuilder.substring(0, colonIndex);
                String value = stringBuilder.substring(content.indexOf(":") + 1, content.length());
                String[] data = new String[]{infoCategory, value};
                // 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
                publishProgress(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void postData(String[]... data){
        for (String[] d : data){
            String infoCategory = d[0];
            String value = d[1];
            switch (infoCategory) {
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

/*
    @Override
    public void run() {
        String content;
        // 不断读取Socket输入流中的内容。
        Looper.prepare();
        if (br == null) {
            Log.d(TAG, "BufferedReader is null");
            Message msg = mRecHandler.obtainMessage();
            msg.what = MESSAGE_POST_SENSOR_ERROR;
            msg.obj = "暂时无法获得视频流";
            mRecHandler.sendMessage(msg);
            Looper.loop();
            return;
        }
        try {
            while ((content = br.readLine()) != null) {
                int colonIndex = content.indexOf(":");
                if (colonIndex == -1) {
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder(content);
                String infoCategory = stringBuilder.substring(0, colonIndex);
                String value = stringBuilder.substring(content.indexOf(":") + 1, content.length());
                // 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
                String[] data = new String[]{infoCategory, value};
                Map<String, String> mapData = new HashMap<>();
                mapData.put(infoCategory, value);
                Message msg = Message.obtain();
                msg.what = MESSAGE_POST_SENSOR_DATA;
                msg.obj = data;
                mRecHandler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }


    private static class MsgReceiveHandler extends Handler {
        private RemoterContract.InfoPresenter mInfoPresenter;

        MsgReceiveHandler(RemoterContract.InfoPresenter infoPresenter) {
            this.mInfoPresenter = infoPresenter;
        }

        @Override
        public void handleMessage(Message msg) {
            // 如果消息来自于子线程
            switch (msg.what) {
                case MESSAGE_POST_SENSOR_DATA:
                    String[] data = (String[]) msg.obj;
                    String infoCategory = data[0];
                    String value = data[1];
                    switch (infoCategory) {
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
                    break;
                case MESSAGE_POST_SENSOR_ERROR:

                    break;

            }
        }
    }
    */
}
