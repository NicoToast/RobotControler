package mixturedd.robotcontroler.remoter;

import android.view.MotionEvent;
import android.view.View;

import mixturedd.robotcontroler.BaseActivity;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.BaseView;

/**
 * Created by hp on 2016/11/21 17:59.
 */

public interface RemoterContract {

    interface ActView extends BaseView.ActBaseView {
        void hideNavigationAndStatus();
        void showNavigationAndStatus();
    }

    interface ActPresenter extends BasePresenter.ActBasePresenter<ActView> {
        void toggleNavigationAndStatus(boolean visible);
    }

    interface FragView extends BaseView.FragBaseView {
        void hideToolbar();
        void showToolbar();
        void hideInfo();
        void showInfo();
        void hideHand();
        void showHand();

        void hideFloatView();
        void showFloatView();

        /**
         * 前进按钮
         * @param view
         * @param motionEvent
         */
        void onMoveForward(View view, MotionEvent motionEvent);

        /**
         * 向后
         */
        void onMoveBackwards(View view, MotionEvent motionEvent);

        /**
         * 向右
         */
        void onMoveRight(View view, MotionEvent motionEvent);

        /**
         * 向左
         */
        void onMoveLeft(View view, MotionEvent motionEvent);

        /**
         * 停止
         */
        void onMoveStop();
    }

    interface FragPresenter extends BasePresenter.FragBasePresenter<FragView> {
        BaseActivity getActivity();
        void toggleToolbar(int visible);
        void toggleInfo(int visible);
        void toggleHand(int visible);
        void toggleFloatView(int visible);
        /**
         * 向前
         */
        void forward(View view, MotionEvent motionEvent);

        /**
         * 向后
         */
        void backwards(View view, MotionEvent motionEvent);

        /**
         * 向右
         */
        void turnRight(View view, MotionEvent motionEvent);

        /**
         * 向左
         */
        void turnLeft(View view, MotionEvent motionEvent);

        /**
         * 停止
         */
        void stop();

        void startMjpeg();

        void stopMjpeg();

        void orderSendBySocket(String order);

        void refreshInputStream();
    }

    interface InfoView extends BaseView.FragBaseView {
        void showTemperature(String value);

        void showGas(String value);

        void showDistance(String value);

        void showHumidity(String value);
    }

    interface InfoPresenter extends BasePresenter.FragBasePresenter<InfoView> {
        void postTemperature(String value);

        void postGas(String value);

        void postDistance(String value);

        void postHumidity(String value);
    }

    interface ToolbarView extends BaseView.FragBaseView{
        void onVideoPlay();

        void onVideoPause();
    }

    interface ToolbarPresenter extends BasePresenter.FragBasePresenter<ToolbarView> {
        void setStatePlay();

        void setStatePause();
    }

    interface HandView extends BaseView.FragBaseView{
        /**
         * 机械手前进按钮
         * @param view
         * @param motionEvent
         */
        void onArmForward(View view, MotionEvent motionEvent);

        /**
         * 向后
         */
        void onArmBackwards(View view, MotionEvent motionEvent);

        /**
         * 向右
         */
        void onArmTurnRight(View view, MotionEvent motionEvent);

        /**
         * 向左
         */
        void onArmTurnLeft(View view, MotionEvent motionEvent);

        void onArmLoosen(View view, MotionEvent motionEvent);

        void onArmTighten(View view, MotionEvent motionEvent);

    }

    interface HandPresenter extends BasePresenter.FragBasePresenter<HandView> {
        void armForward(View view, MotionEvent motionEvent);

        void armBackwards(View view, MotionEvent motionEvent);

        void armTurnRight(View view, MotionEvent motionEvent);

        void armTurnLeft(View view, MotionEvent motionEvent);

        void armLoosen(View view, MotionEvent motionEvent);

        void armTighten(View view, MotionEvent motionEvent);

        void setArmAngle(int angle, int position);

    }
}
