package mixturedd.robotcontroler.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Config.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/6/1 09:16.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class Config implements Parcelable{
    private ServerConfig serverConfig;
    private VideoConfig videoConfig;


    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public VideoConfig getVideoConfig() {
        return videoConfig;
    }

    public void setVideoConfig(VideoConfig videoConfig) {
        this.videoConfig = videoConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.serverConfig, 0);
        dest.writeParcelable(this.videoConfig, 0);
    }

    public Config() {
        serverConfig = new ServerConfig();
        videoConfig = new VideoConfig();
    }

    protected Config(Parcel in) {
        this.serverConfig = in.readParcelable(ServerConfig.class.getClassLoader());
        this.videoConfig = in.readParcelable(VideoConfig.class.getClassLoader());
    }

    public static final Creator<Config> CREATOR = new Creator<Config>() {
        public Config createFromParcel(Parcel source) {
            return new Config(source);
        }

        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
}
