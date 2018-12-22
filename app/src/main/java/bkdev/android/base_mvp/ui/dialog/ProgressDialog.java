package bkdev.android.base_mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Window;

import bkdev.android.base_mvp.R;


/**
 * Created by Linh NDD
 * on 2/8/2018.
 */

public class ProgressDialog {
    private static Dialog sDialog;

    /**
     * show loading mDialog when call API
     *
     * @param context app context
     */
    public static void showLoadingDialog(Context context, DialogListener listener) {
        if (null == context) return;
        if (sDialog != null) {
            if (sDialog.isShowing()) {
                return;
            }
            sDialog = null;
        }
        sDialog = new Dialog(context, R.style.DialogProgress);
        sDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sDialog.setCancelable(false);
        //here we set layout of collapse mDialog
        sDialog.setContentView(R.layout.custom_progress_dialog);
        sDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && !event.isCanceled()) {
                if (null != sDialog && sDialog.isShowing() && listener != null) {
                    listener.onBackProgress();
                    hideLoadingDialog();
                }
                return true;
            }
            return false;
        });
        sDialog.show();
    }

    /**
     * dismiss loading mDialog when call API done
     */
    public static void hideLoadingDialog() {
        try {
            if (sDialog != null) {
                sDialog.dismiss();
                sDialog = null;
            }
        } catch (Exception ex) {
            sDialog = null;
        }
    }
}
