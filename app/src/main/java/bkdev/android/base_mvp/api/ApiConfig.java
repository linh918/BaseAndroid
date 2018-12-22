package bkdev.android.base_mvp.api;

import android.content.Context;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class ApiConfig {
    private Context context;
    private String baseUrl;
}
