package mixturedd.robotcontroler.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import mixturedd.robotcontroler.base.BaseActivity;
import mixturedd.robotcontroler.base.BasePresenter;
import mixturedd.robotcontroler.base.BaseView;
import mixturedd.robotcontroler.model.Config;

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

        void loadSettings(Context context, Config config);

        void updateSettings(Context context, Config config);
    }


}
