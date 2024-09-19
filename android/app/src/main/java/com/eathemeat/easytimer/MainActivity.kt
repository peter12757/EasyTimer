package com.eathemeat.easytimer

import android.os.Bundle
import android.view.Window
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.eathemeat.easytimer.screen.ListScreen
import com.eathemeat.easytimer.screen.RecorderScreen
import com.eathemeat.easytimer.screen.SettingScreen
import com.eathemeat.easytimer.screen.time.AddScreen
import com.eathemeat.transkit.main.ui.theme.EasyTimerTheme

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            EasyTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }

    }
}

@Composable
fun HomeScreen() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.LIST) }
    // [START android_compose_adaptivelayouts_sample_navigation_suite_scaffold_item_colors]
    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            for (it in AppDestinations.values()) {
                item(
                    icon = {
                        Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it },
                    colors = myNavigationSuiteItemColors,
                )
            }
        },navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color.Transparent,
        )
    ) {
        when (currentDestination) {
            AppDestinations.LIST -> ListScreen()
            AppDestinations.ADD -> AddScreen()
            AppDestinations.RECORDERS -> RecorderScreen()
            AppDestinations.SETTING -> SettingScreen()
        }
    }
}


@Preview
@Composable
fun HomePagePre() {
    HomeScreen()
}