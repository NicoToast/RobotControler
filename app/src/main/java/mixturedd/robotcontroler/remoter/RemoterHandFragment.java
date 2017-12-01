package mixturedd.robotcontroler.remoter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;

import mixturedd.robotcontroler.base.BaseFragment;
import mixturedd.robotcontroler.base.BasePresenter;
import mixturedd.robotcontroler.R;

/**
 * RemoterHandFragment.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/11 11:50.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class RemoterHandFragment extends BaseFragment implements RemoterContract.HandView {
    private static final String TAG = "RemoterHandFragment";
    private static final String ARG_HAND_COUNT = "HAND_COUNT";
    private static final String ARG_HAND_MODE = "HAND_MODE";
    public static final String MODE_EASY = "EASY";
    public static final String MODE_ADVANCE = "ADVANCE";

    private ImageView btUp;
    private ImageView btDown;
    private ImageView btRight;
    private ImageView btLeft;
    private ImageButton btLoosen;
    private ImageButton btTighten;

    private int mHandCount;
    private String mHandMode;
    private RemoterHandPresenter mPresenter = new RemoterHandPresenter();

    public static RemoterHandFragment newInstance(String mode, int handCount) {
        RemoterHandFragment fragment = new RemoterHandFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_HAND_COUNT, handCount);
        args.putString(ARG_HAND_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        switch (mHandMode) {
            case MODE_EASY:
                return R.layout.frag_remoter_hand_easy_mode;
            case MODE_ADVANCE:
                return R.layout.frag_remoter_hand_adv_mode;
            default:
                return R.layout.frag_remoter_hand_easy_mode;
        }
    }

    @Override
    protected BasePresenter.FragBasePresenter[] getPresenters() {
        return new BasePresenter.FragBasePresenter[]{mPresenter};
    }

    @Override
    protected void onInitPresenters(View view) {
        mPresenter.init(this, view);
    }

    @Override
    protected void parseArgumentsFromBundle(Bundle argBundle) {
        super.parseArgumentsFromBundle(argBundle);
        mHandCount = argBundle.getInt(ARG_HAND_COUNT);
        mHandMode = argBundle.getString(ARG_HAND_MODE);
    }

    @Override
    public void initView(View view) {
        switch (mHandMode) {
            case MODE_EASY:

                btUp = (ImageView) view.findViewById(R.id.bt_arm_up);
                btDown = (ImageView) view.findViewById(R.id.bt_arm_down);
                btRight = (ImageView) view.findViewById(R.id.bt_arm_right);
                btLeft = (ImageView) view.findViewById(R.id.bt_arm_left);
                btLoosen = (ImageButton) view.findViewById(R.id.bt_arm_loosen);
                btTighten = (ImageButton) view.findViewById(R.id.bt_arm_tighten);

                break;
            case MODE_ADVANCE:
                if (view instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    recyclerView.setAdapter(new HandRemoterAdapter(mHandCount));
                }
                break;
        }
    }

    @Override
    protected void initEvent() {
        switch (mHandMode) {
            case MODE_EASY:
                btUp.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mPresenter.armForward(view, motionEvent);
                        return false;
                    }
                });
                btDown.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mPresenter.armBackwards(view, motionEvent);
                        return false;
                    }
                });
                btRight.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mPresenter.armTurnRight(view, motionEvent);
                        return false;
                    }
                });
                btLeft.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mPresenter.armTurnLeft(view, motionEvent);
                        return false;
                    }
                });
                btTighten.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mPresenter.armTighten(v, event);
                        return false;
                    }
                });
                btLoosen.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mPresenter.armLoosen(v, event);
                        return false;
                    }
                });
                break;
            case MODE_ADVANCE:
                break;
        }
    }

    @Override
    public void onArmForward(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onArmBackwards(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onArmTurnRight(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onArmTurnLeft(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onArmLoosen(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    @Override
    public void onArmTighten(View view, MotionEvent motionEvent) {

    }

    private class HandRemoterAdapter extends RecyclerView.Adapter<HandRemoterAdapter.ViewHolder> {
        private int count;

        private HandRemoterAdapter(int count) {
            this.count = count;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_remoter_hand_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return count;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private int handMun;
            private VerticalSeekBar mSeekBar;
            private TextView mHandNum;

            private ViewHolder(View view) {
                super(view);
                mHandNum = (TextView) view.findViewById(R.id.handNum);
                mSeekBar = (VerticalSeekBar) view.findViewById(R.id.verticalSeekBar);
                mSeekBar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
                    private int angle;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        angle = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mPresenter.setArmAngle(angle, handMun);
                    }
                });
            }

            private void bind(int position) {
                handMun = position;
                if (position == 0) {
                    mHandNum.setText("云台垂直方向");
                } else {
                    mHandNum.setText("" + handMun + "号舵机");
                }
            }
        }
    }
}
