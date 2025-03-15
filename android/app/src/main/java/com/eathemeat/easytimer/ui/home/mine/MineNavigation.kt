package com.eathemeat.easytimer.ui.home.mine

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable data object MineRouter

fun NavController.navigateToMine(
    navOptions: NavOptions? = null,
) {
    navigate(route = MineRouter, navOptions)
}

fun NavGraphBuilder.MineScreen() {
    composable<MineRouter> {
        MineScreen()
    }
}