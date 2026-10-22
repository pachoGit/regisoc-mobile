package com.pacho.regisoc.ui.features.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.ui.account.LocalAccountActions
import com.pacho.regisoc.ui.layouts.MainLayout
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun AboutRoute(
    sessionManager: SessionManager,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    val sessionData by sessionManager.sessionData.collectAsState()
    val accountActions = LocalAccountActions.current

    MainLayout(
        title = "Sobre mi",
        onBackClick = { navigator.navigateTo(Screen.Home) },
        selectedTab = 0,
        onTabSelected = navigator::navigateToTab,
        snackbarHost = snackbarHost
    ) {
        AboutScreen(
            model = sessionData.toAboutUiModel(accountActions.clubCrestUrl)
        )
    }
}
