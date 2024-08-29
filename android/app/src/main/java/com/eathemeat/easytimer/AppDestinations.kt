package com.eathemeat.easytimer
import androidx.compose.ui.res.vectorResource
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons

import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestinations(@StringRes val label: Int,
                           val icon: ImageVector,
                           @StringRes val contentDescription: Int
) {
    HOME(R.string.nav_home, vectorResource(res = R.drawable.home_48px), R.string.nav_home),
    ADD(R.string.nav_add, vectorResource(res = R.drawable.home_48px), R.string.nav_add),
    RECORDERS(R.string.nav_recorders,vectorResource(res = R.drawable.home_48px), R.string.nav_recorders),
    SETTING(R.string.nav_setting, vectorResource(res = R.drawable.home_48px), R.string.nav_setting),
}
