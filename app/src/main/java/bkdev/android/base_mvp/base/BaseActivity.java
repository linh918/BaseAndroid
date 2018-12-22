package bkdev.android.base_mvp.base;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;



import activitystarter.ActivityStarter;
import bkdev.android.base_mvp.R;
import bkdev.android.base_mvp.type.AnimationType;
import bkdev.android.base_mvp.ui.dialog.DialogListener;
import bkdev.android.base_mvp.ui.dialog.ProgressDialog;
import butterknife.ButterKnife;
import butterknife.Unbinder;



public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends AppCompatActivity
        implements MvpView, LifecycleOwner {

    private LifecycleRegistry mLifecycle;
    private Unbinder mUnBinder;
    private SafeTransaction mSafeTransaction;
    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mLifecycle = new LifecycleRegistry(this);
        mUnBinder = ButterKnife.bind(this);
        mSafeTransaction = SafeTransaction.createInstance(getLifecycle(), getSupportFragmentManager());
        getLifecycle().addObserver(mSafeTransaction);
        ActivityStarter.fill(this);
        initValue(savedInstanceState);
    }

    protected P createPresenter() {
        return null;
    }

    public abstract int getContentView();

    public abstract void initValue(Bundle savedInstanceState);

    @SuppressWarnings("unchecked")
    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
            mPresenter.attachLifecycle(getLifecycle());
            mPresenter.attachView((V) this, this);
        }
        return mPresenter;
    }

    public SafeTransaction getSafeTransaction() {
        return mSafeTransaction;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachLifecycle(getLifecycle());
            mPresenter.detachView();
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void showLoading() {
        ProgressDialog.hideLoadingDialog();
        ProgressDialog.showLoadingDialog(this, new DialogListener() {
            @Override
            public void onBackProgress() {
                BaseActivity.this.onDialogBackProgress();
            }
        });
    }

    @Override
    public void hideLoading() {
        ProgressDialog.hideLoadingDialog();
    }

    @Override
    public void onError(int code, String message) {
        showToast(message);
    }

    public void onDialogBackProgress() {
    }

    @Override
    public void hideRefresh() {
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resMsg) {
        Toast.makeText(this, getString(resMsg), Toast.LENGTH_SHORT).show();
    }

    public void activityTransition(int animationType) {
        switch (animationType) {
            case AnimationType.RIGHT_IN:
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;

            case AnimationType.LEFT_IN:
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;

            case AnimationType.FADE:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    public void addFragment(Fragment target, int animationType, int containerId) {
        switch (animationType) {
            case AnimationType.RIGHT_IN:
                addFragment(target, R.anim.slide_right_in, R.anim.translate_still, R.anim.translate_still, R.anim.slide_right_out, containerId);
                break;
            case AnimationType.BOTTOM_IN:
                addFragment(target, R.anim.slide_bottom_in, R.anim.translate_still, R.anim.translate_still, R.anim.slide_bottom_out, containerId);
                break;
            case AnimationType.FADE:
                addFragment(target, R.anim.fade_in, R.anim.translate_still, R.anim.translate_still, R.anim.fade_out, containerId);
                break;
        }
    }

    private void addFragment(Fragment target, int enter, int exit, int popEnter, int popExit, int containerId) {
        mSafeTransaction.registerTransition(fragmentManager -> {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(enter, exit,
                    popEnter, popExit);
            ft.add(containerId, target, target.getClass().getName());
            ft.addToBackStack(null);
            if (!isFinishing()) {
                ft.commit();
            }
        });
    }

    public void addFragment(Fragment target, int containerId) {
        mSafeTransaction.registerTransition(fragmentManager -> {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(containerId, target, target.getClass().getName());
            ft.addToBackStack(null);
            if (!isFinishing()) {
                ft.commit();
            }
        });
    }
}
