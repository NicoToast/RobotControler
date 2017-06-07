package mixturedd.robotcontroler.remoter;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @deprecated
 */
public class ClientThread implements Runnable {
    public static final String TAG = "ClientThread";
    private static Socket mSocket;
    // 定义向UI线程发送消息的Handler对象
    private static Handler mRecHandler;
    // 定义接收UI线程的消息的Handler对象
    private static Handler mSendHandler;
    // 该线程所处理的Socket所对应的输入流
    private BufferedReader br = null;
    //ClientThread的运行状态
    private boolean ClientThreadRunState;

    private static ClientThread mClientThread;

    public static ClientThread getInstance(){
        if (null == mClientThread) {

            synchronized (ClientThread.class) {

                if (null == mClientThread) {
                    mClientThread = new ClientThread();
                }
            }

        }

        return mClientThread;
    }

    private ClientThread(){
        ClientThreadRunState = false;
    }


    public ClientThread setSendMessageHandler(Handler handler)throws NullPointerException{
        if (null == mClientThread) {
            throw new NullPointerException("ClientThread must be initialized");
        }
        mRecHandler = handler;
        ClientThreadRunState = true;
        return mClientThread;
    }

    public void run() {
        try {
            mSocket = new Socket(ControlCode.SEVER_IP, ControlCode.SEVER_PORT);
            br = new BufferedReader(new InputStreamReader(
                    mSocket.getInputStream()));
            // 启动一条子线程来读取服务器响应的数据
            new Thread() {
                @Override
                public void run() {
                    String content;
                    // 不断读取Socket输入流中的内容。
                    try {
                        while ((content = br.readLine()) != null && ClientThreadRunState) {
                            // 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            mRecHandler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            // 为当前线程初始化Looper
            Looper.prepare();
            // 创建revHandler对象
            mSendHandler = new SendHandler();
            // 启动Looper
            Looper.loop();
        } catch (SocketTimeoutException e1) {
            stopSocket();
            System.out.println("网络连接超时！！");
        } catch (Exception e) {
            stopSocket();
            e.printStackTrace();
        }
    }

    public void stopSocket() {
        try {
            if (mSocket != null){
                ClientThreadRunState = false;
                mSocket.close();
                mSocket = null;
                mRecHandler = null;
                mSendHandler = null;
            }else{
                Log.e(TAG, "ClientThread is null!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Handler getSendHandler() {
        return mSendHandler;
    }

    public boolean isClientThreadRun() {
        return ClientThreadRunState;
    }

    private static class SendHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // 接收到UI线程中用户输入的数据
            if (msg.what == 0x345) {
                // 将用户在文本框内输入的内容写入网络
                try {
                    new GetOutputTask().execute((msg.obj.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private class GetOutputTask extends AsyncTask<String, Void, String> {
            private static final String TAG = "GetOutputTask";
            private OutputStream mWriter;

            @Override
            protected String doInBackground(String... params) {
                sendMsg(params[0]);
                return null;
            }

            private void sendMsg(String msg) {
                try {
                    mWriter = mSocket.getOutputStream();
                    mWriter.write(hexStringToBytes(msg));
                    mWriter.flush();
                    Log.d(TAG, "成功发送数据:" + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public byte[] hexStringToBytes(String hexString) {
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
    }


}