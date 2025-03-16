package com.eathemeat.easytimer.ui.widget.calender


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.ui.widget.calender.CalendarView.Companion.FILL_LARGE_INDICATOR
import com.eathemeat.easytimer.ui.widget.calender.CalendarView.Companion.NO_FILL_LARGE_INDICATOR
import com.eathemeat.easytimer.ui.widget.calender.CalendarView.Companion.SMALL_INDICATOR
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.sqrt


class CalendarController {
    companion object {
        val IDLE: Int = 0
        val EXPOSE_CALENDAR_ANIMATION: Int = 1
        val EXPAND_COLLAPSE_CALENDAR: Int = 2
        val ANIMATE_INDICATORS: Int = 3
        val VELOCITY_UNIT_PIXELS_PER_SECOND: Int = 1000
        val LAST_FLING_THRESHOLD_MILLIS: Int = 300
        val DAYS_IN_WEEK: Int = 7
        val SNAP_VELOCITY_DIP_PER_SECOND: Float = 400f
        val ANIMATION_SCREEN_SET_DURATION_MILLIS: Float = 700f
    }

    var eventIndicatorStyle: Int = SMALL_INDICATOR
    var currentDayIndicatorStyle: Int = FILL_LARGE_INDICATOR
    var currentSelectedDayIndicatorStyle: Int = FILL_LARGE_INDICATOR
    var paddingWidth = 40F
    var paddingHeight = 40F
    var textHeight = 0
    var textWidth = 0
    var widthPerDay = 0
    var monthsScrolledSoFar = 0
    var heightPerDay = 0
    var textSize = 30F
    var width = 0F
    var height = 0F
    var paddingRight = 0
    var paddingLeft = 0
    var maximumVelocity = 0F
    var densityAdjustedSnapVelocity = 0
    var distanceThresholdForAutoScroll = 0
    var targetHeight = 0F
    var animationStatus = 0
    var firstDayOfWeekToDraw: Int = Calendar.MONDAY
    var xIndicatorOffset = 0f
    var multiDayIndicatorStrokeWidth = 0f
    var bigCircleIndicatorRadius = 0f
    var smallIndicatorRadius = 0f
    var growFactor = 0f
    var screenDensity = 1f
    var growfactorIndicator = 0f
    var distanceX = 0f
    var lastAutoScrollFromFling: Long = 0

    var useThreeLetterAbbreviation = false
    var isSmoothScrolling = false
    var isScrolling = false
    var shouldDrawDaysHeader = true
    var shouldDrawIndicatorsBelowSelectedDays = false
    var displayOtherMonthDays = false
    var shouldSelectFirstDayOfMonthOnScroll = true
    var isRtl = false

    var listener: CalendarView.CompactCalendarViewListener? = null
    var velocityTracker: VelocityTracker? = null
    var currentDirection = Direction.NONE
    var currentDate: Date = Date()
        set(dateTimeMonth) {
            distanceX = 0f
            monthsScrolledSoFar = 0
            accumulatedScrollOffset.x = 0f
            scroller!!.startScroll(0, 0, 0, 0)
            field = Date(dateTimeMonth.time)
            currentCalender?.setTime(currentDate)
            todayCalender = Calendar.getInstance(timeZone, locale)
            setToMidnight(currentCalender)
        }
    var locale: Locale
    lateinit var currentCalender: Calendar
    lateinit var todayCalender: Calendar
    var calendarWithFirstDayOfMonth: Calendar? = null
    var eventsCalendar: Calendar? = null
    var eventsContainer: EventsContainer? = null
    val accumulatedScrollOffset = PointF()
    var scroller: OverScroller? = null
    var textSizeRect: Rect
    var dayPaint: Paint = Paint()
    val background: Paint = Paint()

    var dayColumnNames: Array<String>? = null
        set(value) {
            require(!(field == null || field?.size != 7)) { "Column names cannot be null and must contain a value for each day of the week" }
            field = dayColumnNames
        }

    // colors
    var multiEventIndicatorColor = 0
    var currentDayBackgroundColor:Int = 0
    var currentDayTextColor = 0
    var calenderTextColor:Int= 0
    var currentSelectedDayBackgroundColor = 0
    var currentSelectedDayTextColor = 0
    var calenderBackgroundColor: Int = Color.White.toArgb()
    var otherMonthDaysTextColor = 0
    var timeZone: TimeZone

    /**
     * Only used in onDrawCurrentMonth to temporarily calculate previous month days
     */
    var tempPreviousMonthCalendar: Calendar? = null

    enum class Direction {
        NONE, HORIZONTAL, VERTICAL
    }

    constructor(
        dayPaint: Paint, scroller: OverScroller?, textSizeRect: Rect, attrs: AttributeSet?,
        context: Context?, currentDayBackgroundColor: Int, calenderTextColor: Int,
        currentSelectedDayBackgroundColor: Int, velocityTracker: VelocityTracker,
        multiEventIndicatorColor: Int, eventsContainer: EventsContainer?,
        locale: Locale, timeZone: TimeZone
    ) {
        this.dayPaint = dayPaint
        this.scroller = scroller
        this.textSizeRect = textSizeRect
        this.currentDayBackgroundColor = currentDayBackgroundColor
        this.calenderTextColor = calenderTextColor
        this.currentSelectedDayBackgroundColor = currentSelectedDayBackgroundColor
        this.otherMonthDaysTextColor = calenderTextColor
        this.velocityTracker = velocityTracker
        this.multiEventIndicatorColor = multiEventIndicatorColor
        this.eventsContainer = eventsContainer
        this.locale = locale
        this.timeZone = timeZone
        this.displayOtherMonthDays = false
        loadAttributes(attrs, context)
        init(context)
    }

