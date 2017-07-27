package eu.davidea.starterapp.common;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

/**
 * {@link AnimatorListener} implementation that does nothing by default
 */
public abstract class SimpleAnimatorListener implements AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

}