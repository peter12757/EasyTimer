package com.future.composecalendar.data

import androidx.compose.ui.graphics.Color
import java.util.Calendar






data class YearEntity(val year: Int = 0,
                      val monthList: MutableMap<Int,MonthEntity> = mutableMapOf()
) {
    fun isSameYear(other: YearEntity): Boolean {
        return year == other.year
    }

    fun isThisYear(calendar:Calendar = Calendar.getInstance()):Boolean {
        return calendar.get(Calendar.YEAR) == year
    }
}

data class MonthEntity(
    val year: YearEntity,
    val month: Int = 0,
    val weekList: MutableMap<Int,WeekEntity> = mutableMapOf(),
) {
    fun isSameMonth(other: MonthEntity): Boolean {
        return month == other.month && year.isSameYear(other.year)
    }

    fun isThisMonth(calendar:Calendar = Calendar.getInstance()):Boolean {
        return calendar.get(Calendar.MONTH) == month &&year.isThisYear(calendar)
    }
}

data class WeekEntity(
    val month: MonthEntity,
    val week:Int = 0,
    val dayList: MutableMap<Int,DayEntity> = mutableMapOf(),
) {
    fun isSameWeek(other: WeekEntity): Boolean {
        return week == other.week && month.isSameMonth(other.month)
    }

    fun isThisWeek(calendar:Calendar = Calendar.getInstance()):Boolean {
        return calendar.get(Calendar.WEEK_OF_MONTH) == week && month.isThisMonth(calendar)
    }

    fun isSameMonth(other: DayEntity): Boolean {
        return month.isSameMonth(other.week.month)
    }
}

data class DayEntity(
    val week: WeekEntity,
    val day: Int = Calendar.DAY_OF_WEEK,
    var color: Color = Color.Black,
) {
    fun isSameDay(other: DayEntity) :Boolean {
        return  day == other.day && week.isSameWeek(other.week)
    }

    fun isToday(calendar:Calendar = Calendar.getInstance()) :Boolean {
        return calendar.get(Calendar.DAY_OF_WEEK) == day && week.isThisWeek(calendar)
    }

    fun isSameMonth(other: DayEntity): Boolean {
        return week.month.isSameMonth(other.week.month)
    }

    fun isSameWeek(other: DayEntity): Boolean {
        return week.isSameWeek(other.week)
    }

    fun isWeekend(): Boolean {
        return day == 6 || day == 7
    }
}


