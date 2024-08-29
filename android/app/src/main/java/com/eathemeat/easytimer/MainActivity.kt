package com.eathemeat.easytimer

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.eathemeat.easytimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}

@Composable
fun ListPage(navController: NavHostController) {

}

@Composable
fun AddPage(navController: NavHostController) {

}

@Composable
fun RecoderPage(navController: NavHostController) {

}


@Composable
fun SettingPage(navController: NavHostController) {

}




@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HomePage() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    // [START android_compose_adaptivelayouts_sample_navigation_suite_scaffold_container_color]
    NavigationSuiteScaffold(
        navigationSuiteItems = { /* ... */ },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        // Content...
    }
    // [END android_compose_adaptivelayouts_sample_navigation_suite_scaffold_container_color]

    // [START android_compose_adaptivelayouts_sample_navigation_suite_scaffold_suite_colors]
    NavigationSuiteScaffold(
        navigationSuiteItems = { /* ... */ },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color.Transparent,
        )
    ) {
        // Content...
    }
    // [END android_compose_adaptivelayouts_sample_navigation_suite_scaffold_suite_colors]

    // [START android_compose_adaptivelayouts_sample_navigation_suite_scaffold_item_colors]
    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            drawableResource(),
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it },
                    colors = myNavigationSuiteItemColors,
                )
            }
        },
    ) {
        // Content...
    }
    // [END android_compose_adaptivelayouts_sample_navigation_suite_scaffold_item_colors]

}

@Preview
@Composable
fun HomePagePre() {
    HomePage()
}