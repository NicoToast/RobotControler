package mixturedd.robotcontroler.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import mixturedd.robotcontroler.model.ServerConfig;
import mixturedd.robotcontroler.remoter.MjpegSurfaceView;
import mixturedd.robotcontroler.remoter.RemoterContract;

import static mixturedd.robotcontroler.remoter.RemoterToolbarFragment.MSG_TYPE_ERROR;
import static mixturedd.robotcontroler.remoter.RemoterToolbarFragment.MSG_TYPE_SUCCESS;

/**
 * Client.java
 * Description :客户端类。
 * <p>
 * Created by MixtureDD on 2017/5/29 10:49.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class Client extends AsyncTask<ServerConfig, String[], Void> implements ClientContract {
    private static final String TAG = "Client";
//    private boolean ClientRunning;
    private RemoterContract.InfoPresenter mInfoPresenter;
    private RemoterContract.ToolbarPresenter mToolbarPresenter;
    private InputStream mInputStream;
    private Socket mSocket;

    public Client(@NonNull RemoterContract.InfoPresenter infoPresenter,
                  @NonNull RemoterContract.ToolbarPresenter toolbarPresenter) {
        mInfoPresenter = infoPresenter;
        mToolbarPresenter = toolbarPresenter;
//        ClientRunning = false;
    }

    /**
     * 异步操作前的准备
     */
    @Override
    protected void onPreExecute() {
        mToolbarPresenter.sendMsg("已与小车建立连接", MSG_TYPE_SUCCESS);
    }

    /**
     * 后台操作函数
     * @param params {@link ServerConfig} Socket参数
     * @return NUll
     */
    @Override
    protected Void doInBackground(ServerConfig... params) {
        try {
            initClient(params[0].getHost(), params[0].getPort());
            mInputStream = mSocket.getInputStream();
            recMsg(new BufferedReader(new InputStreamReader(mInputStream)));
        } catch (IOException e) {
//            ClientRunning = false;
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发布数据
     * 由{@link #publishProgress}调用
     * @param values 从 InputStream 获取的数据
     */
    @Override
    protected void onProgressUpdate(String[]... values) {
        postData(values);
    }

    /**
     * 读取InputStream数据
     * @param br InputStreamReader
     */
    private void recMsg(BufferedReader br) {
        String content;
        if (br == null) {
            Log.d(TAG, "BufferedReader is null");
            return;
        }
        try {
            while (mInputStream.read() != -1) {//循环读取InputStream数据，直到无数据
                content = br.readLine();
                int colonIndex = content.indexOf(":");
                if (colonIndex == -1) {
                    continue;
                }
                StringBuilder stringBuilder = new StringBuilder(content);
                String infoCategory = stringBuilder.substring(0, colonIndex);
                String value = stringBuilder.substring(colonIndex + 1, content.length());
                Log.d(TAG, infoCategory + ":" + value);
                String[] data = new String[]{infoCategory, value};
                publishProgress(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@link #doInBackground}结束后调用
     * @param aVoid null
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        mToolbarPresenter.sendMsg("已与小车失去连接", MSG_TYPE_ERROR);
    }

    /**
     * 解析数据流 data
     * @param data
     */
    private void postData(String[]... data) {
        for (String[] d : data) {
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

    /**
     * 初始化Socket
     * @param host 服务器ip
     * @param port 服务器端口
     * @throws IOException
     */
    private void initClient(@NonNull String host, int port) throws IOException {
        mSocket = new Socket(host, port);
        Log.d(TAG, "ClientRunning," + host + ":" + port);
    }

    /**
     * 停止Socket
     * @throws IOException
     */
    @Override
    public void stopClient() throws IOException {
        if (mSocket == null) {
            throw new NullPointerException("Socket is null!");
        }
        mSocket.close();
        Log.d(TAG, "ClientStop");
    }

    /**
     * 发送指令
     * @param order 十六进制的字节流
     */
    @Override
    public void sendOrder(String order) {
        if (mSocket == null) {
            throw new NullPointerException("Socket is null");
        }
        new SendMsgTask(mSocket).execute(order);
    }

    /**
     * 打开摄像头
     * @param strUrl 摄像头URL
     * @param mjpegSurfaceView SurfaceView控件
     */
    @Override
    public void openCam(String strUrl, MjpegSurfaceView mjpegSurfaceView) {
        new InputStreamTask(mjpegSurfaceView).execute(strUrl);
    }

    @Override
    public boolean isClientRunning() {
        return !mSocket.isOutputShutdown();
    }

}
