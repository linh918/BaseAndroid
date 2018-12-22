package bkdev.android.base_mvp.ui;

import android.os.Bundle;

import activitystarter.MakeActivityStarter;
import bkdev.android.base_mvp.R;
import bkdev.android.base_mvp.base.BaseActivity;


@MakeActivityStarter
public class MainActivity extends BaseActivity {

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initValue(Bundle savedInstanceState) {

    }
}
