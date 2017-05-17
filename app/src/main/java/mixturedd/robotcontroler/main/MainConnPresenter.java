package mixturedd.robotcontroler.main;

import android.content.Context;
import android.view.View;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * MainConnPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/15 21:11.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class MainConnPresenter implements MainContract.ConnPresenter {
    private MainContract.ConnView connViewContract;

    @Override
    public void init(MainContract.ConnView fragView, View view) {
        connViewContract = checkNotNull(fragView, "ConnView cannot be null!");
        ;
        connViewContract.initView(view);
    }

    @Override
    public void enterRemoter() {
        connViewContract.showRemoterUI();
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
