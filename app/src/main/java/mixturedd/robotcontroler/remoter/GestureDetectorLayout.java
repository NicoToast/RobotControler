package mixturedd.robotcontroler.remoter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import mixturedd.robotcontroler.MyApplication;

import static com.google.common.base.Preconditions.checkNotNull;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HEAD;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HEAD_VERTICAL;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_SAE;
import static mixturedd.robotcontroler.remoter.RemoterFragment.VIEW_INVISIBLE;
import static mixturedd.robotcontroler.remoter.RemoterFragment.VIEW_VISIBLE;
import static mixturedd.robotcontroler.remoter.RemoterFragment.floatViewVisible;

/**
 * GestureFrameLayout.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/8 09:19.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */
public class GestureDetectorLayout extends RelativeLayout {
    private static final String TAG = "GestureFrameLayout";
    private static final String COMM_HEAD = "FF0103%sFF";
    private static final int ACTION_MOVE_NONE = 0;
    private static final int ACTION_TOUCH_UP = 1;
    private static final int ACTION_TOUCH_DOWN = 2;
    private static final int ACTION_TOUCH_RIGHT = 3;
    private static final int ACTION_TOUCH_LEFT = 4;
    private final static int MIN_MOVE = 10;   //最小距离
    private final static int HEAD_Y_MAX = 175;
    private final static int HEAD_Y_MIN = 75;
    private final static int HEAD_Y_INITIAL = 100;
    private final static int HEAD_ANGLE_RATIO = 10;
    private int HEAD_Y_CURRENT;
    private OnGestureDetectorListener mListener;
    private float mTouchStartX;
    private float mTouchStartY;

    private RemoterContract.FragPresenter mPresenter;

    public GestureDetectorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureDetectorLayout(Context context) {
        super(context);
    }

    protected void setPresenter(RemoterContract.FragPresenter fragPresenter) {
        mPresenter = checkNotNull(fragPresenter, "fragPresenter cannot be null!");
        HEAD_Y_CURRENT = HEAD_Y_INITIAL;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchEndX = event.getX();
        float touchEndY = event.getY();
        float valueX;
        float valueY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //获取到距离差
                valueX = (touchEndX - mTouchStartX);
                valueY = (touchEndY - mTouchStartY);
                //防止是按下也判断
                if (Math.abs(valueX) > MIN_MOVE && Math.abs(valueY) > MIN_MOVE) {
                    //通过距离差判断方向
                    int orientation = getOrientation(valueX, valueY);

                    if (!floatViewVisible) {
                        //显示FloatView
                        mPresenter.toggleFloatView(VIEW_VISIBLE);
                    }

                    float absAngleX = (Math.abs(valueX / HEAD_ANGLE_RATIO));
                    float absAngleY = (Math.abs(valueY / HEAD_ANGLE_RATIO));

                    switch (orientation) {
                        case ACTION_TOUCH_RIGHT:
                            updateFloatView(absAngleX, "云台右转");
                            break;
                        case ACTION_TOUCH_LEFT:
                            updateFloatView(absAngleX, "云台左转");
                            break;
                        case ACTION_TOUCH_UP:
                            updateFloatView(absAngleY, "云台上转");
                            break;
                        case ACTION_TOUCH_DOWN:
                            updateFloatView(absAngleY, "云台下转");
                            break;
                    }
                } else {
                    if (floatViewVisible) {
                        mPresenter.toggleFloatView(VIEW_INVISIBLE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                valueX = (touchEndX - mTouchStartX);
                valueY = (touchEndY - mTouchStartY);
                if (Math.abs(valueX) > MIN_MOVE && Math.abs(valueY) > MIN_MOVE) {
                    //通过距离差判断方向
                    int orientation = getOrientation(valueX, valueY);

                    float angleX = valueX / HEAD_ANGLE_RATIO;
                    float angleY = valueY / HEAD_ANGLE_RATIO;

                    String headOrder = COMM_HEAD;
                    switch (orientation) {
                        case ACTION_TOUCH_RIGHT:
                            break;
                        case ACTION_TOUCH_LEFT:
                            break;
                        case ACTION_TOUCH_UP:
                        case ACTION_TOUCH_DOWN:
                            headOrder = String.format(headOrder, getHeadAngleHax(angleFilter(HEAD_Y_CURRENT - angleY)));
                            break;
                    }
                    mPresenter.orderSendBySocket(headOrder);
                    if (floatViewVisible) {
                        //隐藏FloatView
                        mPresenter.toggleFloatView(VIEW_INVISIBLE);
                    }
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? ACTION_TOUCH_RIGHT : ACTION_TOUCH_LEFT;
        } else if (Math.abs(dx) < Math.abs(dy)) {
            //Y轴移动
            return dy > 0 ? ACTION_TOUCH_DOWN : ACTION_TOUCH_UP;
        } else {
            return ACTION_MOVE_NONE;
        }
    }

    /**
     * 角度过滤器
     * <p>
     * 由于舵机最大最小角度的影响需要过滤角度
     *
     * @param value 距离差的绝对值
     * @return 角度值
     */
    private float angleFilter(float value) {
        if (value >= HEAD_Y_MAX) {
            HEAD_Y_CURRENT = HEAD_Y_MAX;

        } else if (value <= HEAD_Y_MIN) {
            HEAD_Y_CURRENT = HEAD_Y_MIN;

        } else {
            HEAD_Y_CURRENT = (int) value;
        }
        return HEAD_Y_CURRENT;
    }

    /**
     * 角度转16进制
     *
     * @param value 距离差的绝对值
     * @return 角度值
     */
    private String getHeadAngleHax(float value) {
        int angle = (int) (value);
        if (angle >= HEAD_Y_MAX) {
            return Integer.toHexString(HEAD_Y_MAX);
        } else if (angle <= HEAD_Y_MIN) {
            return Integer.toHexString(HEAD_Y_MIN);
        }
        if (angle < 10) {
            return "0" + Integer.toHexString(angle);
        }
        return Integer.toHexString(angle);
    }

    private void updateFloatView(float value, String action) {
        mListener.onScrolling(value, action);
    }


    public void setOnGestureDetectorListener(OnGestureDetectorListener mListener) {
        this.mListener = mListener;
    }

    interface OnGestureDetectorListener {
        void onScrolling(float value, String action);
    }
}
