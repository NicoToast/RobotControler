package mixturedd.robotcontroler.main;


import android.support.design.widget.FloatingActionButton;
import android.view.View;

import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;

public class MainNoConnFragment extends BaseFragment implements MainContract.NoConnView {

    private MainNoConnPresenter mPresenter = new MainNoConnPresenter();

    public static MainNoConnFragment newInstance() {
        MainNoConnFragment fragment = new MainNoConnFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frag_main_no_conn;
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
                mPresenter.refresh();
            }
        });
    }

    @Override
    public void animatedWifi() {
    }

}
