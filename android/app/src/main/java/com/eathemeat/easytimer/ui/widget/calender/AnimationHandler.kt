package com.eathemeat.easytimer.ui.widget.calender

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.OvershootInterpolator
import com.eathemeat.easytimer.ui.widget.calender.CalendarController.Companion.ANIMATE_INDICATORS
import com.eathemeat.easytimer.ui.widget.calender.CalendarController.Companion.EXPAND_COLLAPSE_CALENDAR
import com.eathemeat.easytimer.ui.widget.calender.CalendarController.Companion.EXPOSE_CALENDAR_ANIMATION
import com.eathemeat.easytimer.ui.widget.calender.CalendarController.Companion.IDLE
import kotlin.math.sqrt


class AnimationHandler(val calendarController:CalendarController,val calendarView:CalendarView) {
    val  HEIGHT_ANIM_DURATION_MILLIS = 650
    val INDICATOR_ANIM_DURATION_MILLIS = 600
    var isAnimating = false

    var  calendarAnimationListener:CalendarView.CompactCalendarAnimationListener? = null

    fun openCalendar() {
        if (isAnimating) {
            return
        }
        isAnimating = true
        val heightAnim: Animation = getCollapsingAnimation(true)
        heightAnim.setDuration(HEIGHT_ANIM_DURATION_MILLIS.toLong())
        heightAnim.setInterpolator(AccelerateDecelerateInterpolator())
        calendarController.animationStatus = EXPAND_COLLAPSE_CALENDAR
        setUpAnimationLisForOpen(heightAnim)
        calendarView.getLayoutParams().height = 0
        calendarView.requestLayout()
        calendarView.startAnimation(heightAnim)
    }

    fun closeCalendar() {
        if (isAnimating) {
            return
        }
        isAnimating = true
        val heightAnim = getCollapsingAnimation(false)
        heightAnim.duration = HEIGHT_ANIM_DURATION_MILLIS.toLong()
        heightAnim.interpolator = AccelerateDecelerateInterpolator()
        setUpAnimationLisForClose(heightAnim)
        calendarController.animationStatus = EXPAND_COLLAPSE_CALENDAR
        calendarView.layoutParams.height = calendarView.height
        calendarView.requestLayout()
        calendarView.startAnimation(heightAnim)
    }

    fun closeCalendarWithAnimation() {
        if (isAnimating) {
            return
        }
        isAnimating = true
        val indicatorAnim =
            getIndicatorAnimator(calendarController.bigCircleIndicatorRadius, 1f)
        val heightAnim = getExposeCollapsingAnimation(false)
        calendarView.getLayoutParams().height = calendarView.getHeight()
        calendarView.requestLayout()
        setUpAnimationLisForExposeClose(indicatorAnim, heightAnim)
        calendarView.startAnimation(heightAnim)
    }

    fun openCalendarWithAnimation() {
        if (isAnimating) {
            return
        }
        isAnimating = true
        val indicatorAnim: Animator =
            getIndicatorAnimator(1f, calendarController.bigCircleIndicatorRadius)
        val heightAnim: Animation = getExposeCollapsingAnimation(true)
        calendarView.getLayoutParams().height = 0
        calendarView.requestLayout()
        setUpAnimationLisForExposeOpen(indicatorAnim, heightAnim)
        calendarView.startAnimation(heightAnim)
    }

    private fun setUpAnimationLisForExposeOpen(indicatorAnim: Animator, heightAnim: Animation) {
        heightAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                calendarController.animationStatus=EXPOSE_CALENDAR_ANIMATION
            }

            override fun onAnimationEnd(animation: Animation) {
                indicatorAnim.start()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        indicatorAnim.addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                calendarController.animationStatus=ANIMATE_INDICATORS
            }

            override fun onAnimationEnd(animation: Animator) {
                calendarController.animationStatus=IDLE
                onOpen()
                isAnimating = false
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    private fun setUpAnimationLisForExposeClose(indicatorAnim: Animator, heightAnim: Animation) {
        heightAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                calendarController.animationStatus = EXPOSE_CALENDAR_ANIMATION
                indicatorAnim.start()
            }

            override fun onAnimationEnd(animation: Animation) {
                calendarController.animationStatus = IDLE
                onClose()
                isAnimating = false
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        })
        indicatorAnim.addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                calendarController.animationStatus = ANIMATE_INDICATORS
            }

            override fun onAnimationEnd(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getIndicatorAnimator(from: Float, to: Float): Animator {
        val animIndicator = ValueAnimator.ofFloat(from, to)
        animIndicator.setDuration(INDICATOR_ANIM_DURATION_MILLIS.toLong())
        animIndicator.interpolator = OvershootInterpolator()
        animIndicator.addUpdateListener { animation ->
            calendarController.growfactorIndicator= animation.animatedValue as Float
            calendarView.invalidate()
        }
        return animIndicator
    }

    private fun getExposeCollapsingAnimation(isCollapsing: Boolean): Animation {
        val heightAnim = getCollapsingAnimation(isCollapsing)
        heightAnim.duration = HEIGHT_ANIM_DURATION_MILLIS.toLong()
        heightAnim.interpolator = AccelerateDecelerateInterpolator()
        return heightAnim
    }

    private fun getCollapsingAnimation(isCollapsing: Boolean): Animation {
        return CollapsingAnimation(
            calendarView,
            calendarController,
            calendarController.targetHeight.toInt(),
            getTargetGrowRadius(),
            isCollapsing
        )
    }

    private fun getTargetGrowRadius(): Int {
        val heightSq: Float = sqrt(calendarController.targetHeight)
        val widthSq: Float = sqrt(calendarController.width)
        return (0.5 * sqrt(heightSq + widthSq)).toInt()
    }

    private fun onOpen() {
            calendarAnimationListener?.onOpened()
    }

    private fun onClose() {
            calendarAnimationListener?.onClosed()
    }

    private fun setUpAnimationLisForOpen(openAnimation: Animation) {
        openAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation) {
                onOpen()
                isAnimating = false
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun setUpAnimationLisForClose(openAnimation: Animation) {
        openAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation) {
                onClose()
                isAnimating = false
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }



}