package com.eathemeat.easytimer.ui.widget.calender

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs


class CalendarView : View {

    companion object {
        val FILL_LARGE_INDICATOR: Int = 1
        val NO_FILL_LARGE_INDICATOR: Int = 2
        val SMALL_INDICATOR: Int = 3
    }

    private var animationHandler: AnimationHandler
    private lateinit var calendarController: CalendarController
    private var gestureDetector: GestureDetectorCompat? = null
    private var horizontalScrollEnabled = true

    interface CompactCalendarViewListener {
        fun onDayClick(dateClicked: Date?)
        fun onMonthScroll(firstDayOfNewMonth: Date?)
    }

    interface CompactCalendarAnimationListener {
        fun onOpened()
        fun onClosed()
    }

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            calendarController.onSingleTapUp(e)
            invalidate()
            return super.onSingleTapUp(e)
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (horizontalScrollEnabled) {
                if (abs(distanceX.toDouble()) > 0) {
                    parent.requestDisallowInterceptTouchEvent(true)

                    calendarController.onScroll(e1, e2, distanceX, distanceY)
                    invalidate()
                    return true
                }
            }

            return false
        }
    }



    constructor(context: Context?) : this(context, null) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0){
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        calendarController = CalendarController(
            Paint(), OverScroller(getContext()),
            android.graphics.Rect(), attrs, getContext(), Color.argb(255, 233, 84, 81),
            Color.argb(255, 64, 64, 64), Color.argb(255, 219, 219, 219), VelocityTracker.obtain(),
            Color.argb(255, 100, 68, 65), EventsContainer(Calendar.getInstance()),
            Locale.getDefault(), TimeZone.getDefault()
        )
        gestureDetector = GestureDetectorCompat(getContext(), gestureListener)
        animationHandler = AnimationHandler(calendarController, this)
    }

    fun setAnimationListener(calendarAnimationListener: CompactCalendarAnimationListener?) {
        animationHandler.calendarAnimationListener = calendarAnimationListener
    }

    /*
    Use a custom locale for compact calendar and reinitialise the view.
     */
    fun setLocale(timeZone: TimeZone?, locale: Locale?) {
        calendarController.setLocale(timeZone, locale)
        invalidate()
    }

    /*
    Compact calendar will use the locale to determine the abbreviation to use as the day column names.
    The default is to use the default locale and to abbreviate the day names to one character.
    Setting this to true will displace the short weekday string provided by java.
     */
    fun setUseThreeLetterAbbreviation(useThreeLetterAbbreviation: Boolean) {
        calendarController.setUseWeekDayAbbreviation(useThreeLetterAbbreviation)
        invalidate()
    }

    fun setCalendarBackgroundColor(calenderBackgroundColor: Int) {
        calendarController.calenderBackgroundColor = calenderBackgroundColor
        invalidate()
    }

    /*
    Sets the name for each day of the week. No attempt is made to adjust width or text size based on the length of each day name.
    Works best with 3-4 characters for each day.
     */
    fun setDayColumnNames(dayColumnNames: Array<String>) {
        calendarController.dayColumnNames = dayColumnNames
    }

    fun setFirstDayOfWeek(dayOfWeek: Int) {
        calendarController.setFirstDayOfWeek(dayOfWeek)
        invalidate()
    }

    fun setCurrentSelectedDayBackgroundColor(currentSelectedDayBackgroundColor: Int) {
        calendarController.currentSelectedDayBackgroundColor = currentSelectedDayBackgroundColor
        invalidate()
    }

    fun setCurrentDayBackgroundColor(currentDayBackgroundColor: Int) {
        calendarController.currentDayBackgroundColor = currentDayBackgroundColor
        invalidate()
    }

    fun getHeightPerDay(): Int {
        return calendarController.heightPerDay
    }

    fun setListener(listener: CompactCalendarViewListener?) {
        calendarController.listener = listener
    }

    fun getFirstDayOfCurrentMonth(): Date {
        return calendarController.getFirstDayOfCurrentMonth()
    }

    fun shouldDrawIndicatorsBelowSelectedDays(shouldDrawIndicatorsBelowSelectedDays: Boolean) {
        calendarController.shouldDrawIndicatorsBelowSelectedDays= shouldDrawIndicatorsBelowSelectedDays
    }

    fun setCurrentDate(dateTimeMonth: Date) {
        calendarController.currentDate= dateTimeMonth
        invalidate()
    }

    fun getWeekNumberForCurrentMonth(): Int {
        return calendarController.getWeekNumberForCurrentMonth()
    }

    fun setShouldDrawDaysHeader(shouldDrawDaysHeader: Boolean) {
        calendarController.shouldDrawDaysHeader = shouldDrawDaysHeader
    }

    fun setCurrentSelectedDayTextColor(currentSelectedDayTextColor: Int) {
        calendarController.currentSelectedDayTextColor = currentSelectedDayTextColor
    }

    fun setCurrentDayTextColor(currentDayTextColor: Int) {
        calendarController.currentDayTextColor = currentDayTextColor
    }

    /**
     * see [.addEvent] when adding single events to control if calendar should redraw
     * or [.addEvents]  when adding multiple events
     * @param event
     */
    fun addEvent(event: Event) {
        addEvent(event, true)
    }

    /**
     * Adds an event to be drawn as an indicator in the calendar.
     * If adding multiple events see [.addEvents]} method.
     * @param event to be added to the calendar
     * @param shouldInvalidate true if the view should invalidate
     */
    fun addEvent(event: Event, shouldInvalidate: Boolean) {
        calendarController.addEvent(event)
        if (shouldInvalidate) {
            invalidate()
        }
    }

    /**
     * Adds multiple events to the calendar and invalidates the view once all events are added.
     */
    fun addEvents(events: List<Event>) {
        calendarController.addEvents(events)
        invalidate()
    }

    /**
     * Fetches the events for the date passed in
     * @param date
     * @return
     */
    fun getEvents(date: Date): List<Event> {
        return calendarController.getCalendarEventsFor(date.time)
    }

    /**
     * Fetches the events for the epochMillis passed in
     * @param epochMillis
     * @return
     */
    fun getEvents(epochMillis: Long): List<Event> {
        return calendarController.getCalendarEventsFor(epochMillis)
    }

    /**
     * Fetches the events for the month of the epochMillis passed in and returns a sorted list of events
     * @param epochMillis
     * @return
     */
    fun getEventsForMonth(epochMillis: Long): List<Event> {
        return calendarController.eventsContainer?.getEventsForMonth(epochMillis) ?: listOf()
    }

    /**
     * Fetches the events for the month of the date passed in and returns a sorted list of events
     * @param date
     * @return
     */
    fun getEventsForMonth(date: Date): List<Event> {
        return calendarController.eventsContainer?.getEventsForMonth(date.time) ?: listOf()
    }

    /**
     * Remove the event associated with the Date passed in
     * @param date
     */
    fun removeEvents(date: Date) {
        calendarController.removeEventsFor(date.time)
    }

    fun removeEvents(epochMillis: Long) {
        calendarController.removeEventsFor(epochMillis)
    }

    /**
     * see [.removeEvent] when removing single events to control if calendar should redraw
     * or [.removeEvents] (java.util.List)}  when removing multiple events
     * @param event
     */
    fun removeEvent(event: Event) {
        removeEvent(event, true)
    }

    /**
     * Removes an event from the calendar.
     * If removing multiple events see [.removeEvents]
     *
     * @param event event to remove from the calendar
     * @param shouldInvalidate true if the view should invalidate
     */
    fun removeEvent(event: Event, shouldInvalidate: Boolean) {
        calendarController.removeEvent(event)
        if (shouldInvalidate) {
            invalidate()
        }
    }

    /**
     * Removes multiple events from the calendar and invalidates the view once all events are added.
     */
    fun removeEvents(events: List<Event>) {
        calendarController.removeEvents(events)
        invalidate()
    }

    /**
     * Clears all Events from the calendar.
     */
    fun removeAllEvents() {
        calendarController.removeAllEvents()
        invalidate()
    }

    fun setIsRtl(isRtl: Boolean) {
        calendarController.isRtl = isRtl
    }

    fun shouldSelectFirstDayOfMonthOnScroll(shouldSelectFirstDayOfMonthOnScroll: Boolean) {
        calendarController.shouldSelectFirstDayOfMonthOnScroll= shouldSelectFirstDayOfMonthOnScroll
    }

    fun setCurrentSelectedDayIndicatorStyle(currentSelectedDayIndicatorStyle: Int) {
        calendarController.currentSelectedDayIndicatorStyle= currentSelectedDayIndicatorStyle
        invalidate()
    }

    fun setCurrentDayIndicatorStyle(currentDayIndicatorStyle: Int) {
        calendarController.currentDayIndicatorStyle=currentDayIndicatorStyle
        invalidate()
    }

    fun setEventIndicatorStyle(eventIndicatorStyle: Int) {
        calendarController.eventIndicatorStyle=eventIndicatorStyle
        invalidate()
    }

    private fun checkTargetHeight() {
        check(!(calendarController.targetHeight <= 0)) { "Target height must be set in xml properties in order to expand/collapse CompactCalendar." }
    }

    fun displayOtherMonthDays(displayOtherMonthDays: Boolean) {
        calendarController.displayOtherMonthDays = displayOtherMonthDays
        invalidate()
    }

    fun setTargetHeight(targetHeight: Int) {
        calendarController.targetHeight = targetHeight.toFloat()
        checkTargetHeight()
    }

    fun showCalendar() {
        checkTargetHeight()
        animationHandler.openCalendar()
    }

    fun hideCalendar() {
        checkTargetHeight()
        animationHandler.closeCalendar()
    }

    fun showCalendarWithAnimation() {
        checkTargetHeight()
        animationHandler.openCalendarWithAnimation()
    }

    fun hideCalendarWithAnimation() {
        checkTargetHeight()
        animationHandler.closeCalendarWithAnimation()
    }

    /**
     * Moves the calendar to the right. This will show the next month when [.setIsRtl]
     * is set to false. If in rtl mode, it will show the previous month.
     */
    fun scrollRight() {
        calendarController.scrollRight()
        invalidate()
    }

    /**
     * Moves the calendar to the left. This will show the previous month when [.setIsRtl]
     * is set to false. If in rtl mode, it will show the next month.
     */
    fun scrollLeft() {
        calendarController.scrollLeft()
        invalidate()
    }

    fun isAnimating(): Boolean {
        return animationHandler.isAnimating
    }

    override fun onMeasure(parentWidth: Int, parentHeight: Int) {
        super.onMeasure(parentWidth, parentHeight)
        val width = MeasureSpec.getSize(parentWidth)
        val height = MeasureSpec.getSize(parentHeight)
        if (width > 0 && height > 0) {
            calendarController.onMeasure(width, height, paddingRight, paddingLeft)
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        calendarController.onDraw(canvas)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (calendarController.computeScroll()) {
            invalidate()
        }
    }

    fun shouldScrollMonth(enableHorizontalScroll: Boolean) {
        this.horizontalScrollEnabled = enableHorizontalScroll
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (horizontalScrollEnabled) {
            calendarController.onTouch(event)
            invalidate()
        }

        // on touch action finished (CANCEL or UP), we re-allow the parent container to intercept touch events (scroll inside ViewPager + RecyclerView issue #82)
        if ((event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) && horizontalScrollEnabled) {
            parent.requestDisallowInterceptTouchEvent(false)
        }

        // always allow gestureDetector to detect onSingleTap and scroll events
        return gestureDetector!!.onTouchEvent(event)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        if (this.visibility == GONE) {
            return false
        }
        // Prevents ViewPager from scrolling horizontally by announcing that (issue #82)
        return this.horizontalScrollEnabled
    }

    






}