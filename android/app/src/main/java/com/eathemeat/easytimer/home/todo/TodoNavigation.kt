package com.eathemeat.easytimer.home.todo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable


@Serializable data object HomeRouter
@Serializable data object HomeBaseRouter

private const val DEEP_LINK_SCHEME_AND_HOST = "https://www.nowinandroid.apps.samples.google.com"
private const val DEEP_LINK_FOR_YOU_PATH = "home"
private const val DEEP_LINK_BASE_PATH = "$DEEP_LINK_SCHEME_AND_HOST/$DEEP_LINK_FOR_YOU_PATH"
const val DEEP_LINK_NEWS_RESOURCE_ID_KEY = "linkedNewsResourceId"
const val DEEP_LINK_URI_PATTERN = "$DEEP_LINK_BASE_PATH/{$DEEP_LINK_NEWS_RESOURCE_ID_KEY}"

fun NavController.navigateToForYou(navOptions: NavOptions) = navigate(route = HomeRouter, navOptions)

fun NavGraphBuilder.forYouSection(
    onTopicClick: (String) -> Unit,
    topicDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<HomeBaseRouter>(startDestination = HomeRouter) {
        composable<HomeRouter>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = DEEP_LINK_URI_PATTERN
                },
            ),
        ) {
            HomeScreen()
        }
        topicDestination()
    }
}