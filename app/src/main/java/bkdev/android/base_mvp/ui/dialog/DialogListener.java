package bkdev.android.base_mvp.ui.dialog;


/**
 * Created by tamnguyen
 * on 10/21/17.
 */

public interface DialogListener {

    default void onBackProgress() {
    }

    default void onDismiss() {
    }

    default void onPositive() {
    }
}
