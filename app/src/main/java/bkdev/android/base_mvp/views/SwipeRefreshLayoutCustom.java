package bkdev.android.base_mvp.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import bkdev.android.base_mvp.R;


public class SwipeRefreshLayoutCustom extends SwipeRefreshLayout {
    private int mTouchSlop;
    private float mPrevX;
    private View mScrollUpChild;

    public SwipeRefreshLayoutCustom(Context context) {
        this(context, null);
    }

    public SwipeRefreshLayoutCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.primary));
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean canChildScrollUp() {
        if (mScrollUpChild != null) {
            return mScrollUpChild.canScrollVertically(-1);
        }
        return super.canChildScrollUp();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setScrollUpChild(View view) {
        mScrollUpChild = view;
    }
}
