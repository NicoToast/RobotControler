package mixturedd.robotcontroler.remoter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import mixturedd.robotcontroler.BaseActivity;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.model.Config;
import mixturedd.robotcontroler.unit.ActivityUtils;

import static mixturedd.robotcontroler.main.MainActivity.ARGS_CONFIG;

public class RemoterActivity extends BaseActivity implements RemoterContract.ActView {
    private static final String TAG = "RemoterActivity";

    private RemoterActPresenter mPresenter = new RemoterActPresenter();
    private View contentView;
    private Config mConfig;

    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {

        }
    };
    private boolean mVisible = false;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideNavigationAndStatus();
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.act_remoter;
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
    protected void parseArgumentsFromIntent(Intent argIntent) {
        mConfig = argIntent.getExtras().getParcelable(ARGS_CONFIG);
    }

    @Override
    protected void initEvent() {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.toggleNavigationAndStatus(mVisible);
            }
        });
    }

    @Override
    public void initView() {
        contentView = findViewById(R.id.remoterContentFrame);

        loadMainFragment();
    }

    @Override
    public void hideNavigationAndStatus() {
        mVisible = false;
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable,
                getResources().getInteger(R.integer.UI_ANIMATION_DELAY));
    }

    @Override
    public void showNavigationAndStatus() {
        mVisible = true;
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable,
                getResources().getInteger(R.integer.UI_ANIMATION_DELAY));
    }

    private void loadMainFragment() {

        RemoterFragment remoterFragment =
                (RemoterFragment) getSupportFragmentManager().findFragmentById(R.id.remoterContentFrame);
        if (remoterFragment == null) {
            // Create the fragment
            remoterFragment = RemoterFragment.newInstance(mConfig);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), remoterFragment, R.id.remoterContentFrame, remoterFragment.getClass().getName());
        }
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stopSocketClient();
    }*/

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            mPresenter.toggleNavigationAndStatus(mVisible);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
