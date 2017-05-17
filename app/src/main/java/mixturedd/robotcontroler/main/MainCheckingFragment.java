package mixturedd.robotcontroler.main;

import android.view.View;

import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;

/**
 * MainCheckingFragment.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/6 16:41.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */
public class MainCheckingFragment extends BaseFragment {
    //    private static final String TAG = "MainCheckingFragment";
    public static MainCheckingFragment newInstance() {
        return new MainCheckingFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_main_checking;
    }

    @Override
    protected BasePresenter.FragBasePresenter[] getPresenters() {
        return new BasePresenter.FragBasePresenter[0];
    }

    @Override
    protected void onInitPresenters(View view) {

    }

    @Override
    protected void initEvent() {

    }

}
