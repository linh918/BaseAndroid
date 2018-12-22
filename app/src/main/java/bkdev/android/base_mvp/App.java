package bkdev.android.base_mvp;

import android.app.Application;
import android.content.Context;

import bkdev.android.base_mvp.api.ApiClient;
import bkdev.android.base_mvp.api.ApiConfig;
import bkdev.android.base_mvp.utils.rx.RxBus;

/**
 * Created by linhdd
 * on 1/17/18.
 */

public class App extends Application {

    private static App sInstance;
    private RxBus mBus;

    public static synchronized App getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        ApiConfig apiConfig = ApiConfig.builder()
                .context(getApplicationContext())
                .baseUrl(BuildConfig.HOST_API)
                .build();
        ApiClient.getInstance().init(apiConfig);

        mBus = new RxBus();
    }

    public RxBus bus() {
        return mBus;
    }
}
