package eu.davidea.starterapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import eu.davidea.starterapp.MyApplication;
import eu.davidea.starterapp.R;
import eu.davidea.starterapp.viewmodels.message.MessageViewModel;
import eu.davidea.starterapp.viewmodels.user.AnonymousUser;
import eu.davidea.starterapp.viewmodels.user.UserViewModel;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject @Named("activity")
    CompositeDisposable compositeDisposable;

    MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Logger
        Timber.tag(TAG);
        Timber.d("onCreate");

        // Inject dependencies
        MyApplication application = (MyApplication) getApplication();
        application.getApplicationComponent()
                .inject(this);

        // Views
        TextView text = findViewById(R.id.hello_message);
        text.setText(getString(R.string.hello, "World"));

        // Load User
        UserViewModel model = ViewModelProviders
                .of(this, viewModelFactory)
                .get(UserViewModel.class);
        model.getUser().observe(this, resource -> {
            assert resource != null;
            if (resource.data != null) {
                Timber.d("Result from UserViewModel status=%s, data=%s",
                        resource.status, resource.data.getName());
                text.setText(getString(R.string.hello, resource.data.getName()));
            } else {
                Timber.w("Empty Result from UserViewModel status=%s", resource.status);
                Timber.e(resource.message);
            }
        });
        // Offline Flavor returns an AnonymousUser
        model.login(AnonymousUser.ANONYMOUS, "password");


        messageViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MessageViewModel.class);

        compositeDisposable.add(messageViewModel.getConversation(1L, 1L)
                .subscribe(messages -> {
                    if (messages != null && !messages.isEmpty()) {
                        Timber.i("Loaded %s messages", messages.size());
                    } else {
                        Timber.i("No message found");
                    }
                }, throwable -> Timber.e(throwable, "Exception getting messages")));

        messageViewModel.loadConversation(1L, 1L);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO: On changing Theme change icon colors too
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getIcon() != null) {
                DrawableCompat.setTint(menuItem.getIcon(), Color.WHITE);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //TODO: This is for sub activities, don't use for MainActivity
            //super.onBackPressed();
            //return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeBasic();
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        // Dispose subscriptions
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        super.onDestroy();
    }
}