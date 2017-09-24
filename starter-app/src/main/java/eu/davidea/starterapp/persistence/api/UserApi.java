package eu.davidea.starterapp.persistence.api;

import android.arch.lifecycle.LiveData;

import eu.davidea.starterapp.persistence.api.network.ApiResponse;
import eu.davidea.starterapp.persistence.api.network.AuthInterceptor;
import eu.davidea.starterapp.viewmodels.user.LoginRequest;
import eu.davidea.starterapp.viewmodels.user.User;
import eu.davidea.starterapp.viewmodels.user.UserToken;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Davide
 * @since 17/09/2017
 */
public interface UserApi {

    String USER_URL = "/v1/auth";

    @GET(USER_URL + "/users/{userId}")
    LiveData<ApiResponse<User>> getUser(@Path("userId") Long userId);

    @POST(USER_URL + "/login") // Does not require authentication
    @Headers(AuthInterceptor.NO_AUTHORIZATION_VALUE)
    LiveData<ApiResponse<UserToken>> login(@Body LoginRequest request);

    @GET(USER_URL + "/logout")
    void logout();

}