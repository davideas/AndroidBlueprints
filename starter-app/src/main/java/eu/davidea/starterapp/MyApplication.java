package eu.davidea.starterapp;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class MyApplication extends Application {
	
	private static MyApplication myApp;
	
	public MyApplication() {
		myApp = this;
	}
	
	public static MyApplication getInstance() {
		//The instance is never null.
		//It's created when the App starts.
		return myApp;
	}

	public String getVersionName() {
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			return "v"+pInfo.versionName;
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
	public void onCreate() {
		Log.wtf("MyApplication", "onCreate called!");
		//TODO: Put here strategic initializations
		super.onCreate();
	}
	
	@Override
	public void onLowMemory() {
		Log.wtf("MyApplication", "onLowMemory called!");
		//TODO: onLowMemory save DB now and try to synchronize
		super.onLowMemory();
	}

}