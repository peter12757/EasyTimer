package com.eathemeat.easytimer.ui.home.date

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DateRouter

fun NavController.navigateToDate(
    navOptions: NavOptions? = null,
) {
    navigate(route = DateRouter, navOptions)
}

fun NavGraphBuilder.DateScreen() {
    composable<DateRouter> { backStackEntry->
        DateScreen()
    }
}