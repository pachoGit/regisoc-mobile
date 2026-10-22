package com.pacho.regisoc.ui.features.lineup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun LineupDetailRoute(
    screen: Screen.LineupDetail,
    viewModel: LineupDetailViewModel,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    LaunchedEffect(screen.match.id, screen.lineupClubId) {
        viewModel.load(matchId = screen.match.id, clubId = screen.lineupClubId)
    }

    val uiState by viewModel.uiState.collectAsState()

    LineupDetailScreen(
        title = if (screen.isMine) "Mi plantilla" else "Plantilla rival",
        matchDate = screen.matchDate,
        clubName = screen.clubName,
        uiState = uiState,
        onBack = { navigator.navigateTo(Screen.Event.MatchDateDetail(screen.event, screen.matchDate)) },
        onTabSelected = navigator::navigateToTab,
        snackbarHost = snackbarHost
    )
}
