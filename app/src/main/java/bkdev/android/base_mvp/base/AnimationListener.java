package bkdev.android.base_mvp.base;

import android.view.animation.Animation;


public interface AnimationListener extends Animation.AnimationListener {
    @Override
    default void onAnimationStart(Animation animation) {
    }

    @Override
    void onAnimationEnd(Animation animation);

    @Override
    default void onAnimationRepeat(Animation animation) {
    }
}
