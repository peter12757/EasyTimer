package com.eathemeat.easytimer.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.eathemeat.easytimer.data.ClockInfo

@Composable
fun TimeDetailPage(times:List<ClockInfo>) {
    val TAG = "TimeAddPage"
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        var (list,add) = createRefs()
        LazyColumn(modifier = Modifier.constrainAs(list){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }) {
            items(times) {
                Text(text = "闹钟剩余时间：${it.getResetTimeStr()}", modifier = Modifier.fillMaxSize().padding(20.dp)
                    , fontSize = TextUnit(25f, TextUnitType.Sp), textAlign = TextAlign.Center)
            }
        }
        Button(onClick = {
            Log.d(TAG, "TimeDetailPage() called")
        }, modifier = Modifier
            .constrainAs(add) {
                top.linkTo(list.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .background(Color.Blue)
            .clip(RoundedCornerShape(10.dp))) {
            Text(text = "Del", modifier = Modifier.fillMaxWidth(), fontSize = TextUnit(20f, TextUnitType.Sp), textAlign = TextAlign.Center)
        }

    }
}

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun TimeDetailPagePreview() {
    TimeDetailPage(listOf())
}