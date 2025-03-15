package com.eathemeat.easytimer.ui.home.todo

import android.view.LayoutInflater
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.ui.home.MainViewModel


@Composable
fun TodoScreen() {
    var viewmodule = viewModel(MainViewModel::class.java)
//    ConstraintLayout {
//        var (title,menu,content) =createRefs()
//        var centerLine = createGuidelineFromTop(0.5f)
//        Text(text = "TODO", fontSize = 30.sp, color = Color.White, style = TextStyle.Default, maxLines = 1,
//            modifier = Modifier.constrainAs(title){
//                start.linkTo(parent.start,10.dp)
//                top.linkTo(parent.top,20.dp)
//
//            }
//        )
//        Image(imageVector = ImageVector.vectorResource(R.drawable.nav_todo), contentDescription = "menu",
//            modifier = Modifier.constrainAs(menu) {
//                start.linkTo(title.end)
//                end.linkTo(parent.end,10.dp)
//                top.linkTo(parent.top,20.dp)
//
//        })
//    }
    AndroidView(factory = { context->
        LayoutInflater.from(context).inflate(R.layout.todoscreen, null, false)
    }) { rootView ->
        val menu = rootView.findViewById<ImageView>(R.id.todo_menu)

    }



}



@Preview(backgroundColor = 0xFF000000, widthDp = 327, heightDp = 687)
@Composable
fun TodoScreenPreview() {
    TodoScreen()
}
