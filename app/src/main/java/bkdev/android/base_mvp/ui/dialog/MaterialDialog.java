package bkdev.android.base_mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;



import java.util.Collection;

import bkdev.android.base_mvp.R;


/**
 * Created by Linh NDD
 * on 3/23/2018.
 */

public class MaterialDialog {
    private static Dialog sDialog;

    public static void showConfirmDialog(@NonNull final Context context, String message, String positiveText, String negativeText,
                                         SingleButtonCallback positiveCallback,
                                         SingleButtonCallback negativeCallback) {
        if (sDialog != null && sDialog.isShowing()) {
            return;
        }
        sDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveCallback::onClick)
                .setNegativeButton(negativeText, negativeCallback::onClick)
                .show();
    }

    public static void showSingleChoiceDialog(@NonNull final Context context, @NonNull Collection collection, String title, int itemSelected,
                                              SingleChoiceCallback singleChoiceCallback) {
        String[] singleChoiceItems = parseToStringItems(collection);
        showSingleChoiceDialog(context, singleChoiceItems, title, itemSelected, singleChoiceCallback);
    }

    public static void showSingleChoiceDialog(@NonNull final Context context, String[] singleChoiceItems, String title, int itemSelected,
                                              SingleChoiceCallback singleChoiceCallback) {
        if (sDialog != null && sDialog.isShowing()) {
            return;
        }
        sDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(title)
                .setSingleChoiceItems(singleChoiceItems, itemSelected, (dialogInterface, i) -> {
                    singleChoiceCallback.onSelection(dialogInterface, i, singleChoiceItems[i]);
                })
                .show();
    }

    private static String[] parseToStringItems(@NonNull Collection collection) {
        String[] array = new String[0];
        if (collection.size() > 0) {
            array = new String[collection.size()];
            int i = 0;
            for (Object obj : collection) {
                array[i] = obj.toString();
                i++;
            }
        }
        return array;
    }

    public interface SingleButtonCallback {

        void onClick(@NonNull DialogInterface dialog, int selectedIndex);
    }

    public interface SingleChoiceCallback {

        void onSelection(@NonNull DialogInterface dialog, int which, CharSequence text);
    }
}
