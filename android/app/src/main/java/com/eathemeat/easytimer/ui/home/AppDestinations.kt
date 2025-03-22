package com.eathemeat.easytimer.ui.home


import androidx.annotation.StringRes
import androidx.navigation.NavGraphBuilder
import com.eathemeat.easytimer.ui.home.date.DateNavigation
import com.eathemeat.easytimer.ui.home.mine.MineNavigation
import com.eathemeat.easytimer.ui.home.note.NoteNavigation
import com.eathemeat.easytimer.ui.home.todo.ToDoNavigation

data class NavigationItem(@StringRes val label: Int,
                          val icon: Int, val router: Any,
                          @StringRes val contentDescription: Int,
                          val screen: NavGraphBuilder.() -> Unit)

val AppDestinations = mutableListOf<NavigationItem>().apply {
    add(ToDoNavigation)
    add(DateNavigation)
    add(NoteNavigation)
    add(MineNavigation)
}
