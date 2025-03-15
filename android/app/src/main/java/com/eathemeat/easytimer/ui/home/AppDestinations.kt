package com.eathemeat.easytimer.ui.home


import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.ui.home.date.DateRouter
import com.eathemeat.easytimer.ui.home.date.DateScreen
import com.eathemeat.easytimer.ui.home.mine.MineRouter
import com.eathemeat.easytimer.ui.home.mine.MineScreen
import com.eathemeat.easytimer.ui.home.note.NoteRouter
import com.eathemeat.easytimer.ui.home.note.NoteScreen
import com.eathemeat.easytimer.ui.home.todo.TodoRouter
import com.eathemeat.easytimer.ui.home.todo.TodoScreen

enum class AppDestinations(@StringRes val label: Int,
                           val icon: Int, val router: Any,
                           @StringRes val contentDescription: Int,
                           val screen: @Composable () -> Unit
) {
    ToDo(R.string.nav_todo, R.drawable.nav_todo, TodoRouter, R.string.nav_todo, { TodoScreen() }),
    DATE(R.string.nav_date, R.drawable.nav_date, DateRouter, R.string.nav_date,{ DateScreen() }),
    NOTE(
        R.string.nav_note,R.drawable.nav_note, NoteRouter, R.string.nav_note,
        { NoteScreen() }
    ),
    MINE(R.string.nav_mine, R.drawable.nav_mine, MineRouter, R.string.nav_mine,{ MineScreen() }),
}
