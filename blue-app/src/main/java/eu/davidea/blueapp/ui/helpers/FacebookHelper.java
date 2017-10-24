package eu.davidea.blueapp.ui.helpers;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.davidea.blueapp.R;
import eu.davidea.blueapp.persistence.preferences.PreferencesService;


public class FacebookHelper {
    /* Constants */
    public static final int RC_FACEBOOK_SIGN_IN = 64206;
    private static final int RC_FACEBOOK_SHARE = 64207;

    private static final FacebookHelper mInstance = new FacebookHelper();

    public static final String FACEBOOK_PREFERENCES = "facebook_prefs";
    public static final String FACEBOOK_USER_FIRST_NAME = "first_name";
    public static final String FACEBOOK_USER_LAST_NAME = "last_name";
    public static final String FACEBOOK_USER_ID = "id";
    public static final String FACEBOOK_USER_EMAIL = "email";
    public static final String FACEBOOK_USER_GENDER = "gender";
    public static final String FACEBOOK_USER_BIRTHDAY = "birthday";
    public static final String FACEBOOK_USER_LOCATION = "location";
    public static final String FACEBOOK_USER_IMAGE_URL = "picture";

    private static final String FACEBOOK_PERMISSION_PUBLIC_PROFILE = "public_profile";
    private static final String FACEBOOK_PERMISSION_EMAIL = "email";
    private static final String FACEBOOK_PERMISSION_USER_FRIENDS = "user_friends"; // Only friends that have logged in to my app

    /* General obj */
    private CallbackManager mCallbackManager;
    private FacebookLoginResultCallback mFacebookLoginResultCallBack;
    private LoginResult mLoginResult;
    private AccessTokenTracker mAccessTokenTracker;
    private ProfileTracker mProfileTracker;
    private AccessToken mFacebookAccessToken;
    private BitmapDrawable mUserImage;
    private PreferencesService preferences;

    /* User data */
    private List<String> mReadPermissionsRequested;
    private List<String> mUserDataFieldsRequested;
    private Map<String, String> mUserData;

    /**
     * The interface Facebook login result callback.
     */
    /* Interfaces */
    public interface FacebookLoginResultCallback {

        /**
         * On facebook login success.
         *
         * @param result the result
         */
        void onFacebookLoginSuccess(LoginResult result);

        /**
         * On facebook login cancel.
         */
        void onFacebookLoginCancel();

        /**
         * On facebook login error.
         *
         * @param e the e
         */
        void onFacebookLoginError(FacebookException e);

        /**
         * On facebook login image download failed.
         */
        void onFacebookLoginImageDownloadFailed();
    }

    /**
     * The interface Facebook share result callback.
     */
    public interface FacebookShareResultCallback {
        void onFacebookShareSuccess(Sharer.Result result);

        void onFacebookShareCancel();

        void onFacebookShareError(FacebookException error);

        void onFacebookShareCannotShowDialog();
    }

    /*
     * =================
     * Constructor
     * =================
     */

    private FacebookHelper() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static FacebookHelper getInstance() {
        return mInstance;
    }

    /**
     * Init helper.
     * Once initialized, the class will:
     * 1.Restore and hold user data if connected {@link #mUserData}
     * 2.Initialize profile tracker to track changes of the user profile {@link #mProfileTracker}
     * 3.Initialize access token to track changes of the Facebook access token {@link #mAccessTokenTracker}
     *
     * @param activity the activity
     */
    public void initHelper(@NonNull Activity activity) {
        mUserData = new HashMap<>();
        initUser(activity);
        initAccessTokenTracker();
        initProfileTracker();
    }

    /*
     * =================
     * Actions
     * =================
     */

