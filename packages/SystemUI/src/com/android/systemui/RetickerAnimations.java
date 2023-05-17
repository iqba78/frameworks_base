package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;

public class RetickerAnimations {

    static boolean mIsAnimatingTicker;
    
    private static AnimatorSet animatorSet;

    public static void doBounceAnimationIn(View targetView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", -100, 0, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(targetView, "translationX", 0, 17, 0);
        animator.setInterpolator(new BounceInterpolator());
        animator2.setInterpolator(new BounceInterpolator());
        animator.setDuration(500);
        animator2.setStartDelay(500);
        animator2.setDuration(500);
        animator.start();
        animator2.start();
        targetView.setVisibility(View.VISIBLE);
    }

    public static void doBounceAnimationOut(View targetView, View notificationStackScroller) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", 0, -1000, -1000);
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(350);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                notificationStackScroller.setVisibility(View.VISIBLE);
                targetView.setVisibility(View.GONE);
                mIsAnimatingTicker = false;
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    public static void revealAnimation(View targetView) {
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }

        float centerX = targetView.getWidth() / 2f;
        float centerY = targetView.getHeight() / 2f;
        float maxRadius = (float) Math.hypot(centerX, centerY);

        final float initialRadius = 0f;
        final float finalRadius = maxRadius;

        final float initialTranslationY = -maxRadius;
        final float finalTranslationY = 0f;

        final long visibilityDuration = 3000;
        final long animationDuration = visibilityDuration / 2;

        Animator circularRevealAnimator = ViewAnimationUtils.createCircularReveal(
                targetView,
                (int) centerX,
                (int) centerY,
                initialRadius,
                finalRadius
        );

        circularRevealAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        circularRevealAnimator.setDuration(animationDuration);

        ObjectAnimator bounceAnimator = ObjectAnimator.ofFloat(
                targetView,
                "translationY",
                initialTranslationY,
                finalTranslationY
        );
        bounceAnimator.setInterpolator(new BounceInterpolator());
        bounceAnimator.setDuration(animationDuration);

        targetView.setVisibility(View.VISIBLE);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(circularRevealAnimator, bounceAnimator);

        animatorSet.start();
    }

    public static void revealAnimationHide(final View targetView, final View notificationStackScroller) {
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }

        float centerX = targetView.getWidth() / 2f;
        float centerY = targetView.getHeight() / 2f;
        float maxRadius = (float) Math.hypot(centerX, centerY);

        final float initialRadius = maxRadius;
        final float finalRadius = 0f;

        final float initialTranslationY = 0f;
        final float finalTranslationY = -maxRadius;

        final long visibilityDuration = 3000;
        final long animationDuration = visibilityDuration / 2;

        Animator circularRevealAnimator = ViewAnimationUtils.createCircularReveal(
                targetView,
                (int) centerX,
                (int) centerY,
                initialRadius,
                finalRadius
        );

        circularRevealAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        circularRevealAnimator.setDuration(animationDuration);

        ObjectAnimator bounceAnimator = ObjectAnimator.ofFloat(
                targetView,
                "translationY",
                initialTranslationY,
                finalTranslationY
        );
        bounceAnimator.setInterpolator(new BounceInterpolator());
        bounceAnimator.setDuration(animationDuration);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(circularRevealAnimator, bounceAnimator);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                notificationStackScroller.setVisibility(View.VISIBLE);
                targetView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animatorSet.start();
    }

    public static boolean isTickerAnimating() {
	    return mIsAnimatingTicker;
    }

}
