package com.eathemeat.easytimer.screen.time

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.easytimer.AddScreenType
import com.eathemeat.easytimer.EasyAlarmManager
import com.eathemeat.easytimer.MainViewModel

val millisecond = 1000L
val second = 60L
val minute = millisecond*second
//val minute = 100L


val defTimeMap = mutableMapOf<String,Long>().apply {
    put("1分钟",minute)
    put("3分钟",minute*3)
    put("4分钟",minute*4)
    put("5分钟",minute*5)
    put("10分钟",minute*10)
    put("30分钟",minute*30)
    put("1小时",minute*60)

}

@Composable
fun TimeAddScreen() {
    val TAG = "TimeAddPage"
    val viewModel = viewModel(modelClass = MainViewModel::class.java)
    var addTime by remember {
        mutableStateOf(0L)
    }
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        var (list,add) = createRefs()
        LazyColumn(modifier = Modifier.constrainAs(list){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }) {
            items(defTimeMap.keys.toList()) {

                ConstraintLayout(modifier = Modifier
                    .fillMaxSize()) {
                    var (check,text) = createRefs()
                    val centerLine = createGuidelineFromStart(0.5f)
                    Checkbox(checked = addTime == defTimeMap[it], onCheckedChange = { checked->
                        if (checked) {
                            addTime = defTimeMap[it]!!
                        }
                    }, modifier = Modifier.constrainAs(check){
                        end.linkTo(centerLine,5.dp)

                    })
                    Text(text = it, modifier = Modifier
                        .constrainAs(text) {
                            start.linkTo(centerLine, 5.dp)
                        }
                        .padding(10.dp)
                        , fontSize = TextUnit(25f, TextUnitType.Sp), textAlign = TextAlign.Center)
                }

            }
        }
        Button(onClick = {
            Log.d(TAG, "TimeAddPage() called")
            if (addTime>0) {
                EasyAlarmManager.addAlarm(addTime)
                viewModel.screenType.value = AddScreenType.ADD
            }

         }, modifier = Modifier
            .constrainAs(add) {
                top.linkTo(list.bottom, 20.dp)
                start.linkTo(parent.start, 20.dp)
                end.linkTo(parent.end, 20.dp)
                bottom.linkTo(parent.bottom, 20.dp)
            }
            .clip(RoundedCornerShape(10.dp))) {
            Text(text = "Add", modifier = Modifier.fillMaxWidth(), fontSize = TextUnit(20f, TextUnitType.Sp), textAlign = TextAlign.Center)
        }

    }
}

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun TimeAddPagePreview() {
    TimeAddScreen()
}