package com.eathemeat.easytimer

import android.os.Bundle
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.eathemeat.easytimer.databinding.ActivityMainBinding
import com.eathemeat.transkit.main.ui.theme.EasyTimerTheme

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage()

                }
            }
        }

    }
}

@Composable
fun ListPage() {
    Text(text = "ListPage")
}

@Composable
fun AddPage() {
    Text(text = "AddPage")
}

@Composable
fun RecoderPage() {
    Text(text = "RecoderPage")
}


@Composable
fun SettingPage() {
    Text(text = "SettingPage")
}




@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HomePage() {
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
            AppDestinations.LIST -> ListPage()
            AppDestinations.ADD -> AddPage()
            AppDestinations.RECORDERS -> RecoderPage()
            AppDestinations.SETTING -> SettingPage()
        }
    }

}


@Preview
@Composable
fun HomePagePre() {
    HomePage()
}