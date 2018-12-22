package bkdev.android.base_mvp.ui.login;

import android.os.Bundle;

import activitystarter.MakeActivityStarter;
import bkdev.android.base_mvp.R;
import bkdev.android.base_mvp.base.BaseActivity;
import bkdev.android.base_mvp.models.User;

/**
 * Created by Linh NDD
 * on 4/8/2018.
 */

@MakeActivityStarter
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initValue(Bundle savedInstanceState) {

    }

    @Override
    public void loginSuccess(User user) {

    }
}
