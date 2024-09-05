package com.eathemeat.easytimer.screen.time

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@SuppressLint("SuspiciousIndentation")
@Composable
fun AddScreen() {
    val TAG = "AddPage"
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (time,date) = createRefs()
            ConstraintLayout(modifier = Modifier
                .constrainAs(time) {
                    top.linkTo(parent.top, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                    bottom.linkTo(date.top)
                }
                .padding(10.dp)
                .background(Color.Yellow)
                .clip(RoundedCornerShape(20.dp))) {
                val (title,add,detail) = createRefs()
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
                top.linkTo(time.bottom)
                start.linkTo(time.start)
                end.linkTo(time.end)
            }
            .padding(10.dp)
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
                bottom.linkTo(parent.bottom, 10.dp )
            }) {
                Text(text = "Detail")
            }

        }
    }
}

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun AddScreenPreview() {
    AddScreen()
}