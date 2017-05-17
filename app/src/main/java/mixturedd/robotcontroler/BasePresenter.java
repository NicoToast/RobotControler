package mixturedd.robotcontroler;

import android.content.Context;
import android.view.View;

/**
 * Created by hp on 2016/11/21 18:00.
 */

public interface BasePresenter{

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    interface ActBasePresenter<V extends BaseView> extends BasePresenter{
        void init(V actView);
    }

    interface FragBasePresenter<V extends BaseView> extends BasePresenter{
        void init(V fragView, View view);
        void onAttach(Context context);
    }
}
