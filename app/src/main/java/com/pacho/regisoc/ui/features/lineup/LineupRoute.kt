package com.pacho.regisoc.ui.features.lineup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun LineupRoute(
    screen: Screen.Lineup,
    viewModel: LineupViewModel,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    LaunchedEffect(screen.match.id) {
        viewModel.load(screen.match.id)
    }

    val uiState by viewModel.uiState.collectAsState()
    val players by viewModel.players.collectAsState()
    val coaches by viewModel.coaches.collectAsState()
    val physicalTrainers by viewModel.physicalTrainers.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    LineupScreen(
        matchDate = screen.matchDate,
        uiState = uiState,
        players = players,
        coaches = coaches,
        physicalTrainers = physicalTrainers,
        searchQuery = searchQuery,
        isSaving = isSaving,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onTogglePlayer = viewModel::togglePlayer,
        onSelectCoach = viewModel::selectCoach,
        onSelectPhysicalTrainer = viewModel::selectPhysicalTrainer,
        onSaveDraft = viewModel::saveDraft,
        onCloseLineup = viewModel::closeLineup,
        onBack = {
            navigator.navigateTo(Screen.Event.MatchDateDetail(screen.event, screen.matchDate))
        },
        onTabSelected = navigator::navigateToTab,
        snackbarHost = snackbarHost
    )
}