package bkdev.android.base_mvp.ui.login;


import bkdev.android.base_mvp.api.ApiClient;
import bkdev.android.base_mvp.api.CallbackWrapper;
import bkdev.android.base_mvp.base.BasePresenter;
import bkdev.android.base_mvp.models.User;
import bkdev.android.base_mvp.models.input.LoginInput;
import io.reactivex.Observable;

/**
 * Created by Linh NDD
 * on 4/30/2018.
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    public void login(LoginInput loginInput) {
        getView().showLoading();
        getCompositeDisposable().add(loginObservable(loginInput)
                .subscribeWith(new CallbackWrapper<User>() {
                    @Override
                    public void next(User user) {
                        getView().loginSuccess(user);
                    }

                    @Override
                    public void complete() {
                        getView().hideLoading();
                    }

                    @Override
                    public void error(int code, String message) {
                        getView().hideLoading();
                        getView().onError(code, message);
                    }
                }));
    }

    private Observable<User> loginObservable(LoginInput loginInput) {
        return Observable.defer(() -> ApiClient.call().login(loginInput)
                .filter(loginResponse -> loginResponse != null && loginResponse.getUser() != null)
                .map(loginResponse -> loginResponse.getUser())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()
                )
        );
    }
}
