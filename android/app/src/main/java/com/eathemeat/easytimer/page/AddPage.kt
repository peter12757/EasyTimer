package com.eathemeat.easytimer.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet


val TAG = "AddPage"
@Composable
fun AddPage() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        var (time,date) = createRefs()
            ConstraintLayout(modifier = Modifier
                .constrainAs(time) {
                    top.linkTo(parent.top, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                    bottom.linkTo(date.top, 10.dp)
                }
                .padding(50.dp)
                .background(Color.Yellow)
                .clip(RoundedCornerShape(10.dp))) {
                var (title,add,detail) = createRefs()
                Text(text = "Time", modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                    bottom.linkTo(add.top, 10.dp)
                }, fontSize = TextUnit(30f, TextUnitType.Sp))
                Button(onClick = {
                    Log.d(TAG, "AddPage() called time add")
                }, modifier = Modifier.constrainAs(add) {
                    top.linkTo(title.bottom, 10.dp)
                    start.linkTo(title.start, 10.dp)
                    end.linkTo(detail.start, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                }) {
                    Text(text = "Add")
                }
                Button(onClick = {
                    Log.d(TAG, "AddPage() called time detail")
                }, modifier = Modifier.constrainAs(detail) {
                    top.linkTo(add.top)
                    start.linkTo(add.end, 10.dp)
                    end.linkTo(title.end, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                }) {
                    Text(text = "Detail")
                }

            }
        ConstraintLayout(modifier = Modifier
            .constrainAs(date) {
                top.linkTo(time.bottom, 10.dp)
                start.linkTo(time.start)
                end.linkTo(time.end)
                bottom.linkTo(parent.bottom, 10.dp)
            }
            .padding(50.dp)
            .background(Color.Green)
            .clip(RoundedCornerShape(10.dp))) {
            var (title,add,detail) = createRefs()
            Text(text = "Date", modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 10.dp)
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(add.top, 10.dp)
            },fontSize = TextUnit(30f, TextUnitType.Sp))

            Button(onClick = {
                Log.d(TAG, "AddPage() called date add")
            }, modifier = Modifier.constrainAs(add) {
                top.linkTo(title.bottom, 10.dp)
                start.linkTo(title.start, 10.dp)
                end.linkTo(detail.start, 10.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }) {
                Text(text = "Add")
            }
            Button(onClick = {
                Log.d(TAG, "AddPage() called date detail")
            }, modifier = Modifier.constrainAs(detail) {
                top.linkTo(add.top)
                start.linkTo(add.end, 10.dp)
                end.linkTo(title.end, 10.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }) {
                Text(text = "Detail")
            }

        }
    }
}

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun AddPagePreview() {
   AddPage()
}