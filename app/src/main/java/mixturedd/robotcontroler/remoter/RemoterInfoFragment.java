package mixturedd.robotcontroler.remoter;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;

/**
 * RemoterInfoFragment.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/3/9 10:31.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */
public class RemoterInfoFragment extends BaseFragment implements RemoterContract.InfoView {
    //    private static final String TAG = "RemoterInfoFragment";
    private RemoterInfoPresenter mPresenter = new RemoterInfoPresenter();
    private TextView temperature;
    private TextView gas;
    private TextView distance;
    private TextView humidity;

    public static RemoterInfoFragment newInstance() {
        Bundle args = new Bundle();
        RemoterInfoFragment fragment = new RemoterInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_remoter_info;
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
    public void initView(View view) {
        temperature = (TextView) view.findViewById(R.id.info_temperature_value);
        gas = (TextView) view.findViewById(R.id.info_gas_value);
        distance = (TextView) view.findViewById(R.id.info_distance_value);
        humidity = (TextView) view.findViewById(R.id.info_humidity_value);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showTemperature(String str) {
        temperature.setText(String.format(mActivity.getString(R.string.unit_degree), str));
    }

    @Override
    public void showGas(String str) {
        gas.setText(String.format(mActivity.getString(R.string.unit_gas), str));
    }

    @Override
    public void showDistance(String str) {
        distance.setText(String.format(mActivity.getString(R.string.unit_distance), str));
    }

    @Override
    public void showHumidity(String str) {
        humidity.setText(String.format(mActivity.getString(R.string.unit_humidity), str));
    }

}
