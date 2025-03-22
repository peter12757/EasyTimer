package com.eathemeat.easytimer.ui.home.mine

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.ui.home.NavigationItem
import kotlinx.serialization.Serializable

@Serializable data object MineRouter

val MineNavigation = NavigationItem(
    R.string.nav_mine, R.drawable.nav_mine, MineRouter, R.string.nav_mine,
    {
        composable<MineRouter> {
            MineScreen()
        }
    }
)

fun NavController.navigateToMine(
    navOptions: NavOptions? = null,
) {
    navigate(route = MineRouter, navOptions)
}

fun NavGraphBuilder.NavMineScreen() {
    composable<MineRouter> {
        MineScreen()
    }
}