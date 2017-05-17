package mixturedd.robotcontroler.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.remoter.RemoterActivity;

/**
 * MainLoadingFragment.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/6 09:54.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */
public class MainConnFragment extends BaseFragment implements MainContract.ConnView {
    //    private static final String TAG = "MainLoadingFragment";
    private MainConnPresenter mPresenter = new MainConnPresenter();

    public static MainConnFragment newInstance() {
        return new MainConnFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_main_conn;
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
    protected void initEvent() {

    }

    @Override
    public void initView(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.infoFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresenter.enterRemoter();
            }
        });
    }

    @Override
    public void showRemoterUI() {
        startActivity(new Intent(getActivity(), RemoterActivity.class));
    }
}
