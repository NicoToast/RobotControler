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
 * Description :
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

    @Override
    protected void onPreExecute() {
        mToolbarPresenter.sendMsg("已与小车建立连接", MSG_TYPE_SUCCESS);
    }

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

    @Override
    protected void onProgressUpdate(String[]... values) {
        postData(values);
    }

    private void recMsg(BufferedReader br) {
        String content;
        if (br == null) {
            Log.d(TAG, "BufferedReader is null");
            return;
        }
        try {
            while (mInputStream.read() != -1) {
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

    @Override
    protected void onPostExecute(Void aVoid) {
        mToolbarPresenter.sendMsg("已与小车失去连接", MSG_TYPE_ERROR);
    }

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

    private void initClient(@NonNull String host, int port) throws IOException {
        mSocket = new Socket(host, port);
        Log.d(TAG, "ClientRunning," + host + ":" + port);
    }

    @Override
    public void stopClient() throws IOException {
        if (mSocket == null) {
            throw new NullPointerException("Socket is null!");
        }
        mSocket.close();
        Log.d(TAG, "ClientStop");
    }

    @Override
    public void sendOrder(String order) {
        if (mSocket == null) {
            throw new NullPointerException("Socket is null");
        }
        new SendMsgTask(mSocket).execute(order);
    }

    @Override
    public void openCam(String strUrl, MjpegSurfaceView mjpegSurfaceView) {
        new InputStreamTask(mjpegSurfaceView).execute(strUrl);
    }

    @Override
    public boolean isClientRunning() {
        return !mSocket.isOutputShutdown();
    }

}
