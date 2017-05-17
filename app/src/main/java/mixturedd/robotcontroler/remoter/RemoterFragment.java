package mixturedd.robotcontroler.remoter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.unit.ActivityUtils;

public class RemoterFragment extends BaseFragment implements RemoterContract.FragView {
    private static final int HAND_COUNT = 4;
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

    private boolean infoVisible;
    private boolean handVisible;
    private boolean toolbarVisible;

    public static RemoterFragment newInstance() {

        Bundle args = new Bundle();

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
                mPresenter.toggleInfo(infoVisible);
            }
        });
        handFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.toggleHand(handVisible);
            }
        });
    }

    @Override
    public void initView(View view) {
        btUp = (ImageView) view.findViewById(R.id.buttonCtrUp);
        btDown = (ImageView) view.findViewById(R.id.buttonCtrDown);
        btRight = (ImageView) view.findViewById(R.id.buttonCtrRight);
        btLeft = (ImageView) view.findViewById(R.id.buttonCtrLeft);

        //初始化fab
        infoFab = (FloatingActionButton) view.findViewById(R.id.infoFab);
        handFab = (FloatingActionButton) view.findViewById(R.id.handFab);

        //初始化视频模块
        MjpegSurfaceView mMjpegSurfaceView = (MjpegSurfaceView) view.findViewById(R.id.surfaceView);
        //设置SurfaceView
        mPresenter.setSurfaceView(mMjpegSurfaceView);

        initInfoFragment();
        initHandFragment();
        initToolbarFragment();
    }

    private void initInfoFragment() {
        infoFragment = (RemoterInfoFragment) getFragmentManager().findFragmentByTag(infoFragment.getClass().toString());
        if (infoFragment == null) {
            infoFragment = RemoterInfoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), infoFragment, R.id.info_content, infoFragment.getClass().toString());
        }
        infoVisible = true;
    }

    private void initHandFragment() {
        handFragment = (RemoterHandFragment) getFragmentManager().findFragmentByTag(handFragment.getClass().toString());
        if (handFragment == null) {
            handFragment = RemoterHandFragment.newInstance(RemoterHandFragment.MODE_EASY, HAND_COUNT);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), handFragment, R.id.hand_remoter_content, handFragment.getClass().toString());
        }
        RemoterHandPresenter presenter = (RemoterHandPresenter) handFragment.getPresenters()[0];
        presenter.setParentPresenter(mPresenter);
        handVisible = true;
    }

    private void initToolbarFragment() {
        toolbarFragment = (RemoterToolbarFragment) getFragmentManager().findFragmentByTag(toolbarFragment.getClass().toString());
        if (toolbarFragment == null) {
            toolbarFragment = RemoterToolbarFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), toolbarFragment, R.id.toolbar_remoter_content, toolbarFragment.getClass().toString());
        }
        RemoterToolbarPresenter presenter = (RemoterToolbarPresenter) toolbarFragment.getPresenters()[0];
        presenter.setParentPresenter(mPresenter);
        toolbarVisible = true;
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
