package com.eathemeat.easytimer.ui.home.date

import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import com.future.composecalendar.data.DayEntity
import com.future.composecalendar.data.MonthEntity
import com.future.composecalendar.data.WeekEntity
import com.future.composecalendar.data.YearEntity
import com.future.composecalendar.utils.XLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar


class YearEntitys :HashMap<Int,YearEntity> {
    var currentDay: DayEntity
    var weekModelFlag: Boolean = false//周历模式
    // 周历模式上一次滑动的index
    var weekModelLastScrollIndex: Int = 0
    val showYearMonthDialog: Boolean = false

    constructor(calendar: Calendar = Calendar.getInstance()):super() {
        val currentYear = getYear(calendar.get(Calendar.YEAR))
        val currentMonth = MonthEntity(currentYear,calendar.get(Calendar.MONTH))
        val currentWeek = WeekEntity(currentMonth,calendar.get(Calendar.WEEK_OF_MONTH))
        currentDay = DayEntity(currentWeek,calendar.get(Calendar.DAY_OF_WEEK))
    }

    override fun get(key: Int): YearEntity {
        if (containsKey(key)) {
            return super.get(key)!!
        }
        val yearEntity = getYear(key)
        put(key,yearEntity)
        return yearEntity
    }

    fun getYear(year: Int):YearEntity {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        return YearEntity(year).let { result->
            (1..12).forEach { month ->
                calendar.set(Calendar.MONTH, month)
                val monthonEntity = MonthEntity(result, month)
                monthonEntity.apply {
                    (1..5).forEach { week ->
                        calendar.set(Calendar.WEEK_OF_MONTH, week)
                        val weekEntity = WeekEntity(this, month)
                        val day_size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        val firstDay = calendar.get(Calendar.DAY_OF_MONTH)
                        (1..5 * 7).forEach { day ->
                            calendar.set(Calendar.DAY_OF_WEEK, day)
                            val dayEntity = DayEntity(weekEntity, day).apply {
                                if (isToday()) {
                                    color = Color.Red
                                } else if (day < firstDay) {
                                    color = Color.Gray
                                } else if (day < (firstDay + day_size)) {
                                    color = Color.Black
                                } else {
                                    color = Color.Gray
                                }
                            }
                            // TODO: 数据填充


                        }


                    }
//                    result.monthList.put(month, MonthEntity(year, month))
                }

            }
            result
        }
    }


}

class DateScreenState {
    //年的数据
    private val _dateStateData: MutableStateFlow<YearEntitys> = MutableStateFlow(YearEntitys())

    //当前坐标
    var today = Calendar.getInstance()

    val dateStateData = _dateStateData.asStateFlow()    //DateScreen data

    init {
        getMonthData(5000)
    }


    sealed class HomeAction {
        data class ItemClick(val row: Int,val column: Int) : HomeAction()

        data class SetCalendarModel(val isWeekModel: Boolean, val page: Int) : HomeAction()

        data class UpdateData(val page: Int) : HomeAction()
        data class ShowYearMonthSelectDialog(val show: Boolean) : HomeAction()
    }

