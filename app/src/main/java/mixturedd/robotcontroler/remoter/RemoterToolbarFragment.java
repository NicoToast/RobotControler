package mixturedd.robotcontroler.remoter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;

/**
 * RemoterToolbarFragment.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/14 16:18.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class RemoterToolbarFragment extends BaseFragment implements RemoterContract.ToolbarView {
    private static final int[] STATE_SET_PLAY =
            {R.attr.state_play, -R.attr.state_pause, -R.attr.state_stop};
    private static final int[] STATE_SET_PAUSE =
            {-R.attr.state_play, R.attr.state_pause, -R.attr.state_stop};
    private static final int[] STATE_SET_STOP =
            {-R.attr.state_play, -R.attr.state_pause, R.attr.state_stop};
    private boolean playState = false;
    private RemoterToolbarPresenter mPresenter = new RemoterToolbarPresenter();
    private ImageButton videoButton;
    private TextView videoState;

    public static RemoterToolbarFragment newInstance() {

        Bundle args = new Bundle();

        RemoterToolbarFragment fragment = new RemoterToolbarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_remoter_toolbar;
    }

    @Override
    protected void onInitPresenters(View view) {
        mPresenter.init(this, view);
    }

    @Override
    protected BasePresenter.FragBasePresenter[] getPresenters() {
        return new BasePresenter.FragBasePresenter[0];
    }

    @Override
    protected void initEvent() {
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playState) {
                    mPresenter.setStatePlay();
                } else {
                    mPresenter.setStatePause();
                }
            }
        });
    }

    public void initView(View view) {
        videoButton = (ImageButton) view.findViewById(R.id.videoButton);
        videoState = (TextView) view.findViewById(R.id.videoState);
    }

    @Override
    public void onPlayBt() {
        videoButton.setImageState(STATE_SET_PAUSE, true);
        videoState.setText(R.string.remoter_toolbar_pause);
        playState = false;
    }

    @Override
    public void onPauseBt() {
        videoButton.setImageState(STATE_SET_PLAY, true);
        videoState.setText(R.string.remoter_toolbar_play);
        playState = true;
    }

}
