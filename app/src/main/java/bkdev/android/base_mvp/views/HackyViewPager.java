package bkdev.android.base_mvp.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class HackyViewPager extends ViewPager {

    private boolean mEnable;

    public HackyViewPager(Context context) {
        this(context, null);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mEnable = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //do not intercept
        return mEnable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // do not consume
        return mEnable && super.onTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item) {
        this.setCurrentItem(item, false);
    }

    public void setEnabled(boolean enabled) {
        mEnable = enabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // find the first child view
        View view = getChildAt(0);
        if (view != null) {
            // measure the first child view with the specified measure spec
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
    }

    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
