package com.eathemeat.easytimer.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eathemeat.easytimer.ui.home.date.NavDateScreen
import com.eathemeat.easytimer.ui.home.mine.NavMineScreen
import com.eathemeat.easytimer.ui.home.note.NavNoteScreen
import com.eathemeat.easytimer.ui.home.todo.NavTodoScreen
import com.eathemeat.easytimer.ui.home.todo.TodoRouter
import com.eathemeat.transkit.main.ui.theme.EasyTimerTheme
import com.eathemeat.transkit.main.ui.theme.NavigationSel

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)


        setContent {
            EasyTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }

    }


}

    data class ThemeSettings(
        val darkTheme: Boolean,
        val androidTheme: Boolean,
        val disableDynamicTheming: Boolean,
    )

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: HomeViewModel = viewModel(HomeViewModel::class.java)) {
    val navController = rememberNavController()
    navController.setLifecycleOwner(LocalLifecycleOwner.current)
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = NavigationSel
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                AppDestinations.forEach { destination ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(destination.icon),
                                contentDescription = stringResource(destination.contentDescription)
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(destination.router::class) }==true,
                        onClick = {
                            navController.navigate(destination.router) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                    inclusive = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                    )
                }
            }
        }
    ) {
        NavHost(navController = navController,
            startDestination = TodoRouter) {
            NavTodoScreen()
            NavMineScreen()
            NavDateScreen()
            NavNoteScreen()
        }
    }
}


@Preview
@Composable
fun MainPagePre() {
    MainScreen()
}