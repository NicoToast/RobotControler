package mixturedd.robotcontroler.remoter;

import android.content.Context;
import android.view.View;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * RemoterInfoPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/6 16:49.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class RemoterInfoPresenter implements RemoterContract.InfoPresenter {

    private RemoterContract.InfoView mInfoView;

    @Override
    public void init(RemoterContract.InfoView fragView, View view) {
        this.mInfoView = checkNotNull(fragView, "InfoView cannot be null!");
        this.mInfoView.initView(view);
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

    @Override
    public void postTemperature(String value) {
        mInfoView.showTemperature(value);
    }

    @Override
    public void postGas(String value) {
        mInfoView.showGas(value);
    }

    @Override
    public void postDistance(String value) {
        mInfoView.showDistance(value);
    }

    @Override
    public void postHumidity(String value) {
        mInfoView.showHumidity(value);
    }
}
