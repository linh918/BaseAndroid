package bkdev.android.base_mvp.base;



public interface MvpView {
    void showLoading();

    void hideLoading();

    void onError(int code, String message);

    void hideRefresh();
}
