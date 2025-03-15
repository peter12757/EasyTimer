package com.eathemeat.easytimer.ui.home.note

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object NoteRouter

fun NavController.navigateToNote(
    navOptions: NavOptions? = null,
) {
    navigate(route = NoteRouter, navOptions)
}

fun NavGraphBuilder.NoteScreen() {
    composable<NoteRouter> {
        NoteScreen()
    }
}