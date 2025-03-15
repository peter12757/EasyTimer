package com.eathemeat.easytimer.ui.widget.calender

class AnimationHandler {
    val  HEIGHT_ANIM_DURATION_MILLIS = 650
    val INDICATOR_ANIM_DURATION_MILLIS = 600
    var isAnimating = false
    var  calendarController:CalendarController?
    private CompactCalendarView compactCalendarView;
    private CompactCalendarView.CompactCalendarAnimationListener compactCalendarAnimationListener;

    AnimationHandler(CompactCalendarController compactCalendarController, CompactCalendarView compactCalendarView) {
        this.calendarController = calendarController;
        this.compactCalendarView = compactCalendarView;
    }

    void setCompactCalendarAnimationListener(CompactCalendarView.CompactCalendarAnimationListener compactCalendarAnimationListener){
        this.compactCalendarAnimationListener = compactCalendarAnimationListener;
    }

    void openCalendar() {
        if (isAnimating) {
            return;
        }
        isAnimating = true;
        Animation heightAnim = getCollapsingAnimation(true);
        heightAnim.setDuration(HEIGHT_ANIM_DURATION_MILLIS);
        heightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        calendarController.setAnimationStatus(CalendarController.EXPAND_COLLAPSE_CALENDAR);
        setUpAnimationLisForOpen(heightAnim);
        compactCalendarView.getLayoutParams().height = 0;
        compactCalendarView.requestLayout();
        compactCalendarView.startAnimation(heightAnim);
    }

    void closeCalendar() {
        if (isAnimating) {
            return;
        }
        isAnimating = true;
        Animation heightAnim = getCollapsingAnimation(false);
        heightAnim.setDuration(HEIGHT_ANIM_DURATION_MILLIS);
        heightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        setUpAnimationLisForClose(heightAnim);
        calendarController.setAnimationStatus(CalendarController.EXPAND_COLLAPSE_CALENDAR);
        compactCalendarView.getLayoutParams().height = compactCalendarView.getHeight();
        compactCalendarView.requestLayout();
        compactCalendarView.startAnimation(heightAnim);
    }

    void openCalendarWithAnimation() {
        if (isAnimating) {
            return;
        }
        isAnimating = true;
        final Animator indicatorAnim = getIndicatorAnimator(1f, calendarController.getDayIndicatorRadius());
        final Animation heightAnim = getExposeCollapsingAnimation(true);
        compactCalendarView.getLayoutParams().height = 0;
        compactCalendarView.requestLayout();
        setUpAnimationLisForExposeOpen(indicatorAnim, heightAnim);
        compactCalendarView.startAnimation(heightAnim);
    }

    void closeCalendarWithAnimation() {
        if (isAnimating) {
            return;
        }
        isAnimating = true;
        final Animator indicatorAnim = getIndicatorAnimator(calendarController.getDayIndicatorRadius(), 1f);
        final Animation heightAnim = getExposeCollapsingAnimation(false);
        compactCalendarView.getLayoutParams().height = compactCalendarView.getHeight();
        compactCalendarView.requestLayout();
        setUpAnimationLisForExposeClose(indicatorAnim, heightAnim);
        compactCalendarView.startAnimation(heightAnim);
    }

    private void setUpAnimationLisForExposeOpen(final Animator indicatorAnim, Animation heightAnim) {
        heightAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                calendarController.setAnimationStatus(CalendarController.EXPOSE_CALENDAR_ANIMATION);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                indicatorAnim.start();
            }
        });
        indicatorAnim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                calendarController.setAnimationStatus(CalendarController.ANIMATE_INDICATORS);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                calendarController.setAnimationStatus(CalendarController.IDLE);
                onOpen();
                isAnimating = false;
            }
        });
    }

    private void setUpAnimationLisForExposeClose(final Animator indicatorAnim, Animation heightAnim) {
        heightAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                calendarController.setAnimationStatus(CalendarController.EXPOSE_CALENDAR_ANIMATION);
                indicatorAnim.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                calendarController.setAnimationStatus(CalendarController.IDLE);
                onClose();
                isAnimating = false;
            }
        });
        indicatorAnim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                calendarController.setAnimationStatus(CalendarController.ANIMATE_INDICATORS);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
    }

    @NonNull
    private Animation getExposeCollapsingAnimation(final boolean isCollapsing) {
        Animation heightAnim = getCollapsingAnimation(isCollapsing);
        heightAnim.setDuration(HEIGHT_ANIM_DURATION_MILLIS);
        heightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        return heightAnim;
    }

    @NonNull
    private Animation getCollapsingAnimation(boolean isCollapsing) {
        return new CollapsingAnimation(compactCalendarView, compactCalendarController, compactCalendarController.getTargetHeight(), getTargetGrowRadius(), isCollapsing);
    }

    @NonNull
    private Animator getIndicatorAnimator(float from, float to) {
        ValueAnimator animIndicator = ValueAnimator.ofFloat(from, to);
        animIndicator.setDuration(INDICATOR_ANIM_DURATION_MILLIS);
        animIndicator.setInterpolator(new OvershootInterpolator());
        animIndicator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                calendarController.setGrowFactorIndicator((Float) animation.getAnimatedValue());
                compactCalendarView.invalidate();
            }
        });
        return animIndicator;
    }

    private int getTargetGrowRadius() {
        int heightSq = compactCalendarController.getTargetHeight() * calendarController.getTargetHeight();
        int widthSq = compactCalendarController.getWidth() * calendarController.getWidth();
        return (int) (0.5 * Math.sqrt(heightSq + widthSq));
    }

    private void onOpen() {
        if (compactCalendarAnimationListener != null) {
            compactCalendarAnimationListener.onOpened();
        }
    }

    private void onClose() {
        if (compactCalendarAnimationListener != null) {
            compactCalendarAnimationListener.onClosed();
        }
    }

    private void setUpAnimationLisForOpen(Animation openAnimation) {
        openAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                onOpen();
                isAnimating = false;
            }
        });
    }

    private void setUpAnimationLisForClose(Animation openAnimation) {
        openAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                onClose();
                isAnimating = false;
            }
        });
    }

    public boolean isAnimating() {
        return isAnimating;
    }
}