package com.eathemeat.easytimer.ui.widget.calender

import java.text.DateFormatSymbols
import java.util.Locale


fun getWeekdayNames(
    locale: Locale?,
    day: Int,
    useThreeLetterAbbreviation: Boolean
): Array<String> {
    val dateFormatSymbols: DateFormatSymbols = DateFormatSymbols(locale)
    val dayNames: Array<String> = dateFormatSymbols.getShortWeekdays()
    checkNotNull(dayNames) { "Unable to determine weekday names from default locale" }
    check(dayNames.size == 8) {
        ("Expected weekday names from default locale to be of size 7 but: "
                + dayNames.contentToString() + " with size " + dayNames.size + " was returned.")
    }

    val weekDayNames = arrayOfNulls<String>(7)
    val weekDaysFromSunday = arrayOf(
        dayNames[1],
        dayNames[2], dayNames[3], dayNames[4], dayNames[5], dayNames[6], dayNames[7]
    )
    var currentDay = day - 1
    var i = 0
    while (i <= 6) {
        currentDay = if (currentDay >= 7) 0 else currentDay
        weekDayNames[i] = weekDaysFromSunday[currentDay]
        i++
        currentDay++
    }

    if (!useThreeLetterAbbreviation) {
        for (i in weekDayNames.indices) {
            weekDayNames[i] = weekDayNames[i]!!.substring(0, 1)
        }
    }

    return weekDayNames.requireNoNulls()
}