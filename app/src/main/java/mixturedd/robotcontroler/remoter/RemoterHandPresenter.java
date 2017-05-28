package mixturedd.robotcontroler.remoter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import static com.google.common.base.Preconditions.checkNotNull;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HAND;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HAND_FIVE;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HAND_FOUR;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HAND_INITIALIZATION;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HAND_ONE;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HAND_THREE;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HAND_TOW;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HEAD;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_HEAD_VERTICAL;
import static mixturedd.robotcontroler.remoter.ControlCode.ORDER_SAE;

/**
 * RemoterHandPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/12 17:39.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class RemoterHandPresenter implements RemoterContract.HandPresenter {
    private static final String COMM_ARM_FORWARD = "FF020300FF";
    private static final String COMM_ARM_BACKWARDS = "FF020400FF";
    private static final String COMM_ARM_RIGHT = "FF020200FF";
    private static final String COMM_ARM_LEFT = "FF020100FF";
    private static final String COMM_ARM_LOOSEN = "FF020600FF";
    private static final String COMM_ARM_TIGHTEN = "FF020500FF";

    private RemoterContract.HandView mHandView;
    private RemoterContract.FragPresenter mParentPresenter;

    @Override
    public void init(RemoterContract.HandView fragView, View view) {
        this.mHandView = checkNotNull(fragView, "HandView cannot be null!");
        this.mHandView.initView(view);
    }

    public void setParentPresenter(RemoterContract.FragPresenter presenter) {
        mParentPresenter = presenter;
    }

    @Override
    public void armForward(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mParentPresenter.orderSendBySocket(COMM_ARM_FORWARD);
                break;
            case MotionEvent.ACTION_UP:
                mParentPresenter.stop();
                break;
        }
    }

    @Override
    public void armBackwards(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mParentPresenter.orderSendBySocket(COMM_ARM_BACKWARDS);
                break;
            case MotionEvent.ACTION_UP:
                mParentPresenter.stop();
                break;
        }
    }

    @Override
    public void armTurnRight(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mParentPresenter.orderSendBySocket(COMM_ARM_RIGHT);
                break;
            case MotionEvent.ACTION_UP:
                mParentPresenter.stop();
                break;
        }
    }

    @Override
    public void armTurnLeft(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mParentPresenter.orderSendBySocket(COMM_ARM_LEFT);
                break;
            case MotionEvent.ACTION_UP:
                mParentPresenter.stop();
                break;
        }
    }

    @Override
    public void armLoosen(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mHandView.onArmLoosen(view, motionEvent);
                mParentPresenter.orderSendBySocket(COMM_ARM_LOOSEN);
                break;
            case MotionEvent.ACTION_UP:
                mParentPresenter.stop();
                break;
        }

    }

    @Override
    public void armTighten(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mHandView.onArmTighten(view, motionEvent);
                mParentPresenter.orderSendBySocket(COMM_ARM_TIGHTEN);
                break;
            case MotionEvent.ACTION_UP:
                mParentPresenter.stop();
                break;
        }
    }

    @Override
    public void setArmAngle(int angle, int position) {
        String order = ORDER_SAE + ORDER_HAND;
        String a = Integer.toHexString(angle);
        if (angle < 16) {
            a = "0" + angle;
        }
        switch (position) {
            case 0:
                if (angle >= 130) {
                    a = "82";
                } else if (angle <= 70) {
                    a = "46";
                }
                order = ORDER_SAE + ORDER_HEAD + ORDER_HEAD_VERTICAL + a + ORDER_SAE;
                break;
            case 1:
                order = order + ORDER_HAND_ONE + a + ORDER_SAE;
                break;
            case 2:
                order = order + ORDER_HAND_TOW + a + ORDER_SAE;
                break;
            case 3:
                if (angle >= 180) {
                    a = "84";
                } else if (angle <= 120) {
                    a = "78";
                }
                order = order + ORDER_HAND_THREE + a + ORDER_SAE;
                break;
            case 4:
                order = order + ORDER_HAND_FOUR + a + ORDER_SAE;
                break;
            case 5:
                order = order + ORDER_HAND_FIVE + a + ORDER_SAE;
                break;
            default:
                order = ORDER_HAND_INITIALIZATION;
        }
        mParentPresenter.orderSendBySocket(order);
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
