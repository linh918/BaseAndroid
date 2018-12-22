package bkdev.android.base_mvp.type;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tamnguyen
 * on 3/2/18.
 */

@IntDef({AnimationType.RIGHT_IN, AnimationType.LEFT_IN, AnimationType.BOTTOM_IN,
        AnimationType.BOTTOM_OUT, AnimationType.FADE})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationType {
    int RIGHT_IN = 0;
    int LEFT_IN = 1;
    int BOTTOM_IN = 2;
    int BOTTOM_OUT = 3;
    int FADE = 4;
}
