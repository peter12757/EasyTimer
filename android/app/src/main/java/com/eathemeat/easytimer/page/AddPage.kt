package com.eathemeat.easytimer.page

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet

@Composable
fun AddPage() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        var (time,date) = createRefs()
            ConstraintLayout(modifier = Modifier.constrainAs(time){
                top.linkTo(parent.top,10.dp)
                start.linkTo(parent.start,10.dp)
                end.linkTo(parent.end,10.dp)
                bottom.linkTo(date.top,10.dp)
            }.padding(50.dp).border(2.dp, color = Color.White)) {
                Text(text = "Time", modifier = Modifier.constrainAs(createRef()) {
                    centerTo(parent)
                }, fontSize = TextUnit(30f, TextUnitType.Sp))

            }
        ConstraintLayout(modifier = Modifier.constrainAs(date){
            top.linkTo(time.bottom)
            start.linkTo(time.start)
            end.linkTo(time.end)
        }.padding(50.dp)) {
            Text(text = "Date", modifier = Modifier.constrainAs(createRef()) {
                centerTo(parent)
            },fontSize = TextUnit(30f, TextUnitType.Sp))
        }
    }
}

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun AddPagePreview() {
   AddPage()
}