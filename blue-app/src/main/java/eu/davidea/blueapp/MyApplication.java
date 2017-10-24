package eu.davidea.blueapp;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import eu.davidea.blueapp.infrastructure.injection.ApiModule;
import eu.davidea.blueapp.infrastructure.injection.ApplicationComponent;
import eu.davidea.blueapp.infrastructure.injection.ApplicationModule;
import eu.davidea.blueapp.infrastructure.injection.DaggerApplicationComponent;
import timber.log.Timber;

public class MyApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApplication", "onCreate called!");
        createLogger();
        createApplicationComponent();
    }

    /**
     * Timber Logger
     */
    private void createLogger() {
        // Logger & CrashReporting
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /**
     * Dagger2 initialization
     */
    private void createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .apiModule(new ApiModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public String getVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return "v" + pInfo.versionName;
        } catch (NameNotFoundException e) {
            return getString(android.R.string.unknownName);
        }
    }

    public int getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    @Override
    public void onLowMemory() {
        Log.wtf("MyApplication", "onLowMemory called!");
        //TODO: onLowMemory save DB now and try to synchronize
        super.onLowMemory();
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable throwable) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (throwable != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(throwable);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(throwable);
                }
            }
        }
    }

    /* Not a real crash reporting library! */
    static final class FakeCrashLibrary {
        static void log(int priority, String tag, String message) {
            // TODO add log entry to circular buffer.
        }

        static void logWarning(Throwable t) {
            // TODO report non-fatal warning.
        }

        static void logError(Throwable t) {
            // TODO report non-fatal error.
        }

        private FakeCrashLibrary() {
        }
    }

}