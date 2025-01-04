package com.eathemeat.easytimer.screen

import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.easytimer.MainViewModel
import com.eathemeat.easytimer.R


@Composable
fun HomeScreen() {
    var viewmodule = viewModel(MainViewModel::class.java)
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()) {
        var centerLine = createGuidelineFromTop(0.5f)
        var (date,time) = createRefs()

        Text(text = "Date:${viewmodule.timeNow.first}", modifier = Modifier.constrainAs(date){
            centerHorizontallyTo(parent)
            bottom.linkTo(centerLine,10.dp)
        })
        Text(text = "Time:${viewmodule.timeNow.second}", modifier = Modifier.constrainAs(time){
            centerHorizontallyTo(parent)
            top.linkTo(centerLine,10.dp)
        })
    }


}

@Preview
@Composable()
fun HomeScreenPreview() {
    HomeScreen()
}
