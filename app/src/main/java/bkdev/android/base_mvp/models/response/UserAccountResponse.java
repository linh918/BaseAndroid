package bkdev.android.base_mvp.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import bkdev.android.base_mvp.models.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by Linh NDD
 * on 4/30/2018.
 */

@Getter
@EqualsAndHashCode
public class UserAccountResponse {
    @SerializedName("data")
    @Expose
    private User user;
}
