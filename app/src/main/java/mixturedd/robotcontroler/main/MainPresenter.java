package mixturedd.robotcontroler.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.google.common.collect.Interner;

import mixturedd.robotcontroler.BaseActivity;
import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.model.Config;
import mixturedd.robotcontroler.model.ServerConfig;
import mixturedd.robotcontroler.model.VideoConfig;
import mixturedd.robotcontroler.unit.StateCheck;
import pub.devrel.easypermissions.EasyPermissions;

import static com.google.common.base.Preconditions.checkNotNull;
import static mixturedd.robotcontroler.remoter.ControlCode.SEVER_URL;

/**
 * MainPresenter.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/2/28 22:41.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */
public class MainPresenter implements MainContract.Presenter {
    //    private static final String TAG = "MainPresenter";
    private static final int RC_OVERLAY = 1001;
    private static final int RC_STORAGE_WRITE_AND_READ = 1002;
    private MainContract.View viewContract;

/*    public MainPresenter(@NonNull MainContract.View view, MainContract.ConnView connView,
                         MainContract.NoConnView noConnView, Context context) {
        viewContract = view;
        connViewContract = connView;
        noConnViewContract = noConnView;
        mContext = context;
    }*/

    @Override
    public void init(MainContract.View actView) {
        viewContract = checkNotNull(actView, "actView cannot be null!");
        viewContract.initView();
    }

    @Override
    public void enterRemoter() {
        viewContract.showRemoterUI();
    }

    @Override
    public void enterSettings() {
        viewContract.showSettingsUI();
    }

    @Override
    public void checkConnection(Context context) {
        new checkConnectionTask(context).execute();
    }


    private class checkConnectionTask extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;

        public checkConnectionTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return StateCheck.isConnectRobot(mContext, SEVER_URL);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
//                Toast.makeText(mContext, mContext.getString(R.string.state_good), Toast.LENGTH_SHORT).show();
                viewContract.showSuccessMsg(R.string.state_good);
//                viewContract.showConnUI();
//                viewContract.hideCheckingUI();
            } else {
                viewContract.showFailMsg(R.string.state_error);
//                Toast.makeText(mContext, mContext.getString(R.string.state_error), Toast.LENGTH_SHORT).show();
//                viewContract.showNoConnUI();
//                viewContract.hideCheckingUI();
            }
        }
    }

    @Override
    public void requestOverlayPermission(Context context) {
        final Context ctx = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 判断是否有SYSTEM_ALERT_WINDOW权限
            if (!Settings.canDrawOverlays(ctx)) {
                viewContract.showMsgDialog("权限申请", "使用前需要授予\"在其他应用之上显示内容\"的权限，授予后即将会提升用户体验。",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 申请SYSTEM_ALERT_WINDOW权限
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + ctx.getPackageName()));
                                // REQUEST_CODE2是本次申请的请求码
                                ((BaseActivity) ctx).startActivityForResult(intent, RC_OVERLAY);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewContract.showMsgDialog("权限申请", "权限没授予成功，有些功能将无法正常使用。",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }, null);
                            }
                        });

            }
        }
    }

    @Override
    public void requestStoragePermission(BaseActivity activity) {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            EasyPermissions.requestPermissions(activity, "需要获取一些权限来保证程序的正常运行",
                    RC_STORAGE_WRITE_AND_READ, perms);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
        if (requestCode == RC_OVERLAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 判断是否有SYSTEM_ALERT_WINDOW权限
                if (Settings.canDrawOverlays(context)) {

                }
            }
        }
    }

    @Override
    public void crateMsgDialog(String title, String msg, DialogInterface.OnClickListener positive,
                               DialogInterface.OnClickListener negative) {
        viewContract.showMsgDialog(title, msg, positive, negative);
    }

    @Override
    public void loadSettings(Context context, Config config) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        config.setServerConfig(ServerConfig.newInstance(
                settings.getString(
                        context.getString(R.string.pref_key_server_ip),
                        context.getString(R.string.pref_default_server_ip)),
                Integer.parseInt(settings.getString(
                        context.getString(R.string.pref_key_server_port),
                        context.getString(R.string.pref_default_server_port))
                )
        ));
        config.setVideoConfig(VideoConfig.newInstance(
                settings.getString(
                        context.getString(R.string.pref_key_video_ip),
                        context.getString(R.string.pref_default_server_ip)
                ),
                Integer.parseInt(settings.getString(
                        context.getString(R.string.pref_key_video_port),
                        context.getString(R.string.pref_default_video_port)
                )),
                Integer.parseInt(settings.getString(
                        context.getString(R.string.pref_key_video_action),
                        context.getString(R.string.pref_default_video_action))
                )
        ));
    }

    @Override
    public void updateSettings(Context context, Config config) {

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
