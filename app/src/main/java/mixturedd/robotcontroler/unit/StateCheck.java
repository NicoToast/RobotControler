package mixturedd.robotcontroler.unit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static mixturedd.robotcontroler.remoter.ControlCode.SEVER_URL;

/**
 * StateCheck.java
 * Description :
 * <p>
 * Created by hp on 2016/12/26 09:59.
 * Copyright © 2016 hp. All rights reserved.
 */
public class StateCheck {
    private static final String TAG = "StateCheck";

    public static boolean isConnectRobot(Context context, String urlStr) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        return activeNetwork != null && testConnect(urlStr);
        // TODO: 2017/5/24 此处需要改 + activeNetwork.getType() == ConnectivityManager.TYPE_WIFI &&
    }

    public static boolean testConnect(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return (responseCode == 200);
        } catch (IOException e) {
            return false;
        }
    }
}
