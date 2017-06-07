package mixturedd.robotcontroler.remoter.mjpeg;

/**
 * DisplayMode.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/5/30 17:08.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public enum  DisplayMode {
    STANDARD(1), BEST_FIT(4), FULLSCREEN(8);

    private final int value;

    DisplayMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
