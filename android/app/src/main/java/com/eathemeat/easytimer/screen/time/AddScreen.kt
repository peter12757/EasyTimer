package com.eathemeat.easytimer.screen.time

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.easytimer.AddScreenType
import com.eathemeat.easytimer.MainViewModel



@Composable
fun AddScreen() {
    val TAG = "AddScreen"
    var viewModel = viewModel(modelClass = MainViewModel::class.java)
    var screenType by rememberSaveable {
        viewModel.screenType
    }
    when(screenType) {
        AddScreenType.ADD-> AddMainScreen()
        AddScreenType.TIMEADD -> TimeAddScreen()
        AddScreenType.TIMEDETAIL -> TimeDetailPage()
        AddScreenType.DATEADD -> TODO()
        AddScreenType.DATEDETAIL -> TODO()
        else ->{
            Log.e(TAG, "AddScreen: unknow screen type", Throwable())
        }
    }


}

@Composable
fun AddMainScreen() {
    val TAG = "AddMainScreen"
    val viewModel = viewModel(modelClass = MainViewModel::class.java)
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (time,date) = createRefs()
//        createVerticalChain(
//            time,date, chainStyle = ChainStyle.Packed
//        )
        ConstraintLayout(modifier = Modifier
            .constrainAs(time) {
                top.linkTo(parent.top, 10.dp)
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(date.top)
                width = Dimension.fillToConstraints
            }
            .padding(10.dp)
            .background(Color.Yellow)
            .clip(RoundedCornerShape(20.dp))) {
            val (title,add,detail) = createRefs()
            Text(text = "Time", modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 30.dp)
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(add.top, 10.dp)
            }, fontSize = TextUnit(30f, TextUnitType.Sp), color = Color.Black)
            Button(onClick = {
                Log.d(TAG, "AddPage() called time add")
                viewModel.screenType.value = AddScreenType.TIMEADD
            }, modifier = Modifier.constrainAs(add) {
                top.linkTo(title.bottom, 30.dp)
                start.linkTo(title.start, 10.dp)
                end.linkTo(detail.start, 10.dp)
                bottom.linkTo(parent.bottom, 30.dp)
            }) {
                Text(text = "Add")
            }
            Button(onClick = {
                Log.d(TAG, "AddPage() called time detail")
                viewModel.screenType.value = AddScreenType.TIMEDETAIL
            }, modifier = Modifier.constrainAs(detail) {
                top.linkTo(add.top)
                start.linkTo(add.end, 10.dp)
                end.linkTo(title.end, 10.dp)
                bottom.linkTo(parent.bottom, 30.dp)
            }) {
                Text(text = "Detail")
            }

        }
        ConstraintLayout(modifier = Modifier
            .constrainAs(date) {
                top.linkTo(time.bottom)
                start.linkTo(time.start)
                end.linkTo(time.end)
                width = Dimension.fillToConstraints
            }
            .padding(10.dp)
            .background(Color.Green)
            .clip(RoundedCornerShape(10.dp))) {
            var (title,add,detail) = createRefs()
            Text(text = "Date", modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 30.dp)
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(add.top, 10.dp)
            },fontSize = TextUnit(30f, TextUnitType.Sp), color = Color.Black)

            Button(onClick = {
                Log.d(TAG, "AddPage() called date add")
                viewModel.screenType.value = AddScreenType.DATEADD
            }, modifier = Modifier.constrainAs(add) {
                top.linkTo(title.bottom, 30.dp)
                start.linkTo(title.start, 10.dp)
                end.linkTo(detail.start, 10.dp)
                bottom.linkTo(parent.bottom, 30.dp)
            }) {
                Text(text = "Add")
            }
            Button(onClick = {
                Log.d(TAG, "AddPage() called date detail")
                viewModel.screenType.value = AddScreenType.DATEDETAIL
            }, modifier = Modifier.constrainAs(detail) {
                top.linkTo(add.top)
                start.linkTo(add.end, 10.dp)
                end.linkTo(title.end, 10.dp)
                bottom.linkTo(parent.bottom, 30.dp )
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