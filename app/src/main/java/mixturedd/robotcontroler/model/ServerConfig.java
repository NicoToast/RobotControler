package mixturedd.robotcontroler.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ServerConfig.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/31 23:28.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class ServerConfig implements Parcelable{
    private String host;
    private int port;

    public static ServerConfig newInstance(String host, int port) {
        ServerConfig sc = new ServerConfig();
        sc.setHost(host);
        sc.setPort(port);
        return sc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.host);
        dest.writeInt(this.port);
    }

    public ServerConfig() {
    }

    protected ServerConfig(Parcel in) {
        this.host = in.readString();
        this.port = in.readInt();
    }

    public static final Creator<ServerConfig> CREATOR = new Creator<ServerConfig>() {
        public ServerConfig createFromParcel(Parcel source) {
            return new ServerConfig(source);
        }

        public ServerConfig[] newArray(int size) {
            return new ServerConfig[size];
        }
    };

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
}
