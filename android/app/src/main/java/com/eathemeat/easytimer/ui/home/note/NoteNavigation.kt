package com.eathemeat.easytimer.ui.home.note

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.ui.home.NavigationItem
import kotlinx.serialization.Serializable

@Serializable
data object NoteRouter

val NoteNavigation = NavigationItem(
    R.string.nav_note,R.drawable.nav_note, NoteRouter, R.string.nav_note
) {
    composable<NoteRouter> {
        NoteScreen()
    }
}

fun NavGraphBuilder.NavNoteScreen() {
    composable<NoteRouter> {
        NoteScreen()
    }
}