package com.eathemeat.easytimer.ui.home.date

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.ui.home.NavigationItem
import com.eathemeat.easytimer.ui.home.mine.MineRouter
import kotlinx.serialization.Serializable

@Serializable
data object DateRouter

val DateNavigation = NavigationItem(R.string.nav_date, R.drawable.nav_date, DateRouter, R.string.nav_date,{
    composable<MineRouter> {
        DateScreen()
    }
})

fun NavController.navigateToDate(
    navOptions: NavOptions? = null,
) {
    navigate(route = DateRouter, navOptions)
}

fun NavGraphBuilder.NavDateScreen() {
    composable<DateRouter> {
        DateScreen()
    }
}