package mixturedd.robotcontroler.remoter.mjpeg;

import android.view.SurfaceHolder;

/**
 * AbstractMjpegView.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/30 17:11.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public abstract class AbstractMjpegView implements SurfaceContract {

    protected final static int POSITION_UPPER_LEFT = 9;
    protected final static int POSITION_UPPER_RIGHT = 3;
    protected final static int POSITION_LOWER_LEFT = 12;
    protected final static int POSITION_LOWER_RIGHT = 6;

    protected final static int SIZE_STANDARD = 1;
    protected final static int SIZE_BEST_FIT = 4;
    protected final static int SIZE_FULLSCREEN = 8;

    public abstract void onSurfaceCreated(SurfaceHolder holder);

    public abstract void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height);

    public abstract void onSurfaceDestroyed(SurfaceHolder holder);

}
