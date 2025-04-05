package com.eathemeat.easytimer.ui.home.date

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.easytimer.R
import com.future.composecalendar.utils.XLogger
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearMonthSelectDialog(viewModel: DateViewModel = viewModel(), pagerState: PagerState) {
    val homeUIState = viewModel.dateStateData.collectAsState().value


    if (homeUIState.showYearMonthDialog) {
        val coroutineScope = rememberCoroutineScope()
        val monthArray = integerArrayResource(id = R.array.month)
        val yearList = mutableListOf<Int>().apply {
            for (year in 0..200) {
                add(year+homeUIState.currentDay.year()-100)
            }
        }


        val listState = rememberLazyListState()
        LaunchedEffect(key1 = Unit, block = {
            listState.scrollToItem(index = homeUIState.currentDay.year())
        })
        val dialogPagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { 2 },
        )

        var selectMonth by remember {
            mutableStateOf(homeUIState.currentDay.month())
        }

        var selectYear by remember {
            mutableStateOf(homeUIState.currentDay.year())
        }

        XLogger.d("------>$selectMonth    $selectYear")

        AlertDialog(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight(),
            onDismissRequest = {
                viewModel.dispatch(
                    DateViewModel.HomeAction.ShowYearMonthSelectDialog(
                        false
                    )
                )
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .background(
                        color = Color.White, shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                coroutineScope.launch {
                                    dialogPagerState.scrollToPage(0)
                                }
                            },
                        text = "${selectYear}年",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                coroutineScope.launch {
                                    dialogPagerState.scrollToPage(1)
                                }
                            },
                        text = "${selectMonth}月",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth(),
//                    pageCount = 2,
                    state = dialogPagerState
                ) { page ->
                    if (page == 0) {
                        LazyRow(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                            state = listState,
                            content = {
                                itemsIndexed(items = yearList, key = { index, _ ->
                                    index
                                }, itemContent = { _, content ->
                                    Text(text = "$content",
                                        color = if (selectYear == content) Color.Red else Color.Black,
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .clickable {
                                                selectYear = content
                                            }
                                            .padding(horizontal = 6.dp, vertical = 6.dp)
                                    )
                                })
                            })
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth(),
                            columns = GridCells.Fixed(4), content = {
                                itemsIndexed(items = monthArray.asList(), key = { index, _ ->
                                    index
                                }, itemContent = { _, content ->
                                    Text(text = "${content}月",
                                        color = if (selectMonth == content) Color.Red else Color.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectMonth = content
                                            }
                                            .padding(vertical = 10.dp)
                                    )
                                })
                            })
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(modifier = Modifier.padding(horizontal = 20.dp),
                        onClick = {
                            viewModel.dispatch(
                                DateViewModel.HomeAction.ShowYearMonthSelectDialog(
                                    false
                                )
                            )
                        }) {
                        Text(text = "取消")
                    }

                    TextButton(modifier = Modifier.padding(horizontal = 20.dp),
                        onClick = {
                            //月份的跨度
                            val calendar = Calendar.getInstance()

                            val todayYear = calendar.get(Calendar.YEAR)
                            val todayMonth = calendar.get(Calendar.MONTH)

                            //月份的跨度
                            val yearDiff: Int = selectYear - todayYear
                            val monthDiff: Int = selectMonth - todayMonth
                            val totalMonthDiff = yearDiff * 12 + monthDiff

                            XLogger.d("todayYear:$todayYear todayMonth:$todayMonth  selectYear:$selectYear  selectMonth:$selectMonth  totalMonthDiff:$totalMonthDiff")

                            coroutineScope.launch {
                                XLogger.d("=========>${totalMonthDiff + 5000}")
                                pagerState.scrollToPage(totalMonthDiff + 5000 - 1)
                            }
                            viewModel.dispatch(
                                DateViewModel.HomeAction.ShowYearMonthSelectDialog(
                                    false
                                )
                            )
                        }) {
                        Text(text = "确定")
                    }
                }
            }
        }
    }
}