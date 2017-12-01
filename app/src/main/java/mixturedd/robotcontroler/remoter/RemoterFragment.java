package mixturedd.robotcontroler.remoter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import mixturedd.robotcontroler.base.BaseFragment;
import mixturedd.robotcontroler.base.BasePresenter;
import mixturedd.robotcontroler.MyApplication;
import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.model.Config;
import mixturedd.robotcontroler.unit.ActivityUtils;

import static mixturedd.robotcontroler.main.MainActivity.ARGS_CONFIG;

public class RemoterFragment extends BaseFragment implements RemoterContract.FragView {
    private static final int HAND_COUNT = 4;
    public static final int VIEW_VISIBLE_AUTO = 0;
    public static final int VIEW_VISIBLE = 1;
    public static final int VIEW_INVISIBLE = 2;
    private RemoterInfoFragment infoFragment;
    private RemoterHandFragment handFragment;
    private RemoterToolbarFragment toolbarFragment;

    private RemoterFragPresenter mPresenter = new RemoterFragPresenter();
    private ImageView btUp;
    private ImageView btDown;
    private ImageView btRight;
    private ImageView btLeft;
    private FloatingActionButton infoFab;
    private FloatingActionButton handFab;

    private static final float FLOAT_VIEW_MAX_ALPHA = 1.0f;
    private static final float FLOAT_VIEW_MIN_ALPHA = 0.0f;
    private static final int mShortAnimationDuration = 600;
    private GestureDetectorLayout mDetectorView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams param;
    private TextView mArmValue;
    //    private View mFloatView;
    protected static boolean infoVisible;
    protected static boolean handVisible;
    protected static boolean floatViewVisible;
    protected static boolean toolbarVisible;

    private Config mConfig;


    private boolean mDrawOverlaysPermissions;

