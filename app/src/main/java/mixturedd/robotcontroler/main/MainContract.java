package mixturedd.robotcontroler.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import mixturedd.robotcontroler.BaseActivity;
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

        void showFailMsg(int resId);

        void showSuccessMsg(int resId);

        void showRemoterUI();

        void showSettingsUI();

        void showMsgDialog(final String title, String msg, DialogInterface.OnClickListener positive,
                           DialogInterface.OnClickListener negative);
    }

    interface Presenter extends BasePresenter.ActBasePresenter<View> {
        void checkConnection(Context context);

        void enterRemoter();

        void enterSettings();

        void requestOverlayPermission(Context context);

        void requestStoragePermission(BaseActivity baseActivity);

        void onActivityResult(int requestCode, int resultCode, Intent data, Context context);

        void crateMsgDialog(final String title, String msg, DialogInterface.OnClickListener positive,
                           DialogInterface.OnClickListener negative);
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
