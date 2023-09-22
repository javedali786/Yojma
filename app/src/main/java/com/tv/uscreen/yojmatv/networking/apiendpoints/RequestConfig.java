package com.tv.uscreen.yojmatv.networking.apiendpoints;


import static com.tv.uscreen.yojmatv.BuildConstants.BASE_URL;

import android.os.Build;

import com.tv.uscreen.yojmatv.BuildConfig;
import com.tv.uscreen.yojmatv.SDKConfig;
import com.tv.uscreen.yojmatv.networking.profiler.HttpProfiler;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RequestConfig {
    private static Retrofit retrofit = null;
    private static Retrofit enveuRetrofit = null;
    private static Retrofit subscriptionRetrofit = null;
    private static Retrofit participatedRetrofit = null;
    private static Retrofit searchRetrofit = null;
    private static Retrofit configRetrofit = null;
    private static Retrofit recoClickRetrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getEnveuClient() {
        if (enveuRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                        .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                        .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                        .addHeader("x-device-platform", "ANDROID")
                        .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                        .addHeader("x-tracking-sdk-version", "0.0.1")
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                        .addHeader("x-device-name", "");
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();
            if (SDKConfig.getInstance().getOVP_BASE_URL()!=null && !SDKConfig.getInstance().getOVP_BASE_URL().equalsIgnoreCase("")){
                enveuRetrofit = new Retrofit.Builder()
                        .baseUrl(SDKConfig.getInstance().getOVP_BASE_URL())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client)
                        .build();
            }else {
                if (!KsPreferenceKeys.getInstance().getOVPBASEURL().equalsIgnoreCase("")){
                    enveuRetrofit = new Retrofit.Builder()
                            .baseUrl(KsPreferenceKeys.getInstance().getOVPBASEURL())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return enveuRetrofit;
    }

    public static Retrofit getConfigClient() {
        if (configRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.API_KEY_MOB);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            configRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.CONFIG_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return configRetrofit;
    }

    public static Retrofit getRecoClickClient() {
        if (recoClickRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.API_KEY_MOB)
                        .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                        .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                        .addHeader("x-device-platform", "ANDROID")
                        .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                        .addHeader("x-tracking-sdk-version", "0.0.1")
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                        .addHeader("x-device-name", "");
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            recoClickRetrofit = new Retrofit.Builder()
                    .baseUrl("https://media-post.recosenselabs.com/v1.1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return recoClickRetrofit;
    }

    public static Retrofit getEnveuSubscriptionClient(String token) {
        if (subscriptionRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                        .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                        .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                        .addHeader("x-device-platform", "ANDROID")
                        .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                        .addHeader("x-tracking-sdk-version", "0.0.1")
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                        .addHeader("x-device-name", "");
                requestBuilder.addHeader("x-auth",token);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            subscriptionRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return subscriptionRetrofit;
    }

    public static Retrofit getRequestOfferClient(String token) {
        if (participatedRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                        .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                        .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                        .addHeader("x-device-platform", "ANDROID")
                        .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                        .addHeader("x-tracking-sdk-version", "0.0.1")
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                        .addHeader("x-device-name", "");
                requestBuilder.addHeader("x-auth",token);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            participatedRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getPARTICIPATION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return participatedRetrofit;
    }



    public static Retrofit getEnveuSubscriptionClient() {
        if (subscriptionRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                        .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                        .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                        .addHeader("x-device-platform", "ANDROID")
                        .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                        .addHeader("x-tracking-sdk-version", "0.0.1")
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                        .addHeader("x-device-name", "");
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            subscriptionRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return subscriptionRetrofit;
    }

    public static Retrofit getEnveuLogoutClient(String token) {
        if (subscriptionRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder().addHeader("x-api-key",
                        SDKConfig.getInstance().getOvpApiKey()).addHeader("x-auth",token)
                        .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                        .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                        .addHeader("x-device-platform", "ANDROID")
                        .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                        .addHeader("x-tracking-sdk-version", "0.0.1")
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                        .addHeader("x-device-name", "");
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            subscriptionRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return subscriptionRetrofit;
    }


    public static Retrofit getClientInterceptor(final String token) {
        Logger.e("TOKEN", ""+token);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }


        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                    .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                    .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                    .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                    .addHeader("x-device-platform", "ANDROID")
                    .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                    .addHeader("x-tracking-sdk-version", "0.0.1")
                    .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                    .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                    .addHeader("x-device-name", "");

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(SDKConfig.getInstance().getOVP_BASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getUserInteration(final String token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                    .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                    .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                    .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                    .addHeader("x-device-platform", "ANDROID")
                    .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                    .addHeader("x-tracking-sdk-version", "0.0.1")
                    .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                    .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                    .addHeader("x-device-name", "");

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        if (SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL()!=null && !SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL().equalsIgnoreCase("")){
            retrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(KsPreferenceKeys.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }


        return retrofit;
    }

    public static Retrofit getPlayRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }

        OkHttpClient client = httpClient.build();


        if (SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL()!=null && !SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL().equalsIgnoreCase("")){
            retrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(KsPreferenceKeys.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }


        return retrofit;
    }


    public static Retrofit getGeoBlocking() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                    .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                    .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                    .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                    .addHeader("x-device-platform", "ANDROID")
                    .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                    .addHeader("x-tracking-sdk-version", "0.0.1")
                    .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                    .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                    .addHeader("x-device-name", "");

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        if (SDKConfig.getInstance().getOVP_BASE_URL()!=null && !SDKConfig.getInstance().getOVP_BASE_URL().equalsIgnoreCase("")){
            retrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getOVP_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(KsPreferenceKeys.getInstance().getOVPBASEURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }


    public static Retrofit redeemCoupon(final String token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                    .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                    .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                    .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                    .addHeader("x-device-platform", "ANDROID")
                    .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                    .addHeader("x-tracking-sdk-version", "0.0.1")
                    .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                    .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                    .addHeader("x-device-name", "");

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(SDKConfig.getInstance().getCoupon_BASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit paymentClient(final String token,String paymentURL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                    .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                    .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                    .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                    .addHeader("x-device-platform", "ANDROID")
                    .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                    .addHeader("x-tracking-sdk-version", "0.0.1")
                    .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                    .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                    .addHeader("x-device-name", "");

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(paymentURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


    public static Retrofit getClientSearch() {
        if (searchRetrofit==null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                        .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                        .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                        .addHeader("x-device-platform", "ANDROID")
                        .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                        .addHeader("x-tracking-sdk-version", "0.0.1")
                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                        .addHeader("x-device-name", "");
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            searchRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSEARCH_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

        }
        return searchRetrofit;

    }

    public static Retrofit getClientHeader() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                    .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                    .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                    .addHeader("x-device-platform", "ANDROID")
                    .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                    .addHeader("x-tracking-sdk-version", "0.0.1")
                    .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                    .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                    .addHeader("x-device-name", "");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getClientOrderHistoryInterceptor(final String token) {
        Logger.e("TOKEN", ""+token);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }


        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey())
                    .addHeader("x-device-identifier", String.valueOf(UUID.randomUUID()))
                    .addHeader("x-device-os-version", String.valueOf(Build.VERSION.SDK_INT))
                    .addHeader("x-device-model", Objects.requireNonNull(AppCommonMethod.getDeviceName()).toUpperCase())
                    .addHeader("x-device-platform", "ANDROID")
                    .addHeader("x-device-make", Build.MANUFACTURER.toUpperCase())
                    .addHeader("x-tracking-sdk-version", "0.0.1")
                    .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                    .addHeader("x-device-type", Objects.requireNonNull(AppCommonMethod.getDeviceType()))
                    .addHeader("x-device-name", "");

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(SDKConfig.getInstance().getPAYMENT_BASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


}
