package bkdev.android.base_mvp.base;

import android.support.v4.view.ViewPager;



public interface OnPageChangeListener extends ViewPager.OnPageChangeListener {
    @Override
    default void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    void onPageSelected(int position);

    @Override
    default void onPageScrollStateChanged(int state) {
    }
}
