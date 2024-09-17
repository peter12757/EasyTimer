package com.eathemeat.easytimer.screen.alarm

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
            , contentDescription = "alarm", modifier = Modifier.constrainAs(createRef()){
                top.linkTo(parent.top,50.dp)
                start.linkTo(parent.start,50.dp)
                end.linkTo(parent.end,50.dp)
                bottom.linkTo(parent.bottom,50.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }.graphicsLayer {
                transformOrigin = TransformOrigin.Center
                rotationZ = animation.value
            })
        }
    }

@Preview(widthDp = 488, heightDp = 1024)
@Composable
fun AlarmScreenPreview() {
    AlarmScreen()
}