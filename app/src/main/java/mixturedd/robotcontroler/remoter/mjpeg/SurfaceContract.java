package mixturedd.robotcontroler.remoter.mjpeg;

import mixturedd.robotcontroler.remoter.MjpegInputStream;

/**
 * SurfaceContract.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/25 10:18.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public interface SurfaceContract {
    void startPlayback();

    void stopPlayback();

    void setSource(MjpegInputStream stream);

    void setDisplayMode(DisplayMode mode);

    void showFps(boolean show);

    boolean isStreaming();

    void setResolution(int width, int height);

    void freeCameraMemory();

    void setOnFrameCapturedListener(OnFrameCapturedListener onFrameCapturedListener);
}
