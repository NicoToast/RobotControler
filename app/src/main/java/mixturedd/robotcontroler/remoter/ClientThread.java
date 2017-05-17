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

public class ClientThread implements Runnable {
    private Socket mSocket;
    // 定义向UI线程发送消息的Handler对象
    private Handler mHandler;
    // 定义接收UI线程的消息的Handler对象
    private Handler revHandler;
    // 该线程所处理的Socket所对应的输入流
    private BufferedReader br = null;

    public ClientThread(Handler handler) {
        this.mHandler = handler;
    }

    public void run() {
        try {
            //192.168.1.2为本机的ip地址，2001为与MultiThreadServer服务器通信的端口           
            mSocket = new Socket(ControlCode.SEVER_IP, ControlCode.SEVER_PORT);
            br = new BufferedReader(new InputStreamReader(
                    mSocket.getInputStream()));
            // 启动一条子线程来读取服务器响应的数据
            new Thread() {
                @Override
                public void run() {
                    String content = null;
                    // 不断读取Socket输入流中的内容。
                    try {
                        while ((content = br.readLine()) != null) {
                            // 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            mHandler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            // 为当前线程初始化Looper
            Looper.prepare();
            // 创建revHandler对象
            revHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 接收到UI线程中用户输入的数据
                    if (msg.what == 0x345) {
                        // 将用户在文本框内输入的内容写入网络
                        try {
                            /*os.write((msg.obj.toString() + "\r\n")
                                    .getBytes("utf-8"));*/
                            new GetOutputTask().execute((msg.obj.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            // 启动Looper
            Looper.loop();
        } catch (SocketTimeoutException e1) {
            System.out.println("网络连接超时！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopSocket() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Handler getRevHandler() {
        return revHandler;
    }

    private class GetOutputTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetOutputTask";
        //        private Socket mSocket;
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