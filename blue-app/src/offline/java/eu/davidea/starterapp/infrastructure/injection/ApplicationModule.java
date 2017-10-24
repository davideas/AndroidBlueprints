package eu.davidea.blueapp.infrastructure.injection;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.davidea.blueapp.MyApplication;
import eu.davidea.blueapp.persistence.db.StarterDatabase;
import eu.davidea.blueapp.persistence.preferences.PreferencesService;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * @author Davide
 * @since 17/09/2017
 */
@Module(includes = ViewModelModule.class)
public class ApplicationModule {

    private MyApplication application;
    private final StarterDatabase database;

    public ApplicationModule(MyApplication application) {
        this.application = application;

        Timber.w("Build inMemoryDatabase");
        this.database = Room.inMemoryDatabaseBuilder(
                application,
                StarterDatabase.class
        ).build();
    }


    @Provides
    @Singleton
    StarterDatabase provideDatabase() {
        return database;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    PreferencesService providesPreferencesService(SharedPreferences sharedPreferences) {
        return new PreferencesService(sharedPreferences);
    }

    @Provides @Named("activity")
    public CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }

    @Provides @Named("vm")
    public CompositeDisposable provideVMCompositeDisposable(){
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    MyApplication provideMyApplication() {
        return application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

}