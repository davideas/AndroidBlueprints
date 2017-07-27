package eu.davidea.starterapp.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.starterapp.R;


@SuppressWarnings("WeakerAccess")
public final class Utils {

	private static int colorAccent = -1;

	private Utils() {
	}

	public static Point getScreenDimensions(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);

		Point point = new Point();
		point.set(dm.widthPixels, dm.heightPixels);
		return point;
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		return context.getResources().getDisplayMetrics();
	}

	public static float dpToPx(Context context, float dp) {
		return Math.round(dp * getDisplayMetrics(context).density);
	}

	/**
	 * Acceptable pattern: dd MMM yyyy HH:mm:ss z
	 *
	 * @param date    the date to format
	 * @param pattern format
	 * @return The date formatted
	 * @throws NullPointerException     if the given date or pattern is null
	 * @throws IllegalArgumentException if the given pattern is invalid
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		return dateFormat.format(date);
	}

	/**
	 * API 19
	 *
	 * @see VERSION_CODES#KITKAT
	 */
	public static boolean hasKitkat() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
	}

	/**
	 * API 21
	 *
	 * @see VERSION_CODES#LOLLIPOP
	 */
	public static boolean hasLollipop() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP;
	}

	/**
	 * API 23
	 *
	 * @see VERSION_CODES#M
	 */
	public static boolean hasMarshmallow() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.M;
	}

	/**
	 * API 24
	 *
	 * @see VERSION_CODES#N
	 */
	public static boolean hasNougat() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.N;
	}

	/**
	 * Adjusts the alpha of a color.
	 *
	 * @param color the color
	 * @param alpha the alpha value we want to set 0-255
	 * @return the adjusted color
	 */
	public static int adjustAlpha(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
		return (alpha << 24) | (color & 0x00ffffff);
	}

	@TargetApi(VERSION_CODES.LOLLIPOP)
	public static int getColorAccent(Context context) {
		if (colorAccent < 0) {
			int accentAttr = FlexibleUtils.hasLollipop() ? android.R.attr.colorAccent : R.attr.colorAccent;
			TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{accentAttr});
			colorAccent = androidAttr.getColor(0, 0xFF009688); //Default: material_deep_teal_500
			androidAttr.recycle();
		}
		return colorAccent;
	}

	@SuppressWarnings("deprecation")
	public static Spanned fromHtmlCompat(String text) {
		if (hasNougat()) {
			return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
		} else {
			return Html.fromHtml(text);
		}
	}

	@SuppressWarnings("deprecation")
	public static void textAppearanceCompat(TextView textView, int resId) {
		if (hasMarshmallow()) {
			textView.setTextAppearance(resId);
		} else {
			textView.setTextAppearance(textView.getContext(), resId);
		}
	}

	/**
	 * Show Soft Keyboard with new Thread
	 *
	 * @param activity
	 */
	public static void hideSoftInput(final Activity activity) {
		if (activity.getCurrentFocus() != null) {
			new Runnable() {
				public void run() {
					InputMethodManager imm =
							(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
				}
			}.run();
		}
	}

	/**
	 * Hide Soft Keyboard from Dialogs with new Thread
	 *
	 * @param context
	 * @param view
	 */
	public static void hideSoftInputFrom(final Context context, final View view) {
		new Runnable() {
			@Override
			public void run() {
				InputMethodManager imm =
						(InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}.run();
	}

	/**
	 * Show Soft Keyboard with new Thread
	 *
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(final Context context, final View view) {
		new Runnable() {
			@Override
			public void run() {
				InputMethodManager imm =
						(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}.run();
	}

	/**
	 * Create the reveal effect animation
	 *
	 * @param view the View to reveal
	 * @param cx   coordinate X
	 * @param cy   coordinate Y
	 */
	@TargetApi(VERSION_CODES.LOLLIPOP)
	public static void reveal(final View view, int cx, int cy) {
		if (!hasLollipop()) {
			view.setVisibility(View.VISIBLE);
			return;
		}

		//Get the final radius for the clipping circle
		int finalRadius = Math.max(view.getWidth(), view.getHeight());

		//Create the animator for this view (the start radius is zero)
		Animator animator =
				ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

		//Make the view visible and start the animation
		view.setVisibility(View.VISIBLE);
		animator.start();
	}

	/**
	 * Create the un-reveal effect animation
	 *
	 * @param view the View to hide
	 * @param cx   coordinate X
	 * @param cy   coordinate Y
	 */
	@TargetApi(VERSION_CODES.LOLLIPOP)
	public static void unReveal(final View view, int cx, int cy) {
		if (!hasLollipop()) {
			view.setVisibility(View.GONE);
			return;
		}

		//Get the initial radius for the clipping circle
		int initialRadius = view.getWidth();

		//Create the animation (the final radius is zero)
		Animator animator =
				ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

		//Make the view invisible when the animation is done
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				view.setVisibility(View.GONE);
			}
		});

		//Start the animation
		animator.start();
	}

	/**
	 * Locks the device window in landscape mode
	 */
	public static void lockOrientationLandscape(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * Locks the device window in portrait mode
	 */
	public static void lockOrientationPortrait(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * Locks the device window in actual screen mode
	 */
	public static void lockOrientation(Activity activity) {
		Display display = ((WindowManager) activity.
				getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int rotation = display.getRotation();
		int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

		switch (activity.getResources().getConfiguration().orientation) {
			case Configuration.ORIENTATION_LANDSCAPE:
				if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
					orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				else
					orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
			case Configuration.ORIENTATION_PORTRAIT:
				if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270)
					orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				else
					orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
		}
		//noinspection ResourceType
		activity.setRequestedOrientation(orientation);
	}

	/**
	 * Unlocks the device window in user defined screen mode.
	 */
	public static void unlockOrientation(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
	}

}