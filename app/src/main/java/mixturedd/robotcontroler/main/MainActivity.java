package mixturedd.robotcontroler.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mixturedd.robotcontroler.BaseActivity;
import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.model.Config;
import mixturedd.robotcontroler.remoter.ControlCode;
import mixturedd.robotcontroler.remoter.RemoterActivity;
import mixturedd.robotcontroler.settings.SettingsActivity;
import mixturedd.robotcontroler.unit.ActivityUtils;
import pub.devrel.easypermissions.EasyPermissions;

import static mixturedd.robotcontroler.remoter.ControlCode.SEVER_IP;
import static mixturedd.robotcontroler.remoter.ControlCode.SEVER_URL;


public class MainActivity extends BaseActivity implements MainContract.View,
        SharedPreferences.OnSharedPreferenceChangeListener,
        EasyPermissions.PermissionCallbacks {
    private static final String TAG = "MainActivity";
    public static final String ARGS_CONFIG = "CONFIG";
    private MainPresenter mPresenter = new MainPresenter();
    private Config mConfig = new Config();
    private FloatingActionButton mFab;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_main;
    }

    @Override
    protected BasePresenter.ActBasePresenter[] getPresenters() {
        return new BasePresenter.ActBasePresenter[]{mPresenter};
    }

    @Override
    protected void onInitPresenters() {
        mPresenter.init(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPresenter.checkConnection(this);
        mPresenter.requestStoragePermission(this);
    }

    @Override
    protected void parsePreference() {
        super.parsePreference();
        mPresenter.loadSettings(this, mConfig);
    }

    @Override
    public void initView() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_main_setting);
    }

    @Override
    protected void initEvent() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.enterSettings();
            }
        });
    }

    @Override
    public void showMsgDialog(final String title, String msg, DialogInterface.OnClickListener positive,
                              DialogInterface.OnClickListener negative) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg);
        if (positive != null) {
            builder.setPositiveButton("确定", positive);
        }
        if (negative != null) {
            builder.setNegativeButton("取消", negative);
        }
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void showSuccessMsg(int resId) {
        Snackbar.make(mFab, getString(resId), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.main_enter, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.enterRemoter();
                    }
                })
                .setActionTextColor(Color.rgb(76, 175, 80))
                .show();
    }

    @Override
    public void showFailMsg(int resId) {
        Snackbar.make(mFab, getString(resId), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.main_refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.checkConnection(MainActivity.this);
                    }
                })
                .setActionTextColor(Color.rgb(244, 67, 54))
                .show();
    }


    @Override
    public void showRemoterUI() {
        Intent intent = new Intent(this, RemoterActivity.class);
        intent.putExtra(ARGS_CONFIG, mConfig);
        startActivity(intent);
    }

    @Override
    public void showSettingsUI() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // TODO: 2017/6/2  onSharedPreferenceChanged
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data, this);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        mPresenter.requestOverlayPermission(this);

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        setResult(RESULT_OK);
        finish();
    }
}
