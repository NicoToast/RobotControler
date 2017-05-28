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
    private MainPresenter mPresenter = new MainPresenter();

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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SEVER_IP = settings.getString(getString(R.string.pref_key_server_ip), "192.168.1.1");
        SEVER_URL = "http://" + SEVER_IP;
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
        startActivity(new Intent(this, RemoterActivity.class));
    }

    @Override
    public void showSettingsUI() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ControlCode.SEVER_IP = sharedPreferences.getString(key, "192.168.1.1");
        SEVER_URL = "http://" + SEVER_IP;
        Log.d(TAG, "onSharedPreferenceChanged:" + ControlCode.SEVER_URL);
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
