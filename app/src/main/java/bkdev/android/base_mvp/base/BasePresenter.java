package bkdev.android.base_mvp.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;


import java.lang.ref.WeakReference;

import bkdev.android.base_mvp.utils.rx.AppSchedulerProvider;
import bkdev.android.base_mvp.utils.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;



public class BasePresenter<V extends MvpView> implements MvpPresenter<V>, LifecycleObserver {

    private WeakReference<V> mView;
    private WeakReference<Context> mContext;
    private final CompositeDisposable mCompositeDisposable;
    private final SchedulerProvider mSchedulerProvider;

    public BasePresenter() {
        mCompositeDisposable = new CompositeDisposable();
        mSchedulerProvider = new AppSchedulerProvider();
    }

    @Override
    public void attachLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override
    public void detachLifecycle(Lifecycle lifecycle) {
        lifecycle.removeObserver(this);
    }

    @Override
    public void attachView(V view, Context context) {
        mView = new WeakReference<>(view);
        mContext = new WeakReference<>(context);
    }

    @Override
    public void detachView() {
        mCompositeDisposable.dispose();
        mView.clear();
    }

    @Override
    public V getView() {
        return mView.get();
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    @Override
    public Context getContext() {
        return mContext.get();
    }
}
