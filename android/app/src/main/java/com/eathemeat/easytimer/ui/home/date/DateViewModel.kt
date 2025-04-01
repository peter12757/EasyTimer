package com.eathemeat.easytimer.ui.home.date

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.eathemeat.easytimer.data.NoteInfo
import com.future.composecalendar.data.DayEntity
import com.future.composecalendar.data.MonthEntity
import com.future.composecalendar.data.WEEK
import com.future.composecalendar.data.WeekEntity
import com.future.composecalendar.data.YearEntity
import com.future.composecalendar.utils.XLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import kotlin.math.ceil

class YearEntitys() : HashMap<Int, YearEntity>() {
    lateinit var currentDay: DayEntity
    var weekModelFlag: Boolean = false//周历模式
    // 周历模式上一次滑动的index
    var weekModelLastScrollIndex: Int = 0
    var showYearMonthDialog: Boolean = false

    init {
        getYear(Calendar.getInstance().get(Calendar.YEAR))
    }



    override fun get(key: Int): YearEntity {
        if (containsKey(key)) {
            return super.get(key)!!
        }
        val yearEntity = getYear(key)
        put(key,yearEntity)
        return yearEntity
    }

    private fun getYear(year: Int): YearEntity {
        if (containsKey(year)) return get(year)
        return YearEntity(year).let { result->
            (1..12).forEach { month ->
                val calendar = Calendar.getInstance()
                calendar.firstDayOfWeek = Calendar.SUNDAY
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month-1)   //calender的month从0开始
                calendar.set(Calendar.DAY_OF_MONTH,1)   //设置第一天
                val firstDay = calendar.get(Calendar.DAY_OF_WEEK)
                result.monthList[month] =
                    MonthEntity(result, month).apply {
                        val daysOfMonth = getMonthDaysCount(year, month)
                        for (day in 1..daysOfMonth) {
                            calendar.set(Calendar.DAY_OF_MONTH, day)
                            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                            val weekIndex = ceil((firstDay+day-1)/7f).toInt()
                            if(!weekList.containsKey(weekIndex)) weekList[weekIndex] = WeekEntity(this, weekIndex)
                            weekList[weekIndex]?.also {
                                it.dayList.put(day, DayEntity(it,day,WEEK.entries[dayOfWeek-1]).apply {

                                    if (isToday()) {
                                        color = Color.Red
                                        currentDay = this
                                        XLogger.d("currentDay12:$currentDay")
//                                } else if (day < firstDay) {
//                                    color = Color.Gray
//                                } else if (day < (firstDay + day_size)) {
//                                    color = Color.Black
                                    } else {
                                        color = Color.Gray
                                    }
                                    if(day == 2) {
                                        XLogger.d("putDay:$this")
                                    }
                                })
                            }
                        }
                        if (month <6) {
                            XLogger.d("week:${weekList[1]?.week}")
                            XLogger.d("weekList[$1]:${weekList[1]?.dayList}")
                        }
                    }


            }
            XLogger.d("currentDay:$currentDay")
            result
        }
    }


}

class DateNoteEntitys: HashMap<Calendar, NoteInfo>() {

}

class DateViewModel() : ViewModel() {


    var timeNow by mutableStateOf(Pair<String,String>("1989-11-28","00:00:00"))
    //年的数据
    private val _dateStateData: MutableStateFlow<YearEntitys> = MutableStateFlow(YearEntitys())

    //当前坐标
    var today = Calendar.getInstance()

    val dateStateData = _dateStateData.asStateFlow()    //DateScreen data


