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

    public static boolean isConnectRobot(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && testConnect()) {
                // connected to wifi
                return true;
            } else {
                return false;
            }
        } else {
            // not connected to the internet
            return false;
        }
    }

    private static boolean testConnect() {
        try {
            URL url = new URL(SEVER_URL);
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
