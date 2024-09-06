package com.eathemeat.easytimer.screen.alarm

import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.eathemeat.easytimer.R


@Composable
fun AlarmScreen() {
    ConstraintLayout {
        Image(painter = painterResource(id = R.drawable.alarm_24px)
            , contentDescription = "alarm", modifier = Modifier.constrainAs(createRef()){
                top.linkTo(parent.top,50.dp)
                start.linkTo(parent.start,50.dp)
                end.linkTo(parent.end,50.dp)
                bottom.linkTo(parent.bottom,50.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        }
    }

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun AlarmScreenPreview() {
    AlarmScreen()
}