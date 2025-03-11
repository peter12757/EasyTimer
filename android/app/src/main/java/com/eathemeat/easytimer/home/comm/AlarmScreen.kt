package com.eathemeat.easytimer.home.comm

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.eathemeat.easytimer.R


@Composable
fun AlarmScreen() {
    ConstraintLayout {
        val animation  = rememberInfiniteTransition(label = "").animateFloat(
            initialValue = -5f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(tween(100),RepeatMode.Restart),
            label = "rotation"
        )
        Image(painter = painterResource(id = R.drawable.alarm_24px)
            , contentDescription = "alarm", modifier = Modifier
                .constrainAs(createRef()) {
                    top.linkTo(parent.top, 50.dp)
                    start.linkTo(parent.start, 50.dp)
                    end.linkTo(parent.end, 50.dp)
                    bottom.linkTo(parent.bottom, 50.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .graphicsLayer {
                    transformOrigin = TransformOrigin.Center
                    rotationZ = animation.value
                })
        Image(painter = painterResource(id = R.drawable.cancel_24px), contentDescription = "cancel",
            modifier = Modifier
                .constrainAs(createRef()) {
                    top.linkTo(parent.top, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .clickable {

                })
        }
    }

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun AlarmScreenPreview() {
    AlarmScreen()
}