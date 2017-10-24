package eu.davidea.blueapp.ui.common;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * {@link OnPageChangeListener} implementation that does nothing by default
 */
public abstract class SimplePageChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}