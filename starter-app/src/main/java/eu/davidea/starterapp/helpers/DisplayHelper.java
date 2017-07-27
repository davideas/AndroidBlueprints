package eu.davidea.starterapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

/**
 * Created by henen on 27/07/2017.
 */

public class DisplayHelper
{
	/* General obj */
	private Activity mActivity;


	/*
	 * ================
	 * Constructor
	 * ================
	 */

	public DisplayHelper(Activity activity)
	{
		this.mActivity = activity;
	}


	/*
	 * ================
	 * Helper methods
	 * ================
	 */

	/**
	 * Px to dp int.
	 *
	 * @param px the amount of px wanted
	 * @return the amount of dp equal to px
	 */
	public int pxToDp(int px) {
		return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}

	/**
	 * Dp to px int.
	 *
	 * @param dp the amount of dp wanted
	 * @return the amount of px equal to dp
	 */
	public int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	/**
	 * Gets status bar height.
	 *
	 * @return the status bar height
	 */
	public int getStatusBarHeight()
	{
		int height;

		Resources myResources = mActivity.getResources();
		int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");
		if (idStatusBarHeight > 0)
		{
			height = mActivity.getResources()
					.getDimensionPixelSize(idStatusBarHeight);
			Toast.makeText(mActivity, "Status Bar Height = " + height, Toast.LENGTH_LONG)
					.show();
		}
		else
		{
			height = 0;
			Toast.makeText(mActivity, "Resources NOT found", Toast.LENGTH_LONG)
					.show();
		}

		return height;
	}

	/**
	 * Sets window layout rtl.
	 */
	public void setWindowLayoutRtl()
	{
		if (mActivity.getWindow()
				.getDecorView()
				.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR)
		{
			mActivity.getWindow()
					.getDecorView()
					.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
		}
	}
}
