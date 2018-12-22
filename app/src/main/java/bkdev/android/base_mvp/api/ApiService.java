package bkdev.android.base_mvp.api;

import bkdev.android.base_mvp.models.input.LoginInput;
import bkdev.android.base_mvp.models.response.UserAccountResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Linh NDD
 * on 1/17/18.
 */

public interface ApiService {
    @POST("/api/user/login")
    Observable<UserAccountResponse> login(@Body LoginInput loginInput);
}
