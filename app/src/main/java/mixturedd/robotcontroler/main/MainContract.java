package mixturedd.robotcontroler.main;

import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.BaseView;

/**
 * MainContract.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/2/28 17:13.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public interface MainContract {
    interface View extends BaseView.ActBaseView {

        void hideCheckingUI();

        void showConnUI();

        void showNoConnUI();
    }

    interface Presenter extends BasePresenter.ActBasePresenter<View> {
        void checkConnection();
    }

    interface NoConnView extends BaseView.FragBaseView {
        void animatedWifi();
    }

    interface NoConnPresenter extends BasePresenter.FragBasePresenter<NoConnView> {
        void refresh();
    }

    interface ConnView extends BaseView.FragBaseView {
        void showRemoterUI();
    }

    interface ConnPresenter extends BasePresenter.FragBasePresenter<ConnView> {
        void enterRemoter();
    }
}
