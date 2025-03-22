package com.eathemeat.easytimer.ui.home.todo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.ui.DEEP_LINK_URI_PATTERN
import com.eathemeat.easytimer.ui.home.NavigationItem
import kotlinx.serialization.Serializable


@Serializable data object TodoRouter

val ToDoNavigation = NavigationItem(R.string.nav_todo, R.drawable.nav_todo, TodoRouter, R.string.nav_todo) {

}

fun NavController.navigateToTodo(navOptions: NavOptions) = navigate(route = TodoRouter, navOptions)

fun NavGraphBuilder.NavTodoScreen() {
    composable<TodoRouter>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEP_LINK_URI_PATTERN
            },
        ),
    ) {
        TodoScreen()
    }
}