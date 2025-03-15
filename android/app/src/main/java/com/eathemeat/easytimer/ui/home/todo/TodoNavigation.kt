package com.eathemeat.easytimer.ui.home.todo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.eathemeat.easytimer.ui.DEEP_LINK_URI_PATTERN
import kotlinx.serialization.Serializable


@Serializable data object TodoRouter


fun NavController.navigateToTodo(navOptions: NavOptions) = navigate(route = TodoRouter, navOptions)

fun NavGraphBuilder.todoSection() {
        composable<TodoRouter>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = DEEP_LINK_URI_PATTERN
                },
            ),
        ){
            TodoScreen()
        }
}