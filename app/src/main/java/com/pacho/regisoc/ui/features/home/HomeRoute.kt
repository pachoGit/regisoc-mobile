package com.pacho.regisoc.ui.features.home

import androidx.compose.runtime.Composable
import com.pacho.regisoc.ui.features.club.ClubUiState
import com.pacho.regisoc.ui.layouts.MainLayout
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun HomeRoute(
    uiState: ClubUiState,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    MainLayout(
        title = "Inicio",
        selectedTab = 0,
        onTabSelected = navigator::navigateToTab,
        snackbarHost = snackbarHost
    ) {
        HomeScreen(
            uiState = uiState,
            onViewClub = { navigator.navigateTo(Screen.Club.Detail(it)) }
        )
    }
}