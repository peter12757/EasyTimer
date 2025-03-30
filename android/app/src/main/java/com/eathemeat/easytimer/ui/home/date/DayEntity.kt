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

    /**
     * 是否是闰年
     *
     * @param year year
     * @return 是否是闰年
     */
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    override fun toString(): String {
        return "year:$year"
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

    /**
     * 获取某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 某月的天数
     */
    fun getMonthDaysCount(year: Int, month: Int): Int {
        var count = 0

        val bigMonth = arrayOf(1, 3, 5, 7, 8,10,12)
        return month.run {
            if (bigMonth.contains(month)){//判断大月份
                31
            }else if (month == 2){//判断平年与闰年
               if (this@MonthEntity.year.isLeapYear(year)) {
                    29
                } else {
                    28
                }
            }else {//判断小月
                30
            }
        }
    }

    override fun toString(): String {
        return "$year month:$month"
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

    override fun toString(): String {
        return "$month week:$week "
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

    fun year(): Int {
        return week.month.year.year
    }

    fun month(): Int {
        return week.month.month
    }

    override fun toString(): String {
        return "$week day:$day color:$color"
    }
}