    fun dispatch(action: HomeAction) {
        when (action) {
            is HomeAction.ItemClick -> {
                if (_dateStateData.value.weekModelFlag) {
                    val weekData =  _dateStateData.value.currentDay
                    XLogger.d("周历 click========>${weekData}")
                    _dateStateData.update {
                        it.currentDay = it.currentDay.week.month.weekList.get(action.row)!!.dayList.get(action.column)!!
                        it
                    }
                } else {
                    try {
                        //月历的点击事件 通过点击点 找到这一行周历的数据
                        val week = _dateStateData.value.monthEntity.weekList[action.row]
                        val day = week.dayList[action.column]
                        XLogger.d("月历 week========>${week.year}-${week.month + 1}")
                        //把这一行 加入到周历的数据中
                        val (year, month) = _dateStateData.value.monthEntity

                        val weekEntity = WeekEntity(
                            year = year,
                            month = month,
                            dayList = _dateStateData.value.monthEntity.weekList[action.row].dayList,
                            offset = action.pageOffset
                        )

                        _dateStateData.update {
                            it.copy(clickDay = day, weekEntity = weekEntity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is HomeAction.SetCalendarModel -> {
                //无效切换
                if (weekModelFlag == action.isWeekModel) return

                if (action.isWeekModel) {
                    //通过月历的点击 click Day 找到周历
                    val clickDay = clickDay

                    val findIndex = _dateStateData.value.monthEntity.weekList.indexOfFirst {
                        it.year == clickDay.year && it.month == clickDay.month && it.day == clickDay.day
                    }

                    if (findIndex < 0) return

                    XLogger.d("月历 click========>${clickDay.year}-${clickDay.month + 1}-${clickDay.day}")
                    //把这一行 加入到周历的数据中
                    val rowIndex = (findIndex / 7 + if (findIndex % 7 == 0) 0 else 1) - 1

                    _dateStateData.update {
                        it.copy(
                            weekModelFlag = true,
                            weekModelLastScrollIndex = action.page,
                            weekEntity = WeekEntity(
                                year = clickDay.year,
                                month = clickDay.month,
                                dayList = _dateStateData.value.monthEntity.weekList[action.]
                                ),
                                offset = action.page
                            )
                        )
                    }
                } else {
                    //通过周历的click day 找到月历
                    val (year, month, day) = _dateStateData.value.

                    XLogger.d("++++++++++++++>${year}-${month + 1}-${day}")

                    if (year == 0 && month == 0 && day == 0) {
                        return
                    }
                    XLogger.d("查找月历：$year-${month + 1}-$day")

                    val calendar = Calendar.getInstance()
                    val todayYear = calendar.get(Calendar.YEAR)
                    val todayMonth = calendar.get(Calendar.MONTH)

                    //月份的跨度
                    val yearDiff: Int = year - todayYear
                    val monthDiff: Int = month - todayMonth
                    val totalMonthDiff = yearDiff * 12 + monthDiff

                    _dateStateData.update {
                        it.copy(
                            weekModelFlag = false,
                            weekModelLastScrollIndex = action.page,
                            needScrollPage = totalMonthDiff + 5000
                        )
                        it
                    }

                    // val offset = currentPage - 5000
                    XLogger.d("totalMonthDiff=============>${totalMonthDiff + 5000}")
                    getMonthData(totalMonthDiff + 5000, true)
                }
            }

            is HomeAction.UpdateData -> {
                if (_dateStateData.value.weekModelFlag) {
                    //周历 更新数据
                    getWeekData(action.page)
                } else {
                    getMonthData(action.page)
                }
            }

            is HomeAction.ShowYearMonthSelectDialog -> {
                _dateStateData.update {
                    it.copy(showYearMonthDialog = action.show)
                }
            }
        }
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
        //判断大月份
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            count = 31
        }

        //判断小月
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            count = 30
        }

        //判断平年与闰年
        if (month == 2) {
            count = if (isLeapYear(year)) {
                29
            } else {
                28
            }
        }
        return count
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


    fun getWeekData(currentPage: Int = 0) {
        XLogger.d("---------------->getWeekData")
        var weekEntity = WeekEntity()

        val calendar = Calendar.getInstance()
        val todayCalendar = Calendar.getInstance()
        val todayYear = todayCalendar.get(Calendar.YEAR)
        val todayMonth = todayCalendar.get(Calendar.MONTH)
        val todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH)

        val weekList: MutableList<DayEntity> = mutableListOf()

        if (weekEntity.dayList.isNotEmpty()) {
            //如果之前的数据不为空 说明已经赋值
            if (_dateStateData.value.weekModelLastScrollIndex > 0) {
                val clickDayWeek = _dateStateData.value.clickDay.week

                //找出参考日期
                val baseDay =
                    if (_dateStateData.value.weekModelLastScrollIndex < currentPage) weekEntity.dayList.last() else weekEntity.dayList.first()
                calendar[baseDay.year, baseDay.month] = baseDay.day

                XLogger.d("---------------->情况1")
                //这样说明 是从 周模式 滑过来的
                //判断左滑还是右滑进行日期的增减
                repeat(7) {
                    calendar.add(
                        Calendar.DAY_OF_MONTH,
                        if (_dateStateData.value.weekModelLastScrollIndex < currentPage) 1 else -1
                    )

                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val weekOfDay = calendar.get(Calendar.DAY_OF_WEEK)
                    val dayEntity = DayEntity(
                        year = year,
                        month = month,
                        day = day,
                        week = weekOfDay,
                        isCurrentDay = todayYear == year && todayMonth == month && todayDay == day,
                        isCurrentMonth = todayYear == year && todayMonth == month,
                        isWeekend = weekOfDay == 6 || weekOfDay == 7,
                        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR),
                        color = if (todayYear == year && todayMonth == month && todayDay == day) Color.Red else Color.Black
                    )

                    //添加点击的日期
                    if (clickDayWeek > 0 && clickDayWeek == weekOfDay) {
                        _dateStateData.update {
                            it.copy(clickDay = dayEntity)
                        }
                    }


                    if (_dateStateData.value.weekModelLastScrollIndex < currentPage) {
                        weekList.add(dayEntity)
                    } else {
                        XLogger.d("情况1 添加在前面 ${dayEntity.day}")
                        weekList.add(0, dayEntity)
                    }
                }

                weekEntity = WeekEntity(
                    year = todayYear,
                    month = todayMonth,
                    dayList = weekList,
                    offset = currentPage
                )

            } else {
                XLogger.d("---------------->情况2")
                //如果是从 月历 过来的
                val clickDay = _dateStateData.value.clickDay

                val findIndex = _dateStateData.value.monthEntity.weekList.indexOfFirst {
                    it.year == clickDay.year && it.month == clickDay.month && it.day == clickDay.day
                }

                XLogger.d("月历 click========>${clickDay.year}-${clickDay.month + 1}-${clickDay.day}")
                //把这一行 加入到周历的数据中
                val rowIndex = (findIndex / 7 + if (findIndex % 7 == 0) 0 else 1) - 1
                val (year, month) = _dateStateData.value.monthEntity
                weekEntity = WeekEntity(
                    year = year,
                    month = month,
                    dayList = _dateStateData.value.monthEntity.weekList.subList(
                        rowIndex * 7,
                        rowIndex * 7 + 7
                    ),
                    offset = currentPage
                )
            }
        } else {
            XLogger.d("---------------->情况3")
            //如果之前的数据为空 说明初始化的状态 则需要生成 今天这一周的数据 此方法暂时不会用到 因为 没有设置 进来就是周模式
            val week = calendar.get(Calendar.DAY_OF_WEEK)
            //回到周一
            calendar.add(Calendar.DAY_OF_MONTH, -week)

            (1..7).forEach { index ->
                calendar.add(Calendar.DAY_OF_MONTH, index - 1)
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val weekOfDay = calendar.get(Calendar.DAY_OF_WEEK)

                val dayEntity = DayEntity(
                    year = year,
                    month = month,
                    day = day,
                    week = weekOfDay,
                    isCurrentDay = todayYear == year && todayMonth == month && todayDay == day,
                    isCurrentMonth = todayYear == year && todayMonth == month,
                    isWeekend = weekOfDay == 6 || weekOfDay == 7,
                    weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR),
                    color = if (todayYear == year && todayMonth == month && todayDay == day) Color.Red else Color.Black
                )
                weekList.add(dayEntity)
            }
            weekEntity = WeekEntity(
                year = todayYear,
                month = todayMonth,
                dayList = weekList,
                offset = currentPage
            )
        }


        _dateStateData.update {
            it.copy(weekEntity = weekEntity, weekModelLastScrollIndex = currentPage)
        }
    }

