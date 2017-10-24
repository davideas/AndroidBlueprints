package eu.davidea.blueapp.persistence.repositories;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import eu.davidea.blueapp.infrastructure.AppExecutors;
import eu.davidea.blueapp.persistence.api.UserApi;
import eu.davidea.blueapp.persistence.api.network.ApiResponse;
import eu.davidea.blueapp.persistence.api.network.AuthInterceptor;
import eu.davidea.blueapp.persistence.api.network.NetworkBoundResource;
import eu.davidea.blueapp.persistence.api.network.Resource;
import eu.davidea.blueapp.persistence.db.StarterDatabase;
import eu.davidea.blueapp.persistence.db.UserDao;
import eu.davidea.blueapp.viewmodels.user.LoginRequest;
import eu.davidea.blueapp.viewmodels.user.User;
import eu.davidea.blueapp.viewmodels.user.UserToken;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class UserRepository {

    private UserApi api;
    private UserDao userDao;
    private AppExecutors executors;
    private AuthInterceptor authInterceptor;

    @Inject
    public UserRepository(StarterDatabase database, UserApi api,
                          AppExecutors executors, AuthInterceptor authInterceptor) {
        this.userDao = database.userDao();
        this.api = api;
        this.executors = executors;
        this.authInterceptor = authInterceptor;
    }

    public LiveData<Resource<UserToken>> login(LoginRequest loginRequest) {
        return new NetworkBoundResource<UserToken, UserToken>(executors) {
            @NonNull
            @Override
            protected LiveData<UserToken> loadFromDb() {
                Timber.d("Loading LoggedUser username=%s from DB", loginRequest.getUsername());
                return userDao.getLoggedUser();
            }

            @Override
            protected boolean shouldFetch(@Nullable UserToken userToken) {
                if (userToken == null) Timber.d("No LoggedUser in the Database");
                return userToken == null;
            }

            @Override
            protected void processData(UserToken userToken) {
                if (userToken != null) {
                    authInterceptor.setToken(userToken.getToken());
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserToken>> apiCall() {
                Timber.d("Logging in User username=%s from REST API", loginRequest.getUsername());
                return api.login(loginRequest);
            }

            @Override
            protected void saveCallResult(@NonNull UserToken userToken) {
                Timber.d("Saving loggedUser username=%s to DB", userToken.getUsername());
                userDao.saveLoggedUser(userToken);
            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> getUser(User user) {
        return new NetworkBoundResource<User, User>(executors) {
            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Timber.d("Loading User username=%s from DB", user.getUsername());
                return userDao.getUser(user.getId());
            }

            @Override
            protected boolean shouldFetch(@Nullable User user) {
                return user == null;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> apiCall() {
                Timber.d("Loading User username=%s using token from REST Api", user.getUsername());
                return api.getUser(user.getId());
            }

            @Override
            protected void saveCallResult(@NonNull User user) {
                Timber.d("Saving user username=%s to DB", user.getUsername());
                userDao.saveUser(user);
            }
        }.asLiveData();
    }

    public void logout(UserToken userToken) {
        Timber.d("Logging out username=%s", userToken.getUsername());
        api.logout(); // Needs token from AuthInterceptor
        userDao.logout(userToken);
        authInterceptor.clearToken();
    }

}