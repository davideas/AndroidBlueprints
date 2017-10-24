package eu.davidea.blueapp.viewmodels.user;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eu.davidea.blueapp.persistence.api.network.Resource;
import eu.davidea.blueapp.persistence.repositories.AbsentLiveData;
import eu.davidea.blueapp.persistence.repositories.UserRepository;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
public class UserViewModel extends ViewModel {

    private UserRepository repository;
    private MutableLiveData<Identifier> userIdentifier;
    private LiveData<Resource<UserToken>> user;

    public UserViewModel() {
        // Needed by ViewModelProviders
    }

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.repository = userRepository;
        if (user == null) {
            Timber.d("Init UserViewModel");
            userIdentifier = new MutableLiveData<>();
            user = Transformations.switchMap(userIdentifier, input -> {
                if (input == null) {
                    return AbsentLiveData.create();
                }
                return repository.login(new LoginRequest(input.username, input.password));
            });
        }
    }

    public LiveData<Resource<UserToken>> getUser() {
        return user;
    }

    public void login(String username, CharSequence password) {
        Identifier identifier = new Identifier(username, password);
        if (!identifier.equals(userIdentifier.getValue())) {
            userIdentifier.setValue(identifier);
        }
    }

    public void logout(LifecycleOwner owner) {
        if (user.getValue() != null) {
            repository.logout(user.getValue().data);
        }
        userIdentifier = null;
        user.removeObservers(owner);
        user = null;
    }

    static class Identifier {

        private String username;
        private CharSequence password;

        Identifier(String username, CharSequence password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Identifier that = (Identifier) o;

            if (username != null ? !username.equals(that.username) : that.username != null)
                return false;
            return password != null ? password.equals(that.password) : that.password == null;
        }

        @Override
        public int hashCode() {
            int result = username != null ? username.hashCode() : 0;
            result = 31 * result + (password != null ? password.hashCode() : 0);
            return result;
        }
    }

}