    /**
     * Sign in to google account - without Google's log in button.
     *
     * @param activity the activity
     * @param callback the callback to receive the method's result
     */
    public void signIn(@NonNull Activity activity, @NonNull final FacebookLoginResultCallback callback) {
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginResultCallBack = callback;
        registerCallBackManager(activity);

        LoginManager.getInstance()
                .setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK)
                .logInWithPublishPermissions(activity, null);
    }

    /**
     * Sign in to google account - with Google's log in button.
     *
     * @param activity    the activity
     * @param callback    the callback to receive the method's result
     * @param loginButton the login button associated with this sign in
     */
    public void signIn(@NonNull Activity activity, @NonNull final FacebookLoginResultCallback callback, @NonNull LoginButton loginButton) {
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginResultCallBack = callback;
        initLoginButton(activity, loginButton);
        registerCallBackManager(activity, loginButton);

    }

    /**
     * Sign out.
     */
    public void signOut() {
        LoginManager.getInstance().logOut();
    }

    /**
     * Share content on facebook wall
     *
     * @param activity the activity
     * @param callback the callback to receive a result of this share
     * @param url      the url you wish to share
     * @param quote    the text you wish to associate with the url shared
     */
    private void shareLinkOnFacebook(@NonNull final Activity activity, @NonNull final FacebookShareResultCallback callback, @NonNull String url, @Nullable String quote) {
        ShareDialog shareDialog = new ShareDialog(activity);

        shareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                callback.onFacebookShareSuccess(result);
            }

            @Override
            public void onCancel() {
                callback.onFacebookShareCancel();
            }

            @Override
            public void onError(FacebookException error) {
                callback.onFacebookShareError(error);
            }
        }, RC_FACEBOOK_SHARE);


        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentUrl(Uri.parse(url))
                    .setQuote(quote)
                    .build();
            shareDialog.show(linkContent);
        } else {
            callback.onFacebookShareCannotShowDialog();
        }
    }

    /*
     * =================
     * Account info
     * =================
     */

    /**
     * Is logged in boolean.
     *
     * @return the boolean result of if the user is currently connected
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private List<String> getFacebookReadPermissions() {
        List<String> facebookPermissions = new ArrayList<>();
        facebookPermissions.add(FACEBOOK_PERMISSION_PUBLIC_PROFILE);
        facebookPermissions.add(FACEBOOK_PERMISSION_EMAIL);

        if (mReadPermissionsRequested != null) {
            for (String permission : mReadPermissionsRequested) {
                facebookPermissions.add(permission);
            }
        }

        return facebookPermissions;
    }

    /*
     * =================
     * Init methods
     * =================
     */

    /**
     * Init user if connected.
     *
     * @param activity the activity
     */
    private void initUser(Activity activity) {
        if (isLoggedIn()) {
            // restore user data
            String id = preferences.getString(FACEBOOK_USER_ID, null);
            String fistName = preferences.getString(FACEBOOK_USER_FIRST_NAME, null);
            String lastName = preferences.getString(FACEBOOK_USER_LAST_NAME, null);
            String email = preferences.getString(FACEBOOK_USER_EMAIL, null);
            String birthday = preferences.getString(FACEBOOK_USER_BIRTHDAY, null);
            String gender = preferences.getString(FACEBOOK_USER_GENDER, null);
            String location = preferences.getString(FACEBOOK_USER_LOCATION, null);
            String imageUrl = preferences.getString(FACEBOOK_USER_IMAGE_URL, null);

            // init data
            mUserData.put(FACEBOOK_USER_ID, id);
            mUserData.put(FACEBOOK_USER_FIRST_NAME, fistName);
            mUserData.put(FACEBOOK_USER_LAST_NAME, lastName);
            mUserData.put(FACEBOOK_USER_EMAIL, email);
            mUserData.put(FACEBOOK_USER_BIRTHDAY, birthday);
            mUserData.put(FACEBOOK_USER_GENDER, gender);
            mUserData.put(FACEBOOK_USER_LOCATION, location);
            mUserData.put(FACEBOOK_USER_IMAGE_URL, imageUrl);

            downloadImageUrl(activity, imageUrl);
        }
    }

    /**
     * Init Google log in button.
     *
     * @param activity the activity
     */
    private void initLoginButton(Activity activity, LoginButton loginButton) {
        loginButton.setReadPermissions(getFacebookReadPermissions());
        loginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
    }

    /**
     * Register call back manager.
     *
     * @param activity the activity
     */
    private void registerCallBackManager(final Activity activity) {
        LoginManager.getInstance()
                .registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mLoginResult = loginResult;
                        getUserProfile(activity);
                    }

                    @Override
                    public void onCancel() {
                        mFacebookLoginResultCallBack.onFacebookLoginCancel();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        mFacebookLoginResultCallBack.onFacebookLoginError(error);
                    }
                });
    }

    /**
     * Register call back manager to Google log in button.
     *
     * @param activity    the activity
     * @param loginButton the login button
     */
    private void registerCallBackManager(final Activity activity, LoginButton loginButton) {
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLoginResult = loginResult;
                getUserProfile(activity);
            }

            @Override
            public void onCancel() {
                mFacebookLoginResultCallBack.onFacebookLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                mFacebookLoginResultCallBack.onFacebookLoginError(error);
            }
        });
    }

    private void initAccessTokenTracker() {
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                // If the access token is available already assign it.
                mFacebookAccessToken = AccessToken.getCurrentAccessToken();
                //                Toast.makeText(mActivity, "Facebook current Access Token changed", Toast.LENGTH_SHORT)
                //                        .show();
            }
        };
    }

    private void initProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //                Toast.makeText(mActivity, "Facebook current profile changed", Toast.LENGTH_SHORT)
                //                        .show();
            }
        };
    }

    /*
     * =================
     * Helper methods
     * =================
     */

    /**
     * Get requested user data from Facebook
     */
    private void getUserProfile(final Activity activity) {
        GraphRequest request = GraphRequest.newMeRequest(mLoginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                // Get facebook data from login
                parseFacebookUserData(activity, object);
            }
        });
        Bundle parameters = new Bundle();
        String[] params = {FACEBOOK_USER_ID, FACEBOOK_USER_FIRST_NAME, FACEBOOK_USER_LAST_NAME, FACEBOOK_USER_EMAIL, FACEBOOK_USER_BIRTHDAY, FACEBOOK_USER_GENDER, FACEBOOK_USER_LOCATION, FACEBOOK_USER_IMAGE_URL};
        parameters.putString("fields", createStringParams(params));
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Extract data from object
     *
     * @param object json object received from facebook log in
     */
    private void parseFacebookUserData(Activity activity, JSONObject object) {
        HashMap<String, String> userPrefs = new HashMap<>();
        String imageUrl = null;
        try {
            String id = object.getString("id");
            mUserData.put(FACEBOOK_USER_ID, id);
            userPrefs.put(FACEBOOK_USER_ID, id);


            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                mUserData.put(FACEBOOK_USER_IMAGE_URL, profile_pic.toString());
                userPrefs.put(FACEBOOK_USER_ID, profile_pic.toString());
                imageUrl = profile_pic.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (object.has("first_name")) {
                String value = object.getString("first_name");
                mUserData.put(FACEBOOK_USER_FIRST_NAME, value);
                userPrefs.put(FACEBOOK_USER_FIRST_NAME, value);
            }
            if (object.has("last_name")) {
                String value = object.getString("last_name");
                mUserData.put(FACEBOOK_USER_LAST_NAME, value);
                userPrefs.put(FACEBOOK_USER_ID, value);
            }
            if (object.has("email")) {
                String value = object.getString("email");
                mUserData.put(FACEBOOK_USER_EMAIL, value);
                userPrefs.put(FACEBOOK_USER_ID, value);
            }
            if (object.has("gender")) {
                String value = object.getString("gender");
                mUserData.put(FACEBOOK_USER_GENDER, value);
                userPrefs.put(FACEBOOK_USER_ID, value);
            }
            if (object.has("birthday")) {
                String value = object.getString("birthday");
                mUserData.put(FACEBOOK_USER_BIRTHDAY, value);
                userPrefs.put(FACEBOOK_USER_ID, value);
            }
            if (object.has("location")) {
                String value = object.getString("location");
                mUserData.put(FACEBOOK_USER_LOCATION, value);
                userPrefs.put(FACEBOOK_USER_ID, value);
            }
        } catch (JSONException e) {
            // TODO: 26/07/2017 log error
        }

        preferences.putStringsFromMap(userPrefs);
        if (imageUrl != null) {
            downloadImageUrl(activity, imageUrl);
        }
    }

    private void downloadImageUrl(final Activity activity, final String imageUrl) {
        final SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                mUserImage = new BitmapDrawable(activity.getResources(), bitmap);
                mFacebookLoginResultCallBack.onFacebookLoginSuccess(mLoginResult);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                mFacebookLoginResultCallBack.onFacebookLoginImageDownloadFailed();
            }
        };

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher_round);
                Glide.with(activity.getApplicationContext())
                        .asBitmap()
                        .apply(options)
                        .load(imageUrl)
                        .into(target);
            }
        });
    }

    /**
     * Create single string containing and formatted appropriately
     *
     * @param params Array of permissions to receive from Facebook
     * @return Formatted string
     */
    private String createStringParams(String[] params) {

        if (mUserDataFieldsRequested != null) {
            ArrayList<String> paramsList = new ArrayList<>(Arrays.asList(params));
            for (String param : mUserDataFieldsRequested) {
                paramsList.add(param);
            }
            params = new String[paramsList.size()];
            params = paramsList.toArray(params);
        }

        StringBuilder builder = new StringBuilder();

        for (String param : params) {
            builder.append(param + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /*
     * =================
     * Getter methods
     * =================
     */

    /**
     * Gets facebook callback manager.
     *
     * @return the facebook callback manager
     */
    public CallbackManager getFacebookCallbackManager() {
        return this.mCallbackManager;
    }

    /**
     * Gets facebook access token tracker.
     *
     * @return the facebook access token tracker
     */
    public AccessTokenTracker getFacebookAccessTokenTracker() {
        return this.mAccessTokenTracker;
    }

    /**
     * Gets facebook profile tracker.
     *
     * @return the facebook profile tracker
     */
    public ProfileTracker getFacebookProfileTracker() {
        return this.mProfileTracker;
    }

    /**
     * Gets facebook access token.
     *
     * @return the facebook access token
     */
    public AccessToken getFacebookAccessToken() {
        return this.mFacebookAccessToken;
    }

    /**
     * Gets user specific data field.
     * The user data keys are the constants at the start of this class, for example:
     * {@value FACEBOOK_PERMISSION_EMAIL}
     *
     * @param field the field/key you wish to receive
     * @return the user data field
     */
    public String getUserDataField(String field) {
        return mUserData.get(field);
    }

    /**
     * Gets all user data in key value Map.
     * The user data keys are the constants at the start of this class, for example:
     * {@value FACEBOOK_PERMISSION_EMAIL}
     *
     * @return the user data
     */
    public Map<String, String> getUserData() {
        return this.mUserData;
    }

    public Drawable getUserImage() {
        return this.mUserImage;
    }

    /**
     * Gets facebook hash to register in Facebook developer's console.
     */
    public void getFacebookHash(@NonNull Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager()
                    .getPackageInfo("il.co.cambium.sport5radio", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(activity, "PackageManager.NameNotFoundException ", Toast.LENGTH_SHORT)
                    .show();
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(activity, "NoSuchAlgorithmException", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /*
     * =================
     * Setter methods
     * =================
     */

    /**
     * Default read permission asked for are:
     * <p>
     * {@value FACEBOOK_PERMISSION_PUBLIC_PROFILE}
     * {@value FACEBOOK_PERMISSION_EMAIL}
     * <p>
     * If you require more read permissions, you may pass them here in a form of a list
     * After {@link #initHelper(Activity)} ,
     * And before {@link #signIn(Activity, FacebookLoginResultCallback)}, {@link #signIn(Activity, FacebookLoginResultCallback, LoginButton)}
     *
     * @param readPermissions the read permissions
     */
    public void setReadPermissions(List<String> readPermissions) {
        this.mReadPermissionsRequested = readPermissions;
    }

    /**
     * Default user data fetched are:
     * <p>
     * {@value FACEBOOK_USER_FIRST_NAME}
     * {@value FACEBOOK_USER_LAST_NAME}
     * {@value FACEBOOK_USER_EMAIL}
     * {@value FACEBOOK_USER_BIRTHDAY}
     * {@value FACEBOOK_USER_ID}
     * {@value FACEBOOK_USER_GENDER}
     * {@value FACEBOOK_USER_LOCATION}
     * {@value FACEBOOK_USER_IMAGE_URL}
     * <p>
     * If you require more user data, you may pass the params here in a form of a list
     *
     * @param userDataRequested the user data fields requested
     */
    public void setUserDataRequested(List<String> userDataRequested) {
        this.mUserDataFieldsRequested = userDataRequested;
    }


}
