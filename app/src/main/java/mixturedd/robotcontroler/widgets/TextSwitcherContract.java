package mixturedd.robotcontroler.widgets;

/**
 * TextSwitcherContract.java
 * Description :
 * <p>
 * Created by MixtureDD on 2017/6/2 16:51.
 * Copyright © 2017 MixtureDD. All rights reserved.
 */

public interface TextSwitcherContract<T> {
    void addText(T text);

    void pollText();
}
