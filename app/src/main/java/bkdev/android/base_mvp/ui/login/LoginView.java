package bkdev.android.base_mvp.ui.login;


import bkdev.android.base_mvp.base.MvpView;
import bkdev.android.base_mvp.models.User;

/**
 * Created by Linh NDD
 * on 4/30/2018.
 */

public interface LoginView extends MvpView {
    void loginSuccess(User user);
}
