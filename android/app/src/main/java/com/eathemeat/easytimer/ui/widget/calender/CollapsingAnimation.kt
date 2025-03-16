package com.eathemeat.easytimer.ui.widget.calender

import android.view.animation.Animation
import android.view.animation.Transformation

class CollapsingAnimation(var view: CalendarView,var compactCalendarController: CalendarController, var targetHeight:Int = 0, var targetGrowRadius: Int = 0,
                          var down: Boolean = false) :
    Animation() {


    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        var grow = 0f
        val newHeight: Int
        if (down) {
            newHeight = (targetHeight * interpolatedTime).toInt()
            grow = (interpolatedTime * (targetGrowRadius * 2))
        } else {
            val progress = 1 - interpolatedTime
            newHeight = (targetHeight * progress).toInt()
            grow = (progress * (targetGrowRadius * 2))
        }
        compactCalendarController.growFactor = grow
        view.getLayoutParams().height = newHeight
        view.requestLayout()
    }


    override fun willChangeBounds(): Boolean {
        return true
    }
}