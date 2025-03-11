package com.eathemeat.easytimer.home
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.easyicons.EasyIcons
import com.eathemeat.easytimer.easyicons.nav.Add
import com.eathemeat.easytimer.easyicons.nav.Home
import com.eathemeat.easytimer.easyicons.nav.Recorders
import com.eathemeat.easytimer.easyicons.nav.Setting
import com.eathemeat.easytimer.home.date.AddScreen
import com.eathemeat.easytimer.home.mine.SettingScreen
import com.eathemeat.easytimer.home.note.RecorderScreen
import com.eathemeat.easytimer.home.todo.HomeRouter
import com.eathemeat.easytimer.home.todo.HomeScreen
import kotlin.reflect.KClass

enum class AppDestinations(@StringRes val label: Int,
                           val icon: ImageVector,val route:KClass<*>,
                           @StringRes val contentDescription: Int,
                           val screen: @Composable () -> Unit
) {
    HOME(R.string.nav_home, EasyIcons.NAV.Home, HomeRouter::class, R.string.nav_home, { HomeScreen() }),
    ADD(R.string.nav_add, EasyIcons.NAV.Add,"ADD", R.string.nav_add,{ AddScreen()}),
    RECORDERS(
        R.string.nav_recorders,EasyIcons.NAV.Recorders,"RECORDERS", R.string.nav_recorders,
        { RecorderScreen() }
    ),
    SETTING(R.string.nav_setting, EasyIcons.NAV.Setting,"SETTING", R.string.nav_setting,{ SettingScreen()}),
}
