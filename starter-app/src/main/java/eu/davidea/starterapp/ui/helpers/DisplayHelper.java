package eu.davidea.starterapp.ui.helpers;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

/**
 * @author henen
 * @since 27/07/2017
 */
public final class DisplayHelper {

	/*
     * ================
	 * Constructor
	 * ================
	 */

    private DisplayHelper() {
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
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Dp to px int.
     *
     * @param dp the amount of dp wanted
     * @return the amount of px equal to dp
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Gets status bar height.
     *
     * @return the status bar height
     */
    public static int getStatusBarHeight(Activity activity) {
        int height;

        Resources myResources = activity.getResources();
        int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = activity.getResources()
                    .getDimensionPixelSize(idStatusBarHeight);
            Toast.makeText(activity, "Status Bar Height = " + height, Toast.LENGTH_LONG)
                    .show();
        } else {
            height = 0;
            Toast.makeText(activity, "Resources NOT found", Toast.LENGTH_LONG)
                    .show();
        }

        return height;
    }

    /**
     * Sets window layout rtl.
     */
    public static void setWindowLayoutRtl(Activity activity) {
        if (activity.getWindow()
                .getDecorView()
                .getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            activity.getWindow()
                    .getDecorView()
                    .setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

}