package com.eathemeat.easytimer.ui

private const val DEEP_LINK_SCHEME_AND_HOST = "https://www.eathemeat.top"
private const val DEEP_LINK_HOME_PATH = "home"
private const val DEEP_LINK_BASE_PATH = "$DEEP_LINK_SCHEME_AND_HOST/$DEEP_LINK_HOME_PATH"
const val DEEP_LINK_NEWS_RESOURCE_ID_KEY = "linkedNewsResourceId"
const val DEEP_LINK_URI_PATTERN = "$DEEP_LINK_BASE_PATH/{$DEEP_LINK_NEWS_RESOURCE_ID_KEY}"