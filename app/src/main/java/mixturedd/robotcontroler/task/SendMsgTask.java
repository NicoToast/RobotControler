package mixturedd.robotcontroler.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * SendMsgTask.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/29 11:28.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class SendMsgTask extends AsyncTask<String, Void, Boolean> {
    public static final String TAG = "SendMsgTask";
    private Socket mSocket;

    public SendMsgTask(Socket socket){
        mSocket = socket;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            OutputStream mWriter = mSocket.getOutputStream();
            mWriter.write(hexStringToBytes(params[0]));
            mWriter.flush();
            Log.d(TAG, "成功发送数据:" + params[0]);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            Log.d(TAG, d[i] + "");
        }

        return d;
    }

    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
