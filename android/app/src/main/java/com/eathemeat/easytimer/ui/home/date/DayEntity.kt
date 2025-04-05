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
    val yearEntity: YearEntity,
    val month: Int = 0,
    val weekList: MutableMap<Int,WeekEntity> = mutableMapOf(),
) {
    fun isSameMonth(other: MonthEntity): Boolean {
        return month == other.month && yearEntity.isSameYear(other.yearEntity)
    }

    fun isThisMonth(calendar:Calendar = Calendar.getInstance()):Boolean {
        return calendar.get(Calendar.MONTH)+1 == month &&yearEntity.isThisYear(calendar)
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
               if (this@MonthEntity.yearEntity.isLeapYear(year)) {
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
        return "$yearEntity month:$month"
    }

    fun getToDayorFirstDay(): DayEntity {
        val calender = Calendar.getInstance()
        if (isThisMonth(calender)) {
            return findDay(calender.get(Calendar.DAY_OF_MONTH))
        } else {
            return weekList[0]!!.dayList[0]!!
        }
    }

    private fun findDay(day: Int): DayEntity {
        var result = weekList[0]!!.dayList[0]!!
        weekList.forEach{ (weekIndex,week) ->
            week.dayList.forEach { dayIndex, dayEntity ->
                result = if (dayEntity.isThisDay(day)) dayEntity else result
            }


        }
        return result
    }
}

enum class WEEK  {
    SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY
}

data class WeekEntity(
    val monthEntity: MonthEntity,
    val week:Int = 0,
    val dayList: MutableMap<Int,DayEntity> = mutableMapOf(),
) {
    fun isSameWeek(other: WeekEntity): Boolean {
        return week == other.week && monthEntity.isSameMonth(other.monthEntity)
    }

    fun isThisWeek(calendar:Calendar = Calendar.getInstance()):Boolean {
        return calendar.get(Calendar.WEEK_OF_MONTH) == week && monthEntity.isThisMonth(calendar)
    }

    fun isSameMonth(other: DayEntity): Boolean {
        return monthEntity.isSameMonth(other.weekEntity.monthEntity)
    }

    override fun toString(): String {
        return "$monthEntity week:$week "
    }
}

data class DayEntity(
    val weekEntity: WeekEntity,
    val day: Int,
    val week: WEEK,
    var color: Color = Color.Black,
) {
    fun isSameDay(other: DayEntity) :Boolean {
        return  day == other.day && weekEntity.isSameWeek(other.weekEntity)
    }

    fun isToday(calendar:Calendar = Calendar.getInstance()) :Boolean {
        return calendar.get(Calendar.DAY_OF_MONTH) == day && weekEntity.isThisWeek(calendar)
    }

    fun isSameMonth(other: DayEntity): Boolean {
        return weekEntity.monthEntity.isSameMonth(other.weekEntity.monthEntity)
    }

    fun isSameWeek(other: DayEntity): Boolean {
        return weekEntity.isSameWeek(other.weekEntity)
    }

    fun isWeekend(): Boolean {
        return week == WEEK.SATURDAY || week == WEEK.SUNDAY
    }

    fun year(): Int {
        return weekEntity.monthEntity.yearEntity.year
    }

    fun month(): Int {
        return weekEntity.monthEntity.month
    }

    override fun toString(): String {
        return "$weekEntity day:$day week:${week.name} color:$color\n"
    }

    fun isThisDay(day: Int): Boolean {
        return this.day == day
    }
}


