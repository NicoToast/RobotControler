package mixturedd.robotcontroler.remoter;

import android.content.Context;
import android.view.View;

import static com.google.common.base.Preconditions.checkNotNull;
import static mixturedd.robotcontroler.remoter.RemoterFragment.VIEW_VISIBLE;

/**
 * RemoterToolbarPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/10 19:45.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class RemoterToolbarPresenter implements RemoterContract.ToolbarPresenter {

    private RemoterContract.ToolbarView mToolbarView;
    private RemoterContract.FragPresenter mParentPresenter;

    @Override
    public void init(RemoterContract.ToolbarView fragView, View view) {
        this.mToolbarView = checkNotNull(fragView, "ToolbarView cannot be null!");
        this.mToolbarView.initView(view);
    }

    public void setParentPresenter(RemoterContract.FragPresenter presenter) {
        mParentPresenter = presenter;
    }

    @Override
    public void setStatePlay() {
        mParentPresenter.startMjpeg();
        mToolbarView.onVideoPlay();
    }

    @Override
    public void setStatePause() {
        mParentPresenter.stopMjpeg();
        mToolbarView.onVideoPause();
    }

    @Override
    public void sendMsg(String msg, int msgType) {
        mToolbarView.showMag(msg, msgType);
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
