package com.pacho.regisoc.ui.features.staff

import androidx.compose.runtime.Composable
import com.pacho.regisoc.ui.features.coach.CoachUiState
import com.pacho.regisoc.ui.features.coach.CoachViewModel
import com.pacho.regisoc.ui.features.physicaltrainer.PhysicalTrainerUiState
import com.pacho.regisoc.ui.features.physicaltrainer.PhysicalTrainerViewModel
import com.pacho.regisoc.ui.features.player.PlayerUiState
import com.pacho.regisoc.ui.features.player.PlayerViewModel
import com.pacho.regisoc.ui.layouts.MainLayout
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun StaffRoute(
    uiState: PlayerUiState,
    coachUiState: CoachUiState,
    physicalTrainerUiState: PhysicalTrainerUiState,
    viewModel: PlayerViewModel,
    coachViewModel: CoachViewModel,
    physicalTrainerViewModel: PhysicalTrainerViewModel,
    navigator: AppNavigator,
    selectedTab: Int,
    onSelectedTabChange: (Int) -> Unit,
    snackbarHost: @Composable () -> Unit
) {
    MainLayout(
        title = "Plantilla",
        selectedTab = 2,
        onTabSelected = navigator::navigateToTab,
        snackbarHost = snackbarHost
    ) {
        StaffScreen(
            selectedTab = selectedTab,
            onTabSelected = onSelectedTabChange,
            playerUiState = uiState,
            coachUiState = coachUiState,
            physicalTrainerUiState = physicalTrainerUiState,
            onLoadPlayers = viewModel::loadPlayers,
            onLoadCoaches = coachViewModel::loadCoaches,
            onLoadTrainers = physicalTrainerViewModel::loadPhysicalTrainers,
            onAddPlayer = { navigator.navigateTo(Screen.Player.Create) },
            onEditPlayer = { navigator.navigateTo(Screen.Player.Edit(it)) },
            onDeletePlayer = viewModel::deletePlayer,
            onViewPlayer = { navigator.navigateTo(Screen.Player.Detail(it)) },
            onAddCoach = { navigator.navigateTo(Screen.Coach.Create) },
            onEditCoach = { navigator.navigateTo(Screen.Coach.Edit(it)) },
            onDeleteCoach = coachViewModel::deleteCoach,
            onViewCoach = { navigator.navigateTo(Screen.Coach.Detail(it)) },
            onAddTrainer = { navigator.navigateTo(Screen.PhysicalTrainer.Create) },
            onEditTrainer = { navigator.navigateTo(Screen.PhysicalTrainer.Edit(it)) },
            onDeleteTrainer = physicalTrainerViewModel::deletePhysicalTrainer,
            onViewTrainer = { navigator.navigateTo(Screen.PhysicalTrainer.Detail(it)) }
        )
    }
}