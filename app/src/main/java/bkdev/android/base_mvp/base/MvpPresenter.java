package bkdev.android.base_mvp.base;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.res.Resources;


import bkdev.android.base_mvp.utils.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;



public interface MvpPresenter<V extends MvpView> {
    void attachLifecycle(Lifecycle lifecycle);

    void detachLifecycle(Lifecycle lifecycle);

    void attachView(V view, Context context);

    void detachView();

    V getView();

    boolean isViewAttached();

    CompositeDisposable getCompositeDisposable();

    SchedulerProvider getSchedulerProvider();

    Context getContext();

    default Resources getResources() {
        return getContext().getResources();
    }

    default String getString(int resId) {
        return getContext().getString(resId);
    }

    default String getString(int resId, Object... objects) {
        return getContext().getString(resId, objects);
    }
}
