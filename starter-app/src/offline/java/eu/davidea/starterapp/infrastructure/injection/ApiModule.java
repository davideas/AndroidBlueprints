package eu.davidea.starterapp.infrastructure.injection;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.davidea.starterapp.BuildConfig;
import eu.davidea.starterapp.persistence.api.MessageApi;
import eu.davidea.starterapp.persistence.api.OfflineMessageApi;
import eu.davidea.starterapp.persistence.api.OfflineUserApi;
import eu.davidea.starterapp.persistence.api.UserApi;
import eu.davidea.starterapp.persistence.api.network.AuthInterceptor;
import eu.davidea.starterapp.persistence.api.network.LiveDataCallAdapterFactory;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * @author Davide
 * @since 17/09/2017
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    MockRetrofit provideRetrofit(GsonConverterFactory gson, OkHttpClient okHttpClient) {
        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setDelay(1, TimeUnit.SECONDS);
        behavior.setVariancePercent(40);
        behavior.setErrorPercent(2);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
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
    OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .addNetworkInterceptor(authInterceptor)
                .build();
    }

    @Provides
    @Singleton
    AuthInterceptor provideAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Provides
    @Singleton
    UserApi provideUserApi(MockRetrofit mockRetrofit) {
        BehaviorDelegate<UserApi> delegate = mockRetrofit.create(UserApi.class);
        return new OfflineUserApi(delegate);
    }

    @Provides
    @Singleton
    MessageApi provideMessageApi(MockRetrofit mockRetrofit) {
        BehaviorDelegate<MessageApi> delegate = mockRetrofit.create(MessageApi.class);
        return new OfflineMessageApi(delegate);
    }

}