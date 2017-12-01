package mixturedd.robotcontroler.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * BaseFragment.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/14 16:10.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public abstract class BaseFragment extends Fragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private Set<BasePresenter.FragBasePresenter> mAllPresenters = new HashSet<>(1);
    protected Activity mActivity;

    /**
     * 获取layout的id，具体由子类实现
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 需要子类来实现，获取子类的BasePresenter
     */
    protected abstract BasePresenter.FragBasePresenter[] getPresenters();

    /**
     * 初始化presenters
     */
    protected abstract void onInitPresenters(View view);

    /**
     * 事件监听
     */
    protected abstract void initEvent();

    /**
     * 从Bundle中解析数据，具体子类来实现
     *
     * @param argBundle Bundle from newInstance
     */
    protected void parseArgumentsFromBundle(Bundle argBundle) {
    }

    /**
     * 加载设置
     */
    protected void parsePreference() {
    }

    private void addPresenters() {
        BasePresenter.FragBasePresenter[] presenters = getPresenters();
        if (presenters != null) {
            mAllPresenters.addAll(Arrays.asList(presenters));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            parseArgumentsFromBundle(getArguments());
        }

        parsePreference();

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        onInitPresenters(view);
        initEvent();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        addPresenters();
        //依次调用BasePresenter的onAttach方法
        for (BasePresenter.FragBasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onAttach(context);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onResume() {
        super.onResume();
        //依次调用BasePresenter的onResume方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onResume();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //依次调用BasePresenter的onStop方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onStop();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //依次调用BasePresenter的onPause方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onPause();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //依次调用BasePresenter的onStart方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onStart();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //依次调用BasePresenter的onDestroy方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onDestroy();
            }
        }
    }


}