    /**
     * 获取月份数据
     * @param currentPage Int
     */
    fun getMonthData(
        currentPage: Int = 0,
        weekChangeMonthUpdateClickDay: Boolean = false
    ) {
        val offset = currentPage - 5000

        XLogger.d("=====>生成的数据")
        val todayCalendar = Calendar.getInstance()
        val todayYear = todayCalendar.get(Calendar.YEAR)
        val todayMonth = todayCalendar.get(Calendar.MONTH)
        val todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH)

        val monthList: MutableList<DayEntity> = mutableListOf()

        val calendar = Calendar.getInstance()
        calendar.set(todayYear, todayMonth, 1)

        calendar.add(Calendar.MONTH, offset)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        calendar[year, month] = 1

        val nextMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (day in 1..nextMaxDay) {
            calendar[year, month] = day
            val week = calendar.get(Calendar.DAY_OF_WEEK)
            val dayEntity = DayEntity(
                year = year,
                month = month,
                day = day,
                week = week,
                isCurrentDay = todayYear == year && todayMonth == month && todayDay == day,
                isCurrentMonth = todayYear == year && todayMonth == month,
                isWeekend = week == 6 || week == 7,
                weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR),
                color = if (todayYear == year && todayMonth == month && todayDay == day) Color.Red else Color.Black
            )
            //如果点击的日期没有设置
            if (weekChangeMonthUpdateClickDay) {
                //如果是周历 转月历 则 点击的日期不需要更新
                XLogger.d("-------->周历转 月历 不需要 重新赋值click day")
            } else if (todayYear == year && todayMonth == month && todayDay == day) {
                _dateStateData.update {
                    it.copy(clickDay = dayEntity)
                }
            } else if (offset != 0 && day == 1) {
                _dateStateData.update {
                    it.copy(clickDay = dayEntity)
                }
            }
            monthList.add(
                dayEntity
            )
        }


        if (monthList.first().week != 1) {
            //回到当月的第一天
            calendar.set(year, month, 1)
            repeat(monthList.first().week - 1) {
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                val weekDay: Int = calendar.get(Calendar.DAY_OF_WEEK)
                val year1: Int = calendar.get(Calendar.YEAR)
                val month1: Int = calendar.get(Calendar.MONTH)
                val day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
                monthList.add(
                    0, DayEntity(
                        year = year1,
                        month = month1,
                        day = day1,
                        week = weekDay,
                        isCurrentDay = false,
                        isCurrentMonth = false,
                        isWeekend = weekDay == 6 || weekDay == 7,
                        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR),
                        color = Color.LightGray
                    )
                )
            }
        }

        //
        if (monthList.last().week != 7) {
            //回到本月第一天
            calendar[year, month] = 1
            //回到当月最后一天
            val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            calendar[year, month] = lastDayOfMonth

            repeat(7 - monthList.last().week) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                val weekDay: Int = calendar.get(Calendar.DAY_OF_WEEK)
                val year1: Int = calendar.get(Calendar.YEAR)
                val month1: Int = calendar.get(Calendar.MONTH)
                val day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
                monthList.add(
                    DayEntity(
                        year = year1,
                        month = month1,
                        day = day1,
                        week = weekDay,
                        isCurrentDay = false,
                        isCurrentMonth = false,
                        isWeekend = weekDay == 6 || weekDay == 7,
                        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR),
                        color = Color.LightGray
                    )
                )
            }
        }

        val monthEntity =
            MonthEntity(year = year, month = month, weekList = monthList, offset = currentPage)
        XLogger.d("update month data :${monthEntity}")
        _dateStateData.update {
            it.copy(monthEntity = monthEntity, weekModelLastScrollIndex = 0)
        }


        //第一页且 周数据为空的时候 设置 周数据
        if (offset == 0 && _dateStateData.value.weekEntity.dayList.isEmpty()) {
            val clickDay = _dateStateData.value.clickDay

            val findIndex = monthEntity.weekList.indexOfFirst {
                it.year == clickDay.year && it.month == clickDay.month && it.day == clickDay.day
            }

            XLogger.d("月历 click========>${clickDay.year}-${clickDay.month + 1}-${clickDay.day}")
            //把这一行 加入到周历的数据中
            val rowIndex = (findIndex / 7 + if (findIndex % 7 == 0) 0 else 1) - 1

            _dateStateData.update {
                it.copy(
                    weekEntity = WeekEntity(
                        year = year,
                        month = month,
                        dayList = monthEntity.weekList.subList(
                            rowIndex * 7,
                            rowIndex * 7 + 7
                        ),
                        offset = currentPage
                    )
                )
            }
        }
    }
}