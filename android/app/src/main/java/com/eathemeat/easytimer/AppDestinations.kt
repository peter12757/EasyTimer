package com.eathemeat.easytimer
import androidx.annotation.StringRes

enum class AppDestinations(@StringRes val label: Int,
                           val icon: Int,
                           @StringRes val contentDescription: Int
) {
    HOME(R.string.nav_list, R.drawable.home_48px, R.string.nav_list),
    ADD(R.string.nav_add, R.drawable.notification_add_48px, R.string.nav_add),
    RECORDERS(R.string.nav_recorders,R.drawable.recorder_48px, R.string.nav_recorders),
    SETTING(R.string.nav_setting, R.drawable.setting_48px, R.string.nav_setting),
}
