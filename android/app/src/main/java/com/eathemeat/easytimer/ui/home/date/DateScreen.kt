package com.eathemeat.easytimer.ui.home.date

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.easytimer.R
import com.future.composecalendar.utils.XLogger
import kotlinx.coroutines.launch
import kotlin.math.ceil

@Composable
fun DateScreen(homeViewModel: DateViewModel = viewModel()) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 10000 }
    )

    YearMonthSelectDialog(viewModel = homeViewModel,pagerState)
    XLogger.d("==================>Calendar")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        stickyHeader(key = "stickyHeader") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                val textMeasurerAndTextSize = getTextMeasurerAndTextSize()
                YearAndMonth(homeViewModel, pagerState)
                //星期
                WeekRow()
                //日历信息
                CalendarPager(
                    homeViewModel = homeViewModel,
                    textMeasurerAndTextSize = textMeasurerAndTextSize,
                    pagerState
                )
            }
        }
        repeat(5) {
            item(key = it) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(50.dp)
                        .background(
                            color = Color.White
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = if (it % 2 == 0) Color.Gray.copy(alpha = 0.5f) else Color.LightGray.copy(
                                    alpha = 0.5f
                                )
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(start = 12.dp),
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .fillMaxWidth(), text = "item $it", color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarPager(
    homeViewModel: DateViewModel,
    textMeasurerAndTextSize: Pair<TextMeasurer, IntSize>,
    pagerState: PagerState
) {
    XLogger.d("CalendarPager==================>")


    UpdatePagerState(homeViewModel, pagerState)

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        XLogger.d("page Index========>$it")
        CalendarPagerContent(
            homeViewModel = homeViewModel,
            textMeasurerAndTextSize = textMeasurerAndTextSize,
            page = it
        )
    }
}

@Composable
fun CalendarPagerContent(
    homeViewModel: DateViewModel,
    textMeasurerAndTextSize: Pair<TextMeasurer, IntSize>,
    page: Int,
) {
    XLogger.d("CalendarContent======>")

    val dateState = homeViewModel.dateStateData.collectAsState().value
    val (textMeasurer, textSize) = textMeasurerAndTextSize
    val paddingPx = 2

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val clickDay = dateState.currentDay

    val height = if (dateState.weekModelFlag) {
        (screenWidthDp / 7f).dp
    } else {
        (dateState.currentDay.weekEntity.monthEntity.weekList.size  * (screenWidthDp / 7f)).dp
    }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .background(color = Color.Transparent)
        .animateContentSize()
        .pointerInput(key1 = dateState) {
            detectTapGestures(onTap = { offset ->
                val perWidthWithDp = screenWidthDp / 7f
                val column = ceil(offset.x / perWidthWithDp.dp.toPx()).toInt() -1
                val row = ceil(offset.y / perWidthWithDp.dp.toPx()).toInt() -1
                homeViewModel.dispatch(
                    DateViewModel.HomeAction.ItemClick(
                        row,
                        column
                    )
                )
            })
            detectVerticalDragGestures { change, dragAmount ->
                XLogger.d("detectDragGestures=======>change:${change.position.y}  dragAmount:${dragAmount}")
                if (dragAmount >= 20) {
                    XLogger.d("------------>月历")
                    homeViewModel.dispatch(
                        DateViewModel.HomeAction.SetCalendarModel(
                            false,
                            page
                        )
                    )
                }
                if (dragAmount <= -20) {
                    XLogger.d("------------>周历")
                    homeViewModel.dispatch(
                        DateViewModel.HomeAction.SetCalendarModel(
                            true,
                            page = page
                        )
                    )
                }
            }
        }, onDraw = {
        val perWidthWithPadding = this.size.width / 7f

        if (!dateState.weekModelFlag) {
            XLogger.d("月历模式")
            clickDay.weekEntity.monthEntity.weekList.forEach { (weekIndex, weekData) ->
                weekData.dayList.forEach { (dayIndex, dayEntity) ->
                    XLogger.d("每日的数据 ${dayEntity}")
                    val colIndex = (dayIndex+1)%7
                    val rowIndex = weekIndex -1
                    XLogger.d("colIndex:$colIndex  rowIndex:$rowIndex")
                    val textColor =
                        if (dayEntity.isSameDay(dateState.currentDay)) {
                            Color.White
                        } else if (dayEntity.isWeekend() && dayEntity.isSameMonth(dateState.currentDay)) {
                            Color.Red
                        } else {
                            dayEntity.color
                        }
                    val backgroundColor: Color =
                        if (dayEntity.isSameDay(dateState.currentDay)) {
                            //点击的画圆背景
                            Color.Blue.copy(0.5f)
                        } else if (dayEntity.isToday()) {
                            //当天画圆背景
                            Color.LightGray.copy(0.5f)
                        } else {
                            Color.Transparent
                        }
                    drawCircle(
                        color = backgroundColor,
                        radius = (perWidthWithPadding - 2 * paddingPx) / 2f,
                        center = Offset(
                            colIndex * perWidthWithPadding + paddingPx + perWidthWithPadding / 2f,
                            rowIndex * perWidthWithPadding - paddingPx + perWidthWithPadding / 2f
                        ),
                    )

                    drawText(
                        textMeasurer = textMeasurer,
                        text = "${dayEntity.day}",
                        size = Size(
                            perWidthWithPadding - 2 * paddingPx,
                            perWidthWithPadding - 2 * paddingPx
                        ),
                        topLeft = Offset(
                            colIndex * perWidthWithPadding,
                            rowIndex * perWidthWithPadding
                                    //定位到中间位置
                                    + perWidthWithPadding * 0.5f
                                    //减去文字的高度
                                    - textSize.height / 2f
                        ),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = textColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    )

                    if (dayEntity.isToday()) {
                        //今天的背景
                        val todayRadius = (perWidthWithPadding - 2 * paddingPx) / 8f
                        drawCircle(
                            color = Color.White,
                            radius = todayRadius,
                            center = Offset(
                                colIndex * perWidthWithPadding + perWidthWithPadding * 0.75f + todayRadius,
                                rowIndex * perWidthWithPadding + todayRadius
                            ),
                        )
                        //今天的文字 大小是0.75倍的宽度
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "今",
                            size = Size(
                                (perWidthWithPadding - 2 * paddingPx) / 4f,
                                (perWidthWithPadding - 2 * paddingPx) / 4f
                            ),
                            topLeft = Offset(
                                colIndex * perWidthWithPadding + perWidthWithPadding * 0.75f,
                                rowIndex * perWidthWithPadding
                            ),
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                color = Color.Magenta,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        )
                    }
                }
            }
        } else {
            XLogger.d("周历模式")

            clickDay.weekEntity.dayList.forEach { (index, dayEntity) ->
                //一行 当前的日期
                val colIndex = index +1
                XLogger.d("每日的数据 ${dayEntity}")
                val textColor =
                    if (dayEntity.isSameDay(dateState.currentDay)) {
                        Color.White
                    } else if (dayEntity.isWeekend() && dayEntity.isSameMonth(dateState.currentDay)) {
                        Color.Red
                    } else {
                        dayEntity.color
                    }

                val backgroundColor: Color =
                    if (dayEntity.isSameDay(dateState.currentDay)) {
                        //点击的画圆背景
                        Color.Blue.copy(0.5f)
                    } else if (dayEntity.isToday()) {
                        //当天画圆背景
                        Color.LightGray.copy(0.5f)
                    } else {
                        Color.Transparent
                    }

                drawCircle(
                    color = backgroundColor,
                    radius = (perWidthWithPadding - 2 * paddingPx) / 2f,
                    center = Offset(
                        colIndex * perWidthWithPadding + paddingPx + perWidthWithPadding / 2f,
                        perWidthWithPadding / 2f
                    ),
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = "${dayEntity.day}",
                    size = Size(
                        perWidthWithPadding - 2 * paddingPx,
                        perWidthWithPadding - 2 * paddingPx
                    ),
                    topLeft = Offset(
                        colIndex * perWidthWithPadding,
                        perWidthWithPadding * 0.5f
                                //减去文字的高度
                                - textSize.height / 2f
                    ),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = if (textColor == Color.LightGray) Color.Black else textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    )
                )

                if (dayEntity.isToday()) {
                    //今天的背景
                    val todayRadius = (perWidthWithPadding - 2 * paddingPx) / 8f
                    drawCircle(
                        color = Color.White,
                        radius = todayRadius,
                        center = Offset(
                            colIndex * perWidthWithPadding + perWidthWithPadding * 0.75f + todayRadius,
                            todayRadius
                        ),
                    )
                    //今天的文字 大小是0.75倍的宽度
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "今",
                        size = Size(
                            (perWidthWithPadding - 2 * paddingPx) / 4f,
                            (perWidthWithPadding - 2 * paddingPx) / 4f
                        ),
                        topLeft = Offset(
                            colIndex * perWidthWithPadding + perWidthWithPadding * 0.75f,
                            0f
                        ),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Color.Magenta,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
            }
        }
    })
}

