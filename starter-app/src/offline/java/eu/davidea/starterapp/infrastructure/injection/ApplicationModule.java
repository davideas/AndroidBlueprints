package eu.davidea.starterapp.infrastructure.injection;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.davidea.starterapp.MyApplication;
import eu.davidea.starterapp.persistence.db.StarterDatabase;
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
    MyApplication provideApplication() {
        return application;
    }

}