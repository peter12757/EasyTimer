package com.eathemeat.easytimer.ui.widget.calender

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.GestureDetectorCompat
import java.util.Date


class CalenderView : View {

    companion object {
        val FILL_LARGE_INDICATOR: Int = 1
        val NO_FILL_LARGE_INDICATOR: Int = 2
        val SMALL_INDICATOR: Int = 3
    }

    interface CompactCalendarViewListener {
        fun onDayClick(dateClicked: Date?)
        fun onMonthScroll(firstDayOfNewMonth: Date?)
    }

    private var animationHandler: AnimationHandler? = null
    private var calendarController: CalendarController? = null
    private var gestureDetector: GestureDetectorCompat? = null
    private val horizontalScrollEnabled = true

    constructor(context: Context?) : this(context, null) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0){
    }

//    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
//        compactCalendarController = CompactCalendarController(
//            Paint(), OverScroller(getContext()),
//            android.graphics.Rect(), attrs, getContext(), Color.argb(255, 233, 84, 81),
//            Color.argb(255, 64, 64, 64), Color.argb(255, 219, 219, 219), VelocityTracker.obtain(),
//            Color.argb(255, 100, 68, 65), EventsContainer(Calendar.getInstance()),
//            Locale.getDefault(), TimeZone.getDefault()
//        )
//        gestureDetector = GestureDetectorCompat(getContext(), gestureListener)
//        animationHandler = AnimationHandler(
//            compactCalendarController,
//            this
//        )
//    }
}