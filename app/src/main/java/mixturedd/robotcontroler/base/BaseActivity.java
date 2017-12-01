package mixturedd.robotcontroler.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * BaseActivity.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/4/23 21:23.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Set<BasePresenter.ActBasePresenter> mAllPresenters = new HashSet<>(1);

    /**
     * 获取layout的id，具体由子类实现
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 需要子类来实现，获取子类的BasePresenter，一个activity有可能有多个BasePresenter
     */
    protected abstract BasePresenter.ActBasePresenter[] getPresenters();

    /**
     * 初始化presenters
     */
    protected abstract void onInitPresenters();

    /**
     * 事件监听
     */
    protected abstract void initEvent();

    /**
     * 从intent中解析数据，具体子类来实现
     *
     * @param argIntent
     */
    protected void parseArgumentsFromIntent(Intent argIntent) {
    }

    /**
     * 加载设置
     */
    protected void parsePreference() {
    }

    private void addPresenters() {
        BasePresenter.ActBasePresenter[] presenters = getPresenters();
        if (presenters != null) {
            mAllPresenters.addAll(Arrays.asList(presenters));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        if (getIntent() != null) {
            parseArgumentsFromIntent(getIntent());
        }
        parsePreference();
        addPresenters();
        onInitPresenters();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //依次调用BasePresenter的onResume方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onResume();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //依次调用BasePresenter的onStop方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onStop();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //依次调用BasePresenter的onPause方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onPause();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //依次调用BasePresenter的onStart方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onStart();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //依次调用BasePresenter的onDestroy方法
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                presenter.onDestroy();
            }
        }
    }
}
