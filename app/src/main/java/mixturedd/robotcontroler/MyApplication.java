package mixturedd.robotcontroler;

import android.app.Application;
import android.view.WindowManager;

import mixturedd.robotcontroler.task.ThreadManager;

/**
 * MyApplication.java
 * Description :
 * <p>
 * Created by InfiniteStack on 2017/5/18 10:05.
 * Copyright © 2017 InfiniteStack. All rights reserved.
 */

public class MyApplication extends Application {
    private WindowManager.LayoutParams mLayoutParams;

    @Override
    public void onCreate() {
        super.onCreate();
        mLayoutParams = new WindowManager.LayoutParams();

//        CrashHandler.getInstance().init(this);//全局捕获崩溃异常日志并保存本地

        ThreadManager.getInstance();
    }



    public WindowManager.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }
}
