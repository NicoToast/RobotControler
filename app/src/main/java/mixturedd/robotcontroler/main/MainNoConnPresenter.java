package mixturedd.robotcontroler.main;

import android.content.Context;
import android.view.View;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * MainNoConnPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/15 21:14.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class MainNoConnPresenter implements MainContract.NoConnPresenter {

    private MainContract.NoConnView noConnViewContract;
    private MainContract.Presenter mParentPresenter;

    @Override
    public void init(MainContract.NoConnView fragView, View view) {
        noConnViewContract = checkNotNull(fragView, "NoConnView cannot be null!");
        noConnViewContract.initView(view);
    }

    public void setParentPresenter(MainContract.Presenter presenter) {
        mParentPresenter = checkNotNull(presenter, "NoConnView cannot be null!");
    }

    @Override
    public void refresh() {
        mParentPresenter.checkConnection();
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