    fun loadAttributes(attrs: AttributeSet?, context: Context?) {
        if (attrs != null && context != null) {
            val typedArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.CompactCalendarView, 0, 0)
            try {
                currentDayBackgroundColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarCurrentDayBackgroundColor,
                    currentDayBackgroundColor
                )
                calenderTextColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarTextColor,
                    calenderTextColor
                )
                currentDayTextColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarCurrentDayTextColor,
                    calenderTextColor.toInt()
                )
                otherMonthDaysTextColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarOtherMonthDaysTextColor,
                    otherMonthDaysTextColor
                )
                currentSelectedDayBackgroundColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarCurrentSelectedDayBackgroundColor,
                    currentSelectedDayBackgroundColor
                )
                currentSelectedDayTextColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarCurrentSelectedDayTextColor,
                    calenderTextColor.toInt()
                )
                calenderBackgroundColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarBackgroundColor,
                    calenderBackgroundColor
                )
                multiEventIndicatorColor = typedArray.getColor(
                    R.styleable.CompactCalendarView_compactCalendarMultiEventIndicatorColor,
                    multiEventIndicatorColor
                )
                textSize = typedArray.getDimensionPixelSize(
                    R.styleable.CompactCalendarView_compactCalendarTextSize,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        textSize,
                        context.resources.displayMetrics
                    ).toInt()
                ).toFloat()
                targetHeight = typedArray.getDimensionPixelSize(
                    R.styleable.CompactCalendarView_compactCalendarTargetHeight,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        targetHeight,
                        context.resources.displayMetrics
                    ).toInt()
                ).toFloat()
                eventIndicatorStyle = typedArray.getInt(
                    R.styleable.CompactCalendarView_compactCalendarEventIndicatorStyle,
                    SMALL_INDICATOR
                )
                currentDayIndicatorStyle = typedArray.getInt(
                    R.styleable.CompactCalendarView_compactCalendarCurrentDayIndicatorStyle,
                    FILL_LARGE_INDICATOR
                )
                currentSelectedDayIndicatorStyle = typedArray.getInt(
                    R.styleable.CompactCalendarView_compactCalendarCurrentSelectedDayIndicatorStyle,
                    FILL_LARGE_INDICATOR
                )
                displayOtherMonthDays = typedArray.getBoolean(
                    R.styleable.CompactCalendarView_compactCalendarDisplayOtherMonthDays,
                    displayOtherMonthDays
                )
                shouldSelectFirstDayOfMonthOnScroll = typedArray.getBoolean(
                    R.styleable.CompactCalendarView_compactCalendarShouldSelectFirstDayOfMonthOnScroll,
                    shouldSelectFirstDayOfMonthOnScroll
                )
            } finally {
                typedArray.recycle()
            }
        }
    }

    fun init(context: Context?) {
        currentCalender = Calendar.getInstance(timeZone, locale).apply {
            minimalDaysInFirstWeek =1
            setMinimalDaysInFirstWeek(1)
            setTime(currentDate)
        }
        todayCalender = Calendar.getInstance(timeZone, locale).apply {
            setMinimalDaysInFirstWeek(1)
            time = Date()
            setToMidnight(todayCalender)
        }
        calendarWithFirstDayOfMonth = Calendar.getInstance(timeZone, locale).apply {

        }
        eventsCalendar = Calendar.getInstance(timeZone, locale).apply {

        }
        tempPreviousMonthCalendar = Calendar.getInstance(timeZone, locale).apply {
            setMinimalDaysInFirstWeek(1)
        }
        setFirstDayOfWeek(firstDayOfWeekToDraw)
        setUseWeekDayAbbreviation(false)
        dayPaint.apply {
            setTextAlign(Paint.Align.CENTER)
            setStyle(Paint.Style.STROKE)
            setFlags(Paint.ANTI_ALIAS_FLAG)
            setTypeface(Typeface.SANS_SERIF)
            setTextSize(textSize)
            setColor(calenderTextColor)
            getTextBounds("31", 0, "31".length, textSizeRect)
        }
        textHeight = textSizeRect?.height()?.times(3) ?: 0
        textWidth = (textSizeRect?.width() ?: 0) * 2
        setCalenderToFirstDayOfMonth(
            calendarWithFirstDayOfMonth,
            currentDate,

            -monthsScrolledSoFar,
            0
        )

        initScreenDensityRelatedValues(context)

        xIndicatorOffset = 3.5f * screenDensity

        //scale small indicator by screen density
        smallIndicatorRadius = 2.5f * screenDensity

        //just set a default growFactor to draw full calendar when initialised
        growFactor = Int.MAX_VALUE.toFloat()
    }

    fun initScreenDensityRelatedValues(context: Context?) {
        if (context != null) {
            screenDensity = context.resources.displayMetrics.density
            val configuration: ViewConfiguration = ViewConfiguration
                .get(context)
            densityAdjustedSnapVelocity = (screenDensity * SNAP_VELOCITY_DIP_PER_SECOND).toInt()
            maximumVelocity = configuration.getScaledMaximumFlingVelocity().toFloat()

            val dm = context.resources.displayMetrics
            multiDayIndicatorStrokeWidth =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, dm)
        }
    }

    fun setCalenderToFirstDayOfMonth(
        calendarWithFirstDayOfMonth: Calendar?,
        currentDate: Date,
        scrollOffset: Int,
        monthOffset: Int
    ) {
        setMonthOffset(calendarWithFirstDayOfMonth, currentDate, scrollOffset, monthOffset)
        calendarWithFirstDayOfMonth?.set(Calendar.DAY_OF_MONTH, 1)
    }

    fun setMonthOffset(
        calendarWithFirstDayOfMonth: Calendar?,
        currentDate: Date,
        scrollOffset: Int,
        monthOffset: Int
    ) {
        calendarWithFirstDayOfMonth?.apply {
            setTime(currentDate)
            add(Calendar.MONTH, scrollOffset + monthOffset)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

    }

    fun setFirstDayOfWeek(day: Int) {
        require(!(day < 1 || day > 7)) { "Day must be an int between 1 and 7 or DAY_OF_WEEK from Java Calendar class. For more information please see Calendar.DAY_OF_WEEK." }
        this.firstDayOfWeekToDraw = day
        setUseWeekDayAbbreviation(useThreeLetterAbbreviation)
        eventsCalendar?.setFirstDayOfWeek(day)
        calendarWithFirstDayOfMonth?.setFirstDayOfWeek(day)
        todayCalender?.setFirstDayOfWeek(day)
        currentCalender?.setFirstDayOfWeek(day)
        tempPreviousMonthCalendar?.setFirstDayOfWeek(day)
    }

    fun scrollRight() {
        if (isRtl) {
            scrollPrev()
        } else {
            scrollNext()
        }
    }

    fun scrollLeft() {
        if (isRtl) {
            scrollNext()
        } else {
            scrollPrev()
        }
    }

    fun scrollNext() {
        monthsScrolledSoFar = monthsScrolledSoFar - 1
        accumulatedScrollOffset!!.x = (monthsScrolledSoFar * width).toFloat()
        if (shouldSelectFirstDayOfMonthOnScroll) {
            setCalenderToFirstDayOfMonth(
                calendarWithFirstDayOfMonth,
                currentCalender!!.time,
                0,
                1
            )
            currentDate = calendarWithFirstDayOfMonth!!.getTime()
        }
        performMonthScrollCallback()
    }

    fun scrollPrev() {
        monthsScrolledSoFar = monthsScrolledSoFar + 1
        accumulatedScrollOffset.x = (monthsScrolledSoFar * width).toFloat()
        if (shouldSelectFirstDayOfMonthOnScroll) {
            setCalenderToFirstDayOfMonth(
                calendarWithFirstDayOfMonth,
                currentCalender!!.getTime(),
                0,
                -1
            )
            currentDate = calendarWithFirstDayOfMonth!!.getTime()
        }
        performMonthScrollCallback()
    }

    fun setLocale(timeZone: TimeZone?, locale: Locale?) {
        requireNotNull(locale) { "Locale cannot be null." }
        requireNotNull(timeZone) { "TimeZone cannot be null." }
        this.locale = locale
        this.timeZone = timeZone
        this.eventsContainer = EventsContainer(Calendar.getInstance(this.timeZone, this.locale))
        // passing null will not re-init density related values - and that's ok
        init(null)
    }

    fun setUseWeekDayAbbreviation(useThreeLetterAbbreviation: Boolean) {
        this.useThreeLetterAbbreviation = useThreeLetterAbbreviation
        this.dayColumnNames =
            getWeekdayNames(locale, firstDayOfWeekToDraw, this.useThreeLetterAbbreviation)
    }



    fun onMeasure(width: Int, height: Int, paddingRight: Int, paddingLeft: Int) {
        widthPerDay = (width) / DAYS_IN_WEEK
        heightPerDay = if (targetHeight > 0) targetHeight.toInt() / 7 else height / 7
        this.width = width.toFloat()
        this.distanceThresholdForAutoScroll = (width * 0.50).toInt()
        this.height = height.toFloat()
        this.paddingRight = paddingRight
        this.paddingLeft = paddingLeft

        //makes easier to find radius
        bigCircleIndicatorRadius = getInterpolatedBigCircleIndicator()

        // scale the selected day indicators slightly so that event indicators can be drawn below
        bigCircleIndicatorRadius =
            if (shouldDrawIndicatorsBelowSelectedDays && eventIndicatorStyle == SMALL_INDICATOR) bigCircleIndicatorRadius * 0.85f else bigCircleIndicatorRadius
    }

    //assume square around each day of width and height = heightPerDay and get diagonal line length
    //interpolate height and radius
    //https://en.wikipedia.org/wiki/Linear_interpolation
    fun getInterpolatedBigCircleIndicator(): Float {
        val x0: Float = textSizeRect!!.height().toFloat()
        val x1 = heightPerDay.toFloat() // take into account indicator offset
        val x: Float =
            (x1 + textSizeRect!!.height()) / 2f // pick a point which is almost half way through heightPerDay and textSizeRect
        val y1 = 0.5 * sqrt(((x1 * x1) + (x1 * x1)).toDouble())
        val y0 = 0.5 * sqrt(((x0 * x0) + (x0 * x0)).toDouble())

        return (y0 + ((y1 - y0) * ((x - x0) / (x1 - x0)))).toFloat()
    }

    fun removeAllEvents() {
        eventsContainer!!.removeAllEvents()
    }

    fun onDraw(canvas: Canvas) {
        paddingWidth = widthPerDay.toFloat() / 2
        paddingHeight = heightPerDay.toFloat() / 2
        calculateXPositionOffset()

        if (animationStatus == EXPOSE_CALENDAR_ANIMATION) {
            drawCalendarWhileAnimating(canvas)
        } else if (animationStatus == ANIMATE_INDICATORS) {
            drawCalendarWhileAnimatingIndicators(canvas)
        } else {
            drawCalenderBackground(canvas)
            drawScrollableCalender(canvas)
        }
    }

    fun drawCalendarWhileAnimatingIndicators(canvas: Canvas) {
        dayPaint.setColor(calenderBackgroundColor)
        dayPaint.setStyle(Paint.Style.FILL)
        canvas.drawCircle(0F, 0F, growFactor, dayPaint)
        dayPaint.setStyle(Paint.Style.STROKE)
        dayPaint.setColor(Color.White.toArgb())
        drawScrollableCalender(canvas)
    }

    fun drawCalendarWhileAnimating(canvas: Canvas) {
        background.setColor(calenderBackgroundColor)
        background.setStyle(Paint.Style.FILL)
        canvas.drawCircle(0F, 0F, growFactor, background)
        dayPaint.setStyle(Paint.Style.STROKE)
        dayPaint.setColor(Color.White.toArgb())
        drawScrollableCalender(canvas)
    }

    fun onSingleTapUp(e: MotionEvent) {
        // Don't handle single tap when calendar is scrolling and is not stationary
        if (isScrolling2()) {
            return
        }

        val dayColumn = Math.round((paddingLeft + e.x - paddingWidth - paddingRight) / widthPerDay)
        val dayRow = Math.round((e.y - paddingHeight) / heightPerDay)

        setCalenderToFirstDayOfMonth(
            calendarWithFirstDayOfMonth,
            currentDate,
            monthsScrolledSoFar(),
            0
        )

        val firstDayOfMonth = getDayOfWeek(calendarWithFirstDayOfMonth!!)

        var dayOfMonth = ((dayRow - 1) * 7) - firstDayOfMonth
        dayOfMonth += if (isRtl) {
            6 - dayColumn
        } else {
            dayColumn
        }
        if (dayOfMonth < calendarWithFirstDayOfMonth!!.getActualMaximum(Calendar.DAY_OF_MONTH)
            && dayOfMonth >= 0
        ) {
            calendarWithFirstDayOfMonth!!.add(Calendar.DATE, dayOfMonth)

            currentCalender!!.setTimeInMillis(calendarWithFirstDayOfMonth!!.getTimeInMillis())
            performOnDayClickCallback(currentCalender!!.getTime())
        }
    }

    // Add a little leeway buy checking if amount scrolled is almost same as expected scroll
    // as it maybe off by a few pixels
    fun isScrolling2(): Boolean {
        val scrolledX = abs(accumulatedScrollOffset.x.toDouble()).toFloat()
        val expectedScrollX = abs((width * monthsScrolledSoFar).toDouble()).toInt()
        return scrolledX < expectedScrollX - 5 || scrolledX > expectedScrollX + 5
    }

    fun performOnDayClickCallback(date: Date) {
        listener?.also { it.onDayClick(date) }
    }

    fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        //ignore scrolling callback if already smooth scrolling
        if (isSmoothScrolling) {
            return true
        }

        if (currentDirection == Direction.NONE) {
            currentDirection = if (abs(distanceX.toDouble()) > abs(distanceY.toDouble())) {
                Direction.HORIZONTAL
            } else {
                Direction.VERTICAL
            }
        }

        isScrolling = true
        this.distanceX = distanceX
        return true
    }

    fun onTouch(event: MotionEvent): Boolean {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }

        velocityTracker?.addMovement(event)

        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!scroller!!.isFinished) {
                scroller!!.abortAnimation()
            }
            isSmoothScrolling = false
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            velocityTracker?.addMovement(event)
            velocityTracker?.computeCurrentVelocity(500)
        } else if (event.action == MotionEvent.ACTION_UP) {
            handleHorizontalScrolling()
            velocityTracker?.recycle()
            velocityTracker?.clear()
            velocityTracker = null
            isScrolling = false
        }
        return false
    }

    fun snapBackScroller() {
        val remainingScrollAfterFingerLifted1 =
            (accumulatedScrollOffset.x - (monthsScrolledSoFar * width))
        scroller!!.startScroll(
            accumulatedScrollOffset.x.toInt(),
            0,
            -remainingScrollAfterFingerLifted1.toInt(),
            0
        )
    }

    fun handleHorizontalScrolling() {
        val velocityX = computeVelocity()
        handleSmoothScrolling(velocityX)

        currentDirection = Direction.NONE
        setCalenderToFirstDayOfMonth(
            calendarWithFirstDayOfMonth,
            currentDate,
            monthsScrolledSoFar(),
            0
        )

        if (calendarWithFirstDayOfMonth!!.get(Calendar.MONTH) != currentCalender!!.get(Calendar.MONTH) && shouldSelectFirstDayOfMonthOnScroll) {
            setCalenderToFirstDayOfMonth(currentCalender, currentDate, monthsScrolledSoFar(), 0)
        }
    }

    fun computeVelocity(): Int {
        velocityTracker?.computeCurrentVelocity(VELOCITY_UNIT_PIXELS_PER_SECOND, maximumVelocity)
        return velocityTracker?.getXVelocity()?.toInt()!!
    }

    fun handleSmoothScrolling(velocityX: Int) {
        val distanceScrolled = (accumulatedScrollOffset.x - (width * monthsScrolledSoFar)).toInt()
        val isEnoughTimeElapsedSinceLastSmoothScroll =
            System.currentTimeMillis() - lastAutoScrollFromFling > LAST_FLING_THRESHOLD_MILLIS
        if (velocityX > densityAdjustedSnapVelocity && isEnoughTimeElapsedSinceLastSmoothScroll) {
            scrollPreviousMonth()
        } else if (velocityX < -densityAdjustedSnapVelocity && isEnoughTimeElapsedSinceLastSmoothScroll) {
            scrollNextMonth()
        } else if (isScrolling && distanceScrolled > distanceThresholdForAutoScroll) {
            scrollPreviousMonth()
        } else if (isScrolling && distanceScrolled < -distanceThresholdForAutoScroll) {
            scrollNextMonth()
        } else {
            isSmoothScrolling = false
            snapBackScroller()
        }
    }

    fun scrollNextMonth() {
        lastAutoScrollFromFling = System.currentTimeMillis()
        monthsScrolledSoFar = monthsScrolledSoFar - 1
        performScroll()
        isSmoothScrolling = true
        performMonthScrollCallback()
    }

    fun scrollPreviousMonth() {
        lastAutoScrollFromFling = System.currentTimeMillis()
        monthsScrolledSoFar = monthsScrolledSoFar + 1
        performScroll()
        isSmoothScrolling = true
        performMonthScrollCallback()
    }

    fun performMonthScrollCallback() {
        listener?.onMonthScroll(getFirstDayOfCurrentMonth())
    }

    fun performScroll() {
        val targetScroll = (monthsScrolledSoFar) * width
        val remainingScrollAfterFingerLifted = targetScroll - accumulatedScrollOffset.x
        scroller!!.startScroll(
            accumulatedScrollOffset.x.toInt(), 0, (remainingScrollAfterFingerLifted).toInt(), 0,
            (abs(
                (remainingScrollAfterFingerLifted).toInt().toDouble()
            ) / width.toFloat() * ANIMATION_SCREEN_SET_DURATION_MILLIS).toInt()
        )
    }


    fun getWeekNumberForCurrentMonth(): Int {
        val calendar: Calendar = Calendar.getInstance(timeZone, locale)
        calendar.setTime(currentDate)
        return calendar.get(Calendar.WEEK_OF_MONTH)
    }

    fun getFirstDayOfCurrentMonth(): Date {
        val calendar: Calendar = Calendar.getInstance(timeZone, locale)
        calendar.setTime(currentDate)
        calendar.add(Calendar.MONTH, monthsScrolledSoFar())
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        setToMidnight(calendar)
        return calendar.getTime()
    }


    fun setToMidnight(calendar: Calendar?) {
        calendar?.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

    }

    fun addEvent(event: Event) {
        eventsContainer?.addEvent(event)
    }

    fun addEvents(events: List<Event>) {
        eventsContainer?.addEvents(events)
    }

    fun getCalendarEventsFor(epochMillis: Long): List<Event> {
        return eventsContainer?.getEventsFor(epochMillis)!!
    }

    fun getCalendarEventsForMonth(epochMillis: Long): List<Event>? {
        eventsContainer?.let {
            return it.getEventsForMonth(epochMillis)
        }
        return ArrayList()
    }

    fun removeEventsFor(epochMillis: Long) {
        eventsContainer?.removeEventByEpochMillis(epochMillis)
    }

    fun removeEvent(event: Event) {
        eventsContainer?.removeEvent(event)
    }

    fun removeEvents(events: List<Event>) {
        eventsContainer?.removeEvents(events)
    }


    fun onDown(e: MotionEvent?): Boolean {
        scroller!!.forceFinished(true)
        return true
    }

    fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        scroller!!.forceFinished(true)
        return true
    }

    fun computeScroll(): Boolean {
        if (scroller!!.computeScrollOffset()) {
            accumulatedScrollOffset.x = scroller!!.currX.toFloat()
            return true
        }
        return false
    }

    fun drawScrollableCalender(canvas: Canvas) {
        if (isRtl) {
            drawNextMonth(canvas, -1)
            drawCurrentMonth(canvas)
            drawPreviousMonth(canvas, 1)
        } else {
            drawPreviousMonth(canvas, -1)
            drawCurrentMonth(canvas)
            drawNextMonth(canvas, 1)
        }
    }

    fun drawNextMonth(canvas: Canvas, offset: Int) {
        setCalenderToFirstDayOfMonth(
            calendarWithFirstDayOfMonth,
            currentDate,
            -monthsScrolledSoFar,
            offset
        )
        drawMonth(canvas, calendarWithFirstDayOfMonth!!, (width.toInt() * (-monthsScrolledSoFar + 1)))
    }

    fun drawCurrentMonth(canvas: Canvas) {
        setCalenderToFirstDayOfMonth(
            calendarWithFirstDayOfMonth,
            currentDate,
            monthsScrolledSoFar(),
            0
        )
        drawMonth(canvas, calendarWithFirstDayOfMonth!!, width.toInt() * -monthsScrolledSoFar)
    }

    fun monthsScrolledSoFar(): Int {
        return if (isRtl) monthsScrolledSoFar else -monthsScrolledSoFar
    }

    fun drawPreviousMonth(canvas: Canvas, offset: Int) {
        setCalenderToFirstDayOfMonth(
            calendarWithFirstDayOfMonth,
            currentDate,
            -monthsScrolledSoFar,
            offset
        )
        drawMonth(canvas, calendarWithFirstDayOfMonth!!, (width.toInt() * (-monthsScrolledSoFar - 1)))
    }

    fun calculateXPositionOffset() {
        if (currentDirection == Direction.HORIZONTAL) {
            accumulatedScrollOffset.x -= distanceX
        }
    }

    fun drawCalenderBackground(canvas: Canvas) {
        dayPaint.setColor(calenderBackgroundColor)
        dayPaint.setStyle(Paint.Style.FILL)
        canvas.drawRect(0F, 0F, width, height, dayPaint)
        dayPaint.setStyle(Paint.Style.STROKE)
        dayPaint.color=calenderTextColor
    }

    fun drawEvents(canvas: Canvas, currentMonthToDrawCalender: Calendar, offset: Int) {
        val currentMonth: Int = currentMonthToDrawCalender.get(Calendar.MONTH)
        val uniqEvents: List<Events>? = eventsContainer!!.getEventsForMonthAndYear(
            currentMonth,
            currentMonthToDrawCalender.get(Calendar.YEAR)
        )

        val shouldDrawCurrentDayCircle = currentMonth == todayCalender!!.get(Calendar.MONTH)
        val shouldDrawSelectedDayCircle = currentMonth == currentCalender!!.get(Calendar.MONTH)

        val todayDayOfMonth: Int = todayCalender!!.get(Calendar.DAY_OF_MONTH)
        val currentYear: Int = todayCalender!!.get(Calendar.YEAR)
        val selectedDayOfMonth: Int = currentCalender!!.get(Calendar.DAY_OF_MONTH)
        val indicatorOffset = bigCircleIndicatorRadius / 2
        if (uniqEvents != null) {
            for (i in uniqEvents.indices) {
                val events = uniqEvents[i]
                val timeMillis: Long = events.timeInMillis
                eventsCalendar?.setTimeInMillis(timeMillis)

                var dayOfWeek = getDayOfWeek(eventsCalendar!!)
                if (isRtl) {
                    dayOfWeek = 6 - dayOfWeek
                }

                val weekNumberForMonth: Int = eventsCalendar!!.get(Calendar.WEEK_OF_MONTH)
                val xPosition =
                    widthPerDay * dayOfWeek + paddingWidth + paddingLeft + accumulatedScrollOffset.x + offset - paddingRight
                var yPosition = (weekNumberForMonth * heightPerDay + paddingHeight).toFloat()

                if (((animationStatus == EXPOSE_CALENDAR_ANIMATION || animationStatus == ANIMATE_INDICATORS) && xPosition >= growFactor) || yPosition >= growFactor) {
                    // only draw small event indicators if enough of the calendar is exposed
                    continue
                } else if (animationStatus == EXPAND_COLLAPSE_CALENDAR && yPosition >= growFactor) {
                    // expanding animation, just draw event indicators if enough of the calendar is visible
                    continue
                } else if (animationStatus == EXPOSE_CALENDAR_ANIMATION && (eventIndicatorStyle == FILL_LARGE_INDICATOR || eventIndicatorStyle == NO_FILL_LARGE_INDICATOR)) {
                    // Don't draw large indicators during expose animation, until animation is done
                    continue
                }

                val eventsList: List<Event> = events.events
                val dayOfMonth: Int = eventsCalendar!!.get(Calendar.DAY_OF_MONTH)
                val eventYear: Int = eventsCalendar!!.get(Calendar.YEAR)
                val isSameDayAsCurrentDay =
                    shouldDrawCurrentDayCircle && (todayDayOfMonth == dayOfMonth) && (eventYear == currentYear)
                val isCurrentSelectedDay =
                    shouldDrawSelectedDayCircle && (selectedDayOfMonth == dayOfMonth)

                if (shouldDrawIndicatorsBelowSelectedDays || (!shouldDrawIndicatorsBelowSelectedDays && !isSameDayAsCurrentDay && !isCurrentSelectedDay) || animationStatus == EXPOSE_CALENDAR_ANIMATION) {
                    if (eventIndicatorStyle == FILL_LARGE_INDICATOR || eventIndicatorStyle == NO_FILL_LARGE_INDICATOR) {
                        if (!eventsList.isEmpty()) {
                            val event: Event = eventsList[0]
                            drawEventIndicatorCircle(canvas, xPosition, yPosition, event.color)
                        }
                    } else {
                        yPosition += indicatorOffset
                        // offset event indicators to draw below selected day indicators
                        // this makes sure that they do no overlap
                        if (shouldDrawIndicatorsBelowSelectedDays && (isSameDayAsCurrentDay || isCurrentSelectedDay)) {
                            yPosition += indicatorOffset
                        }

                        if (eventsList.size >= 3) {
                            drawEventsWithPlus(canvas, xPosition, yPosition, eventsList)
                        } else if (eventsList.size == 2) {
                            drawTwoEvents(canvas, xPosition, yPosition, eventsList)
                        } else if (eventsList.size == 1) {
                            drawSingleEvent(canvas, xPosition, yPosition, eventsList)
                        }
                    }
                }
            }
        }
    }

    fun drawSingleEvent(
        canvas: Canvas,
        xPosition: Float,
        yPosition: Float,
        eventsList: List<Event>
    ) {
        val event: Event = eventsList[0]
        drawEventIndicatorCircle(canvas, xPosition, yPosition, event.color)
    }

    fun drawTwoEvents(
        canvas: Canvas,
        xPosition: Float,
        yPosition: Float,
        eventsList: List<Event>
    ) {
        //draw fist event just left of center
        drawEventIndicatorCircle(
            canvas,
            xPosition + (xIndicatorOffset * -1),
            yPosition,
            eventsList[0].color
        )
        //draw second event just right of center
        drawEventIndicatorCircle(
            canvas,
            xPosition + (xIndicatorOffset * 1),
            yPosition,
            eventsList[1].color
        )
    }

    //draw 2 eventsByMonthAndYearMap followed by plus indicator to show there are more than 2 eventsByMonthAndYearMap
    fun drawEventsWithPlus(
        canvas: Canvas,
        xPosition: Float,
        yPosition: Float,
        eventsList: List<Event>
    ) {
        // k = size() - 1, but since we don't want to draw more than 2 indicators, we just stop after 2 iterations so we can just hard k = -2 instead
        // we can use the below loop to draw arbitrary eventsByMonthAndYearMap based on the current screen size, for example, larger screens should be able to
        // display more than 2 evens before displaying plus indicator, but don't draw more than 3 indicators for now
        var j = 0
        var k = -2
        while (j < 3) {
            val event: Event = eventsList[j]
            val xStartPosition = xPosition + (xIndicatorOffset * k)
            if (j == 2) {
                dayPaint.setColor(multiEventIndicatorColor)
                dayPaint.setStrokeWidth(multiDayIndicatorStrokeWidth)
                canvas.drawLine(
                    xStartPosition - smallIndicatorRadius,
                    yPosition,
                    xStartPosition + smallIndicatorRadius,
                    yPosition,
                    dayPaint
                )
                canvas.drawLine(
                    xStartPosition,
                    yPosition - smallIndicatorRadius,
                    xStartPosition,
                    yPosition + smallIndicatorRadius,
                    dayPaint
                )
                dayPaint.setStrokeWidth(0F)
            } else {
                drawEventIndicatorCircle(canvas, xStartPosition, yPosition, event.color)
            }
            j++
            k += 2
        }
    }

    // zero based indexes used internally so instead of returning range of 1-7 like calendar class
    // it returns 0-6 where 0 is Sunday instead of 1
    fun getDayOfWeek(calendar: Calendar): Int {
        var dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeekToDraw
        dayOfWeek = if (dayOfWeek < 0) 7 + dayOfWeek else dayOfWeek
        return dayOfWeek
    }

    fun drawMonth(canvas: Canvas, monthToDrawCalender: Calendar, offset: Int) {
        drawEvents(canvas, monthToDrawCalender, offset)

        //offset by one because we want to start from Monday
        val firstDayOfMonth = getDayOfWeek(monthToDrawCalender)

        val isSameMonthAsToday =
            monthToDrawCalender.get(Calendar.MONTH) == todayCalender.get(Calendar.MONTH)
        val isSameYearAsToday =
            monthToDrawCalender.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR)
        val isSameMonthAsCurrentCalendar =
            monthToDrawCalender.get(Calendar.MONTH) == currentCalender.get(Calendar.MONTH) &&
                    monthToDrawCalender.get(Calendar.YEAR) == currentCalender.get(Calendar.YEAR)
        val todayDayOfMonth: Int = todayCalender!!.get(Calendar.DAY_OF_MONTH)
        val isAnimatingWithExpose = animationStatus == EXPOSE_CALENDAR_ANIMATION

        val maximumMonthDay: Int = monthToDrawCalender.getActualMaximum(Calendar.DAY_OF_MONTH)
        tempPreviousMonthCalendar?.also {
            it.setTimeInMillis(monthToDrawCalender.getTimeInMillis())
            it.add(Calendar.MONTH, -1)
        }
        val maximumPreviousMonthDay: Int =
            tempPreviousMonthCalendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)

        var dayColumn = 0
        var colDirection = if (isRtl) 6 else 0
        var dayRow = 0
        while (dayColumn <= 6) {
            if (dayRow == 7) {
                if (isRtl) {
                    colDirection--
                } else {
                    colDirection++
                }
                dayRow = 0
                if (dayColumn <= 6) {
                    dayColumn++
                }
            }
            if (dayColumn == dayColumnNames?.size ?: 0) {
                break
            }
            val xPosition =
                widthPerDay * dayColumn + paddingWidth + paddingLeft + accumulatedScrollOffset.x + offset - paddingRight
            val yPosition = (dayRow * heightPerDay + paddingHeight).toFloat()
            if (xPosition >= growFactor && (isAnimatingWithExpose || animationStatus == ANIMATE_INDICATORS) || yPosition >= growFactor) {
                // don't draw days if animating expose or indicators
                dayRow++
                continue
            }
            if (dayRow == 0) {
                // first row, so draw the first letter of the day
                if (shouldDrawDaysHeader) {
                    dayPaint.setColor(calenderTextColor)
                    dayPaint.setTypeface(Typeface.DEFAULT_BOLD)
                    dayPaint.setStyle(Paint.Style.FILL)
                    dayPaint.setColor(calenderTextColor)
                    canvas.drawText(
                        dayColumnNames?.get(colDirection) ?: "err",
                        xPosition,
                        paddingHeight,
                        dayPaint
                    )
                    dayPaint.setTypeface(Typeface.DEFAULT)
                }
            } else {
                val day = ((dayRow - 1) * 7 + colDirection + 1) - firstDayOfMonth
                var defaultCalenderTextColorToUse = calenderTextColor
                if (currentCalender!!.get(Calendar.DAY_OF_MONTH) == day && isSameMonthAsCurrentCalendar && !isAnimatingWithExpose) {
                    drawDayCircleIndicator(
                        currentSelectedDayIndicatorStyle,
                        canvas,
                        xPosition,
                        yPosition,
                        currentSelectedDayBackgroundColor
                    )
                    defaultCalenderTextColorToUse = currentSelectedDayTextColor
                } else if (isSameYearAsToday && isSameMonthAsToday && todayDayOfMonth == day && !isAnimatingWithExpose) {
                    // TODO calculate position of circle in a more reliable way
                    drawDayCircleIndicator(
                        currentDayIndicatorStyle,
                        canvas,
                        xPosition,
                        yPosition,
                        currentDayBackgroundColor
                    )
                    defaultCalenderTextColorToUse = currentDayTextColor
                }
                if (day <= 0) {
                    if (displayOtherMonthDays) {
                        // Display day month before
                        dayPaint.setStyle(Paint.Style.FILL)
                        dayPaint.setColor(otherMonthDaysTextColor)
                        canvas.drawText(
                            (maximumPreviousMonthDay + day).toString(),
                            xPosition,
                            yPosition,
                            dayPaint
                        )
                    }
                } else if (day > maximumMonthDay) {
                    if (displayOtherMonthDays) {
                        // Display day month after
                        dayPaint.setStyle(Paint.Style.FILL)
                        dayPaint.setColor(otherMonthDaysTextColor)
                        canvas.drawText(
                            (day - maximumMonthDay).toString(),
                            xPosition,
                            yPosition,
                            dayPaint
                        )
                    }
                } else {
                    dayPaint.setStyle(Paint.Style.FILL)
                    dayPaint.setColor(defaultCalenderTextColorToUse)
                    canvas.drawText(day.toString(), xPosition, yPosition, dayPaint)
                }
            }
            dayRow++
        }
    }

    fun drawDayCircleIndicator(
        indicatorStyle: Int,
        canvas: Canvas,
        x: Float,
        y: Float,
        color: Int
    ) {
        drawDayCircleIndicator(indicatorStyle, canvas, x, y, color, 1f)
    }

    fun drawDayCircleIndicator(
        indicatorStyle: Int,
        canvas: Canvas,
        x: Float,
        y: Float,
        color: Int,
        circleScale: Float
    ) {
        val strokeWidth: Float = dayPaint.getStrokeWidth()
        if (indicatorStyle == NO_FILL_LARGE_INDICATOR) {
            dayPaint.setStrokeWidth(2 * screenDensity)
            dayPaint.setStyle(Paint.Style.STROKE)
        } else {
            dayPaint.setStyle(Paint.Style.FILL)
        }
        drawCircle(canvas, x, y, color, circleScale)
        dayPaint.setStrokeWidth(strokeWidth)
        dayPaint.setStyle(Paint.Style.FILL)
    }

    // Draw Circle on certain days to highlight them
    fun drawCircle(canvas: Canvas, x: Float, y: Float, color: Int, circleScale: Float) {
        dayPaint.setColor(color)
        if (animationStatus == ANIMATE_INDICATORS) {
            val maxRadius = circleScale * bigCircleIndicatorRadius * 1.4f
            drawCircle(
                canvas,
                if (growfactorIndicator > maxRadius) maxRadius else growfactorIndicator,
                x,
                y - (textHeight / 6)
            )
        } else {
            drawCircle(canvas, circleScale * bigCircleIndicatorRadius, x, y - (textHeight / 6))
        }
    }

    fun drawEventIndicatorCircle(canvas: Canvas, x: Float, y: Float, color: Int) {
        dayPaint.setColor(color)
        if (eventIndicatorStyle == SMALL_INDICATOR) {
            dayPaint.setStyle(Paint.Style.FILL)
            drawCircle(canvas, smallIndicatorRadius, x, y)
        } else if (eventIndicatorStyle == NO_FILL_LARGE_INDICATOR) {
            dayPaint.setStyle(Paint.Style.STROKE)
            drawDayCircleIndicator(NO_FILL_LARGE_INDICATOR, canvas, x, y, color)
        } else if (eventIndicatorStyle == FILL_LARGE_INDICATOR) {
            drawDayCircleIndicator(FILL_LARGE_INDICATOR, canvas, x, y, color)
        }
    }

    fun drawCircle(canvas: Canvas, radius: Float, x: Float, y: Float) {
        canvas.drawCircle(x, y, radius, dayPaint)
    }
}