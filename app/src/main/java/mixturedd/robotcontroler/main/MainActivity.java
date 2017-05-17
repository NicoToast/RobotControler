package mixturedd.robotcontroler.main;

import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import mixturedd.robotcontroler.BaseActivity;
import mixturedd.robotcontroler.BaseFragment;
import mixturedd.robotcontroler.BasePresenter;
import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.unit.ActivityUtils;


public class MainActivity extends BaseActivity implements MainContract.View {
    private static final String TAG = "MainActivity";
    /*    private final static String TAG_FRAGMENT_CONN = MainConnFragment.class.getSimpleName();
    private final static String TAG_FRAGMENT_NO_CONN = MainNoConnFragment.class.getSimpleName();
    private final static String TAG_FRAGMENT_CHECKING = MainCheckingFragment.class.getSimpleName();*/
    private final static int TAG_FRAGMENT_CONN = 1;
    private final static int TAG_FRAGMENT_NO_CONN = 0;
    private final static int TAG_FRAGMENT_CHECKING = 3;
    private int fragmentFlag = 0;
    //    private HashMap<String, Fragment> fragmentMap = new HashMap<>();
    private ArrayList<BaseFragment> fragmentList = new ArrayList<>();
    private MainPresenter mPresenter = new MainPresenter(this);

    @Override
    protected int getLayoutResId() {
        return R.layout.act_main;
    }

    @Override
    protected BasePresenter.ActBasePresenter[] getPresenters() {
        return new BasePresenter.ActBasePresenter[]{mPresenter};
    }

    @Override
    protected void onInitPresenters() {
        mPresenter.init(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPresenter.checkConnection();
    }

    private void initFragment() {
/*        fragmentMap.put(TAG_FRAGMENT_CONN, MainConnFragment.newInstance());
        fragmentMap.put(TAG_FRAGMENT_NO_CONN, MainNoConnFragment.newInstance());
        fragmentMap.put(TAG_FRAGMENT_CHECKING, MainCheckingFragment.newInstance());*/
//        fragmentList.add(TAG_FRAGMENT_CHECKING, MainCheckingFragment.newInstance());
        fragmentList.add(TAG_FRAGMENT_NO_CONN, MainNoConnFragment.newInstance());
        fragmentList.add(TAG_FRAGMENT_CONN, MainConnFragment.newInstance());
        MainNoConnPresenter presenter = (MainNoConnPresenter)((MainNoConnFragment)fragmentList.get(TAG_FRAGMENT_NO_CONN)).getPresenters()[0];
        presenter.setParentPresenter(mPresenter);
    }

    @Override
    public void initView() {
        initFragment();
/*        MainCheckingFragment mainCheckingFragment =
                (MainCheckingFragment) getSupportFragmentManager().findFragmentById(R.id.mainContentFrame);
        if (mainCheckingFragment == null) {
            // Create the fragment

        }*/
/*        MainCheckingFragment mainCheckingFragment = MainCheckingFragment.newInstance();
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), mainCheckingFragment, R.id.mainContentFrame,
                mainCheckingFragment.getClass().getName());*/
    }

    @Override
    public void showConnUI() {
        if (fragmentFlag != TAG_FRAGMENT_CONN) {
            if (fragmentFlag == TAG_FRAGMENT_NO_CONN) {
                ActivityUtils.removeFragment(getSupportFragmentManager(), fragmentList.get(fragmentFlag));
            }
            ActivityUtils.changeFragment(getSupportFragmentManager(),
                    R.id.mainContentFrame, fragmentList.get(TAG_FRAGMENT_CONN));
            fragmentFlag = TAG_FRAGMENT_CONN;
        }
    }

    @Override
    public void showNoConnUI() {
        if (fragmentFlag != TAG_FRAGMENT_NO_CONN) {
            if (fragmentFlag == TAG_FRAGMENT_CONN) {
                ActivityUtils.removeFragment(getSupportFragmentManager(), fragmentList.get(fragmentFlag));
            }
            ActivityUtils.changeFragment(getSupportFragmentManager(),
                    R.id.mainContentFrame, fragmentList.get(TAG_FRAGMENT_NO_CONN));
            fragmentFlag = TAG_FRAGMENT_NO_CONN;
        }
    }

    @Override
    public void hideCheckingUI() {
/*        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragmentList.get(TAG_FRAGMENT_CHECKING));
        transaction.commit();*/

        while (getSupportFragmentManager().getBackStackEntryCount() != 0){
            getSupportFragmentManager().popBackStack();
        }

    }

}