    init {
        _dateStateData.value.get(today.get(Calendar.YEAR))
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
                        it.currentDay = it.currentDay.weekEntity.monthEntity.weekList[action.row]!!.dayList[action.column]!!
                        it
                    }
                } else {
                    try {
                        //月历的点击事件 通过点击点 找到这一行周历的数据
                        val week = _dateStateData.value.currentDay.weekEntity.monthEntity.weekList[action.row]
                        val day = week!!.dayList[action.column]!!
                        XLogger.d("月历 week========>${day}")
                        //把这一行 加入到周历的数据中

                        val weekEntity = day.weekEntity

                        _dateStateData.update {
                            it.currentDay = day
                            it
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is HomeAction.SetCalendarModel -> {
                //无效切换
                if (_dateStateData.value.weekModelFlag == action.isWeekModel) return

                if (action.isWeekModel) {
                    //通过月历的点击 click Day 找到周历
                    val clickDay = _dateStateData.value.currentDay

                    XLogger.d("月历 click========>${clickDay}")
                    //把这一行 加入到周历的数据中
                    val rowIndex = clickDay.weekEntity.week

                    _dateStateData.update {
                        it.run {
                            weekModelFlag = true
                            weekModelLastScrollIndex = action.page
                            it
                        }

                    }
                } else {
                    //通过周历的click day 找到月历
                    val clickDay = _dateStateData.value.currentDay
                    XLogger.d("++++++++++++++>${clickDay}")

                    XLogger.d("查找月历：${clickDay.year()}-${clickDay.month() + 1}-${clickDay.day}")


                    //月份的跨度
                    val yearDiff: Int = clickDay.year() - today.get(Calendar.YEAR)
                    val monthDiff: Int = clickDay.month() - today.get(Calendar.MONTH)
                    val totalMonthDiff = yearDiff * 12 + monthDiff

                    _dateStateData.update {
                        it.run {
                            weekModelFlag = false
                            weekModelLastScrollIndex = action.page
                            it
                        }
                    }

                    // val offset = currentPage - 5000
                    XLogger.d("totalMonthDiff=============>${totalMonthDiff + 5000}")
//                    getMonthData(totalMonthDiff + 5000, true)
                }
            }

            is HomeAction.UpdateData -> {
                if (_dateStateData.value.weekModelFlag) {
                    //周历 更新数据
                    getWeekData(action.page)
                } else {
//                    getMonthData(action.page)
                }
            }

            is HomeAction.ShowYearMonthSelectDialog -> {
                _dateStateData.update {
                    it.run {
                        showYearMonthDialog = action.show
                        it
                    }
                }
            }
        }
    }

    fun getWeekData(currentPage: Int = 0) {
        XLogger.d("---------------->getWeekData")

        val calendar = Calendar.getInstance()
        val todayCalendar = Calendar.getInstance()
        val todayYear = todayCalendar.get(Calendar.YEAR)
        val todayMonth = todayCalendar.get(Calendar.MONTH)
        val todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH)

        val clickDay = _dateStateData.value.currentDay
        val clickDayWeek = clickDay.weekEntity
        val weekList: MutableList<DayEntity> = mutableListOf()

        if (clickDayWeek.dayList.isNotEmpty()) {
            //如果之前的数据不为空 说明已经赋值
            if (_dateStateData.value.weekModelLastScrollIndex > 0) {


                //找出参考日期
                // TODO:

                XLogger.d("---------------->情况1")
                //这样说明 是从 周模式 滑过来的
                //判断左滑还是右滑进行日期的增减
                repeat(7) {
                    calendar.add(
                        Calendar.DAY_OF_MONTH,
                        if (_dateStateData.value.weekModelLastScrollIndex < currentPage) 1 else -1
                    )
                    val weekOfDay = calendar.get(Calendar.DAY_OF_WEEK)

                    //添加点击的日期
                    if (clickDayWeek.week > 0 && clickDayWeek.week == weekOfDay) {
                        _dateStateData.update {
//                            it.copy(clickDay = dayEntity)
                            it
                        }
                    }
//                    if (_dateStateData.value.weekModelLastScrollIndex < currentPage) {
////                        weekList.add(dayEntity)
//                    } else {
//                        XLogger.d("情况1 添加在前面 ${dayEntity.day}")
//                        weekList.add(0, dayEntity)
//                    }
                }
            } else {
                XLogger.d("---------------->情况2")
                //如果是从 月历 过来的
                XLogger.d("月历 click========>${clickDay}")
                //把这一行 加入到周历的数据中
                val rowIndex = clickDay.weekEntity.week
            }
        } else {
            XLogger.d("---------------->情况3")
            //如果之前的数据为空 说明初始化的状态 则需要生成 今天这一周的数据 此方法暂时不会用到 因为 没有设置 进来就是周模式
            val week = calendar.get(Calendar.DAY_OF_WEEK)
            //回到周一
            calendar.add(Calendar.DAY_OF_MONTH, -week)
        }


        _dateStateData.update {
            it.run {
                weekModelLastScrollIndex = currentPage
                it
            }
        }
    }

}