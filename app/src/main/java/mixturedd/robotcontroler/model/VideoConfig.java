package mixturedd.robotcontroler.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * VideoConfig.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/6/1 09:33.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class VideoConfig implements Parcelable {
    private static final String TAG = "VideoConfig";
    public static final int ACTION_STREAM = 0;
    public static final int ACTION_SNAPSHOT = 1;
    private String host;
    private int port;
    private String actionStr;
    private String url = "http://%1$s:%2$s/?action=%3$s";

    public static VideoConfig newInstance(String host, int port, int action){
        VideoConfig vc = new VideoConfig();
        vc.setHost(host);
        vc.setPort(port);
        vc.setAction(action);
        return vc;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAction(int action) {
        switch (action) {
            case ACTION_STREAM:
                actionStr = "stream";
                break;
            case ACTION_SNAPSHOT:
                actionStr = "snapshot";
                break;
        }
    }

    public String getUrl(){
        return url;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.host);
        dest.writeInt(this.port);
        dest.writeString(String.format(url, host, port, actionStr));
    }

    public VideoConfig() {
    }

    protected VideoConfig(Parcel in) {
        this.host = in.readString();
        this.port = in.readInt();
        this.url = in.readString();
    }

    public static final Creator<VideoConfig> CREATOR = new Creator<VideoConfig>() {
        public VideoConfig createFromParcel(Parcel source) {
            return new VideoConfig(source);
        }

        public VideoConfig[] newArray(int size) {
            return new VideoConfig[size];
        }
    };
}
