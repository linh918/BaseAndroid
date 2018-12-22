package bkdev.android.base_mvp.api;

import android.support.annotation.IntDef;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.SocketTimeoutException;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;



public abstract class CallbackWrapper<T> extends DisposableObserver<T> {

    @Override
    public void onNext(@NonNull T t) {
        next(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            int errorCode = ((HttpException) e).response().code();
            error(errorCode, getErrorMessage(responseBody));

        } else if (e instanceof SocketTimeoutException) {
            error(ErrorType.SOCKET_TIMEOUT, "Network Timeout");

        } else if (e instanceof IOException) {
            error(ErrorType.NETWORK_ERROR, "Network Error");

        } else {
            error(ErrorType.UNKNOWN_ERROR, "Data Parse Error");
        }
    }

    @Override
    public void onComplete() {
        complete();
    }

    public abstract void next(T t);

    public abstract void complete();

    public abstract void error(int code, String message);

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("errors");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @IntDef({ErrorType.SOCKET_TIMEOUT, ErrorType.NETWORK_ERROR, ErrorType.UNKNOWN_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface ErrorType {
        int SOCKET_TIMEOUT = 1;
        int NETWORK_ERROR = 2;
        int UNKNOWN_ERROR = 3;
    }
}
