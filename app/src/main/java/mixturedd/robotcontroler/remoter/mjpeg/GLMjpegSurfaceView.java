package mixturedd.robotcontroler.remoter.mjpeg;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * GLMjpegSurfaceView.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/25 10:16.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class GLMjpegSurfaceView extends GLSurfaceView {

    public GLMjpegSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public class GLRenderer implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }
}
