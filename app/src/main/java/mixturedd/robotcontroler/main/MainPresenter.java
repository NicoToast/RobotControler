package mixturedd.robotcontroler.main;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import mixturedd.robotcontroler.R;
import mixturedd.robotcontroler.unit.StateCheck;

/**
 * MainPresenter.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/2/28 22:41.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */
public class MainPresenter implements MainContract.Presenter {
    //    private static final String TAG = "MainPresenter";
    private MainContract.View viewContract;
    private Context mContext;

/*    public MainPresenter(@NonNull MainContract.View view, MainContract.ConnView connView,
                         MainContract.NoConnView noConnView, Context context) {
        viewContract = view;
        connViewContract = connView;
        noConnViewContract = noConnView;
        mContext = context;
    }*/

    public MainPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void init(MainContract.View actView) {
        viewContract = actView;
    }

    @Override
    public void checkConnection() {
        new checkConnectionTask().execute();
    }


    private class checkConnectionTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return StateCheck.isConnectRobot(mContext);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(mContext, mContext.getString(R.string.state_good), Toast.LENGTH_SHORT).show();
                viewContract.showConnUI();
//                viewContract.hideCheckingUI();
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.state_error), Toast.LENGTH_SHORT).show();
                viewContract.showNoConnUI();
//                viewContract.hideCheckingUI();
            }
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
