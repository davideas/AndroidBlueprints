package eu.davidea.starterapp.persistence.api;

import android.arch.lifecycle.LiveData;

import javax.inject.Inject;

import eu.davidea.starterapp.persistence.api.network.ApiResponse;
import eu.davidea.starterapp.viewmodels.user.AnonymousUser;
import eu.davidea.starterapp.viewmodels.user.LoginRequest;
import eu.davidea.starterapp.viewmodels.user.User;
import eu.davidea.starterapp.viewmodels.user.UserToken;
import retrofit2.mock.BehaviorDelegate;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class OfflineUserApi implements UserApi {

    private static final String USER_URL = "/v1/auth";
    private final BehaviorDelegate<UserApi> delegate;

    @Inject
    public OfflineUserApi(BehaviorDelegate<UserApi> delegate) {
        this.delegate = delegate;
    }

    @Override
    public LiveData<ApiResponse<User>> getUser(Long userId) {
        return delegate.returningResponse(new Exception("getUser from Api not implemented"))
                .getUser(userId);
    }

    @Override
    public LiveData<ApiResponse<UserToken>> login(LoginRequest loginRequest) {
        Timber.d("Generating Anonymous User %s", loginRequest.getUsername());
        return delegate.returningResponse(new AnonymousUser())
                .login(loginRequest);
    }

    @Override
    public void logout() {
    }

}