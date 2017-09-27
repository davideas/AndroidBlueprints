package eu.davidea.starterapp.infrastructure.injection;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.davidea.starterapp.BuildConfig;
import eu.davidea.starterapp.persistence.api.MessageApi;
import eu.davidea.starterapp.persistence.api.UserApi;
import eu.davidea.starterapp.persistence.api.network.AuthInterceptor;
import eu.davidea.starterapp.persistence.api.network.LiveDataCallAdapterFactory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Davide
 * @since 17/09/2017
 */
@Module
public class ApiModule {

    public ApiModule() {
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(gson)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverter() {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, AuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .addNetworkInterceptor(authInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    AuthInterceptor provideAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    UserApi provideUserApi(Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

    @Provides
    @Singleton
    MessageApi provideMessageApi(Retrofit retrofit) {
        return retrofit.create(MessageApi.class);
    }

}