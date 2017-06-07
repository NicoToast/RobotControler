package mixturedd.robotcontroler.widgets;

import android.os.Handler;

/**
 * MsgSwitcher.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/30 23:29.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public class Switcher {
    private MsgSwitcher advTsView;
    private boolean isPaused;
    private int mDuration = 1000;

    public Switcher() {
    }

    public Switcher(MsgSwitcher view, int duration) {
        this.advTsView = view;
        this.mDuration = duration;
    }

    public Switcher setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public Switcher attach(MsgSwitcher view) {
        this.pause();
        this.advTsView = view;
        return this;
    }

    public void start() {
        isPaused = false;
        if (this.advTsView != null) {
            hlUpdt.postDelayed(rbUpdt, mDuration);
        }
    }

    public void pause() {
        isPaused = true;
    }

    public Handler hlUpdt = new Handler();

    public Runnable rbUpdt = new Runnable() {
        @Override
        public void run() {
            if (!isPaused && advTsView != null) {
                advTsView.next();
                hlUpdt.postDelayed(this, mDuration);
            }
        }
    };
}
