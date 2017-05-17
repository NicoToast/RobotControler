package mixturedd.robotcontroler;

import android.view.View;

/**
 * BaseView.java
 * Description :
 * <p>
 * Created by hp on 2016/11/21 19:07.
 * Copyright © 2016 hp. All rights reserved.
 */

public interface BaseView {

    interface ActBaseView extends BaseView{
        void initView();
    }

    interface FragBaseView extends BaseView{
        void initView(View view);
    }
}
