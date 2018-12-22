package bkdev.android.base_mvp.api;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import bkdev.android.base_mvp.BuildConfig;
import bkdev.android.base_mvp.shareds.Prefs;
import bkdev.android.base_mvp.shareds.PrefsKey;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



public class ApiClient {
    private static final String HEADER_UA = "Account-Agent";
    private static final String TAG = "ApiClient";
    private static final String AUTHORIZATION = "Authorization";
    private static final int TIMEOUT_CONNECTION = 15000;
    private static ApiClient sInstance;
    private ApiService mApiService;

    public static synchronized ApiClient getInstance() {
        if (sInstance == null) {
            sInstance = new ApiClient();
        }
        return sInstance;
    }

    public void init(ApiConfig apiConfig) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(
                BuildConfig.BUILD_TYPE.equals("release")
                        ? HttpLoggingInterceptor.Level.NONE : HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("TrustAllX509TrustManager") final TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            Log.d(ApiClient.class.getName(), e.getMessage());
        }

        PackageManager pm = apiConfig.getContext().getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = pm.getPackageInfo(apiConfig.getContext().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        String userAgent = String.format(Locale.getDefault(), "%s %s/%s", System.getProperty("http.agent"), apiConfig.getContext().getPackageName(), versionName);

        builder.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader(HEADER_UA, userAgent);
            if (Prefs.getInstance().get(PrefsKey.IS_LOGIN, Boolean.class)) {
                String accessToken = Prefs.getInstance().get(PrefsKey.ACCESS_TOKEN, String.class);
                if (!TextUtils.isEmpty(accessToken)) {
                    requestBuilder.addHeader(AUTHORIZATION, accessToken);
                }
            }
            return chain.proceed(requestBuilder.build());
        });

        builder.addInterceptor(interceptor);
        builder.retryOnConnectionFailure(true);
        builder.readTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS);
        builder.connectTimeout(TIMEOUT_CONNECTION, TimeUnit.MILLISECONDS);

        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(apiConfig.getContext().getCacheDir(), cacheSize);
        builder.cache(cache);

        OkHttpClient okHttpClient = builder.build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiConfig.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    public static ApiService call() {
        return getInstance().mApiService;
    }

}
