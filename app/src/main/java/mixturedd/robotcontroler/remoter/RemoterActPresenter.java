package mixturedd.robotcontroler.remoter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * RemoterActPresenter.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/4/24 16:31.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class RemoterActPresenter implements RemoterContract.ActPresenter {

    private RemoterContract.ActView actView;

    @Override
    public void init(RemoterContract.ActView actView) {
        this.actView = checkNotNull(actView, "actView cannot be null!");
        this.actView.initView();
    }

    @Override
    public void toggleNavigationAndStatus(boolean visible) {
        if (visible){
            actView.hideNavigationAndStatus();
        }else {
            actView.showNavigationAndStatus();
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

}
