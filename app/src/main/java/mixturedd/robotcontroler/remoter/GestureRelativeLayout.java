package mixturedd.robotcontroler.remoter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * GestureFrameLayout.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/8 09:19.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */
public class GestureRelativeLayout extends RelativeLayout implements GestureDetector.OnGestureListener {
    private static final String TAG = "GestureFrameLayout";
    private final static int MIN_MOVE = 100;   //最小距离

    public GestureRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureRelativeLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (e1.getX() - e2.getX() > MIN_MOVE) {
            //从左向右
            Log.d(TAG, "onScroll: ScrollRight");
        } else if (e2.getX() - e1.getX() > MIN_MOVE) {
            //从右向左
            Log.d(TAG, "onScroll: ScrollLeft");
        } else if (e1.getY() - e2.getY() > MIN_MOVE) {
            //从下向上
            Log.d(TAG, "onScroll: ScrollUP");
        } else if (e2.getY() - e1.getY() > MIN_MOVE) {
            //从上向下
            Log.d(TAG, "onScroll: ScrollDown");
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
