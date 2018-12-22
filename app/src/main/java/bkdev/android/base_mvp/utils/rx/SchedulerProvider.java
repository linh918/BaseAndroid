package bkdev.android.base_mvp.utils.rx;

import io.reactivex.Scheduler;

/**
 * Created by Linh NDD
 * on 1/16/18.
 */

public interface SchedulerProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();

    Scheduler newThread();
}
