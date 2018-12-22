package bkdev.android.base_mvp.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import activitystarter.ActivityStarter;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends Fragment
        implements LifecycleOwner, MvpView {

    private LifecycleRegistry mLifecycle;
    private Unbinder mUnBinder;
    private P mPresenter;
    private BaseActivity mBaseActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getContentView(), container, false);
        mLifecycle = new LifecycleRegistry(this);
        mUnBinder = ButterKnife.bind(this, v);
        ActivityStarter.fill(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue(view, savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        Animation anim = AnimationUtils.loadAnimation(getBaseActivity(), nextAnim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getView() != null) {
                    getView().setLayerType(View.LAYER_TYPE_NONE, null);
                    if (enter) {
                        initValueAfterAnimation();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return anim;
    }

    protected P createPresenter() {
        return null;
    }

    public abstract int getContentView();

    public abstract void initValue(View view, Bundle savedInstanceState);

    protected void initValueAfterAnimation() {
    }

    public BaseActivity getBaseActivity() {
        return mBaseActivity;
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    @SuppressWarnings("unchecked")
    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
            mPresenter.attachLifecycle(getLifecycle());
            mPresenter.attachView((V) this, getBaseActivity());
        }
        return mPresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachLifecycle(getLifecycle());
            mPresenter.detachView();
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBaseActivity = null;
    }

    @Override
    public void showLoading() {
        mBaseActivity.showLoading();
    }

    @Override
    public void hideLoading() {
        mBaseActivity.hideLoading();
    }

    @Override
    public void onError(int code, String message) {
        mBaseActivity.showToast(message);
    }

    @Override
    public void hideRefresh() {
    }
}