    public static RemoterFragment newInstance(Config config) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_CONFIG, config);
        RemoterFragment fragment = new RemoterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_remoter;
    }

    @Override
    protected BasePresenter.FragBasePresenter[] getPresenters() {
        return new BasePresenter.FragBasePresenter[]{mPresenter};
    }

    @Override
    protected void onInitPresenters(View view) {
        mPresenter.init(this, view);
        mPresenter.initFragmentsPresenter(infoFragment, handFragment, toolbarFragment);
        mPresenter.setConfig(mConfig);
    }

    @Override
    protected void parseArgumentsFromBundle(Bundle argBundle) {
        mConfig = argBundle.getParcelable(ARGS_CONFIG);
    }

    @Override
    protected void initEvent() {
        btUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPresenter.forward(view, motionEvent);
                return false;
            }
        });
        btDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPresenter.backwards(view, motionEvent);
                return false;
            }
        });
        btRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPresenter.turnRight(view, motionEvent);
                return false;
            }
        });
        btLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPresenter.turnLeft(view, motionEvent);
                return false;
            }
        });
        infoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.toggleInfo(VIEW_VISIBLE_AUTO);
            }
        });
        handFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.toggleHand(VIEW_VISIBLE_AUTO);
            }
        });
        mDetectorView.setOnGestureDetectorListener(new GestureDetectorLayout.OnGestureDetectorListener() {
            @Override
            public void onScrolling(float value, String action) {
                mArmValue.setText(String.format(getString(R.string.unit_ratio), action, (int) value));
            }
        });
        mDetectorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.toggleToolbar(VIEW_VISIBLE_AUTO);
            }
        });
    }

    @Override
    public void initView(View view) {

        //获取移动控制按钮
        btUp = (ImageView) view.findViewById(R.id.buttonCtrUp);
        btDown = (ImageView) view.findViewById(R.id.buttonCtrDown);
        btRight = (ImageView) view.findViewById(R.id.buttonCtrRight);
        btLeft = (ImageView) view.findViewById(R.id.buttonCtrLeft);

        //获取fab
        infoFab = (FloatingActionButton) view.findViewById(R.id.infoFab);
        handFab = (FloatingActionButton) view.findViewById(R.id.handFab);

        mDetectorView = (GestureDetectorLayout) view.findViewById(R.id.gestureDetectorLayout);
        mDetectorView.setPresenter(mPresenter);

        //获取视频模块
        MjpegSurfaceView mMjpegSurfaceView = (MjpegSurfaceView) view.findViewById(R.id.surfaceView);
        //设置SurfaceView
        mPresenter.setSurfaceView(mMjpegSurfaceView);

        //初始化浮动气泡
        mArmValue = (TextView) view.findViewById(R.id.tv_armValue);
        mArmValue.setVisibility(View.GONE);
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(mActivity)) {
            mFloatView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.view_remoter_float, null);
            mArmValue = (TextView) mFloatView.findViewById(R.id.tv_armValue);
            initParams();
            addWindowView2Window();
            hideFloatView();
        }*/
        initInfoFragment();
        initHandFragment();
        initToolbarFragment();
    }

    private void initInfoFragment() {
        infoFragment = (RemoterInfoFragment) getFragmentManager().findFragmentByTag(RemoterInfoFragment.class.toString());
        if (infoFragment == null) {
            infoFragment = RemoterInfoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), infoFragment, R.id.info_content, infoFragment.getClass().toString());
        }
        infoVisible = true;
    }

    private void initHandFragment() {
        handFragment = (RemoterHandFragment) getFragmentManager().findFragmentByTag(RemoterHandFragment.class.toString());
        if (handFragment == null) {
            handFragment = RemoterHandFragment.newInstance(RemoterHandFragment.MODE_EASY, HAND_COUNT);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), handFragment, R.id.hand_remoter_content, handFragment.getClass().toString());
        }
        RemoterHandPresenter presenter = (RemoterHandPresenter) handFragment.getPresenters()[0];
        presenter.setParentPresenter(mPresenter);
        handVisible = true;
    }

    private void initToolbarFragment() {
        toolbarFragment = (RemoterToolbarFragment) getFragmentManager().findFragmentByTag(RemoterToolbarFragment.class.toString());
        if (toolbarFragment == null) {
            toolbarFragment = RemoterToolbarFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), toolbarFragment, R.id.toolbar_remoter_content, toolbarFragment.getClass().toString());
        }
        RemoterToolbarPresenter presenter = (RemoterToolbarPresenter) toolbarFragment.getPresenters()[0];
        presenter.setParentPresenter(mPresenter);
        toolbarVisible = true;
    }

    private void initParams() {

        mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        //设置LayoutParams(全局变量）相关参数
        param = ((MyApplication) mActivity.getApplication()).getLayoutParams();

        param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;     // 系统提示类型,重要
        param.format = PixelFormat.RGBA_8888;
        param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制

//        param.alpha = FLOAT_VIEW_TO_ALPHA;

        param.gravity = Gravity.CENTER;   //调整悬浮窗口至中央

        param.x = 80;
        param.y = 0;

        //设置悬浮窗口长宽数据
        param.width = 360;
        param.height = 250;
    }

    private void addWindowView2Window() {
//        mWindowManager.addView(mFloatView, param);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDrawOverlaysPermissions =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(mActivity);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mWindowManager.removeViewImmediate(mFloatView);
        mDrawOverlaysPermissions = false;
    }

    @Override
    public void hideFloatView() {
        floatViewVisible = false;
        mArmValue.setAlpha(FLOAT_VIEW_MAX_ALPHA);
        mArmValue.setVisibility(View.VISIBLE);

        mArmValue.animate()
                .alpha(FLOAT_VIEW_MIN_ALPHA)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mArmValue.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void showFloatView() {
        floatViewVisible = true;
        mArmValue.setAlpha(FLOAT_VIEW_MIN_ALPHA);
        mArmValue.setVisibility(View.VISIBLE);

        mArmValue.animate()
                .alpha(FLOAT_VIEW_MAX_ALPHA)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

    }

    @Override
    public void hideInfo() {
        ActivityUtils.hideFragment(getFragmentManager(), infoFragment);
        infoVisible = false;
    }

    @Override
    public void showInfo() {
        ActivityUtils.showFragment(getFragmentManager(), infoFragment);
        infoVisible = true;
    }

    @Override
    public void hideHand() {
        ActivityUtils.hideFragment(getFragmentManager(), handFragment);
        handVisible = false;
    }

    @Override
    public void showHand() {
        ActivityUtils.showFragment(getFragmentManager(), handFragment);
        handVisible = true;
    }

    @Override
    public void hideToolbar() {
        ActivityUtils.hideFragment(getFragmentManager(), toolbarFragment);
        toolbarVisible = false;
    }

    @Override
    public void showToolbar() {
        ActivityUtils.showFragment(getFragmentManager(), toolbarFragment);
        toolbarVisible = true;
    }

    @Override
    public void onMoveForward(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onMoveBackwards(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onMoveRight(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onMoveLeft(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onMoveStop() {

    }
}
