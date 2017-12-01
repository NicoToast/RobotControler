package mixturedd.robotcontroler.task;

import java.io.IOException;

import mixturedd.robotcontroler.remoter.MjpegSurfaceView;

/**
 * ClientContract.java
 * Description :客户端操作接口
 * <p>
 * Created by MixtureDD on 2017/5/29 11:12.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public interface ClientContract {

    void stopClient() throws IOException ;

    void sendOrder(String order);

    void openCam(String strUrl, MjpegSurfaceView mjpegSurfaceView);

    boolean isClientRunning();
}
