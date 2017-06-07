package mixturedd.robotcontroler.remoter.mjpeg;

import android.graphics.Bitmap;

/**
 * OnFrameCapturedListener.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/30 17:19.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public interface OnFrameCapturedListener {
    void onFrameCaptured(Bitmap bitmap);

}