@Composable
fun UpdatePagerState(homeViewModel: DateViewModel, pagerState: PagerState) {
    XLogger.d("================>UpdatePagerState")
    val homeUIState = homeViewModel.dateStateData.collectAsState().value
    // TODO: 处理pager的状态
//    val monthOffset = homeUIState.monthEntity.offset
//    val weekOffset = homeUIState.weekEntity.offset
//    val offset = if (homeUIState.weekModelFlag) {
//        weekOffset
//    } else {
//        monthOffset
//    }
//
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            XLogger.d("snapshotFlow============>$page")
            homeViewModel.dispatch(DateViewModel.HomeAction.UpdateData(page))
        }
    }
//
//    LaunchedEffect(key1 = homeUIState.needScrollPage, block = {
//        XLogger.d("滑动到：${homeUIState.needScrollPage}页")
//        if (homeUIState.needScrollPage >= 0 && homeUIState.needScrollPage != offset) {
//            pagerState.scrollToPage(homeUIState.needScrollPage)
//        }
//    })
}
/**
 * 星期信息
 */
@Composable
fun WeekRow() {
    val weekArray = stringArrayResource(id = R.array.week)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        weekArray.forEachIndexed { _, s ->
            Text(
                text = s,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * 年和月
 */
@Composable
fun YearAndMonth(homeViewModel: DateViewModel, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val homeUiState =
        homeViewModel.dateStateData.collectAsState()
    val currentDay = homeUiState.value.currentDay
    XLogger.d("YearAndMonth=======================>${currentDay}")
    //TODO：点击回到  年月日
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(modifier = Modifier.padding(start = 20.dp), onClick = {
            if (pagerState.currentPage - 1 >= 0) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "上个月",
                tint = Color.Black.copy(alpha = 0.6f)
            )
        }

        TextButton(onClick = {
            homeViewModel.dispatch(
                DateViewModel.HomeAction.ShowYearMonthSelectDialog(
                    true
                )
            )
        }) {
            Text(
                text = "${currentDay.year()}年${currentDay.month()}月${currentDay.day}日",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }

        IconButton(modifier = Modifier.padding(end = 20.dp), onClick = {
            if (pagerState.currentPage + 1 < 10000) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "下个月",
                tint = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * 获取 TextMeasurer 测量文字的高度
 */
@Composable
fun getTextMeasurerAndTextSize(): Pair<TextMeasurer, IntSize> {
    val textMeasurer = rememberTextMeasurer(cacheSize = 0)
    val textLayoutResult: TextLayoutResult =
        textMeasurer.measure(
            text = "9",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        )
    val textSize = textLayoutResult.size

    return Pair(textMeasurer, textSize)
}


@Preview(backgroundColor = 0xFF000000, widthDp = 327, heightDp = 687)
@Composable
fun DateScreenPreview() {
    DateScreen()
}