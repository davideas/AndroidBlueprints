package eu.davidea.starterapp.helpers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by henen on 27/07/2017.
 */

public class ResourcesHelper
{
	/* General obj */
	private Activity mActivity;


	/*
	 * ================
	 * Constructor
	 * ================
	 */

	public ResourcesHelper(Activity activity)
	{
		this.mActivity = activity;
	}


	/*
	 * ================
	 * Helper methods
	 * ================
	 */

	/**
	 * Gets drawable from uri.
	 *
	 * @param path the path
	 * @return the drawable from uri
	 */
	public Drawable getDrawableFromUri (@NonNull Activity activity, @NonNull Uri path)
	{
		Drawable result;
		try
		{
			InputStream inputStream = activity.getContentResolver().openInputStream(path);
			result = Drawable.createFromStream(inputStream, path.toString());
		}
		catch (FileNotFoundException e)
		{
			result = activity.getResources().getDrawable(R.mipmap.ic_launcher);
		}

		return result;
	}

	/**
	 * get uri to any resource type
	 *
	 * @param resId - resource id
	 * @return - Uri to resource by given id
	 */
	public Uri getUriToResource (@NonNull Activity activity, @AnyRes int resId)
	{
		Uri resUri = null;
		try
		{
			/** Return a Resources instance for your application's package. */
			Resources res = activity.getResources();
			/**
			 * Creates a Uri which parses the given encoded URI string.
			 * @param uriString an RFC 2396-compliant, encoded URI
			 * @throws NullPointerException if uriString is null
			 * @return Uri for this given uri string
			 */
			resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + res.getResourcePackageName(resId) + '/' + res
					.getResourceTypeName(resId) + '/' + res.getResourceEntryName(resId));
			/** return uri */

		}
		catch (Resources.NotFoundException e)
		{
			e.printStackTrace();
		}

		return resUri;
	}
}
