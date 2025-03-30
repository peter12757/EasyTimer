package com.eathemeat.easytimer.ui.home.todo

import android.view.LayoutInflater
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.easytimer.R


@Composable
fun TodoScreen(viewmodule: TodoViewModel = viewModel()) {
    AndroidView(factory = { context->
        LayoutInflater.from(context).inflate(R.layout.todoscreen, null, false)
    }, modifier = Modifier.fillMaxWidth().fillMaxHeight()) { rootView ->
        val menu = rootView.findViewById<ImageView>(R.id.todo_menu)

    }



}



@Preview(backgroundColor = 0xFF000000, widthDp = 327, heightDp = 687)
@Composable
fun TodoScreenPreview() {
    TodoScreen()
}
