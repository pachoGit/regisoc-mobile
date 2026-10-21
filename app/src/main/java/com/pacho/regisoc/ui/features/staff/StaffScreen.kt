package com.pacho.regisoc.ui.features.staff

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.ui.features.coach.CoachListScreen
import com.pacho.regisoc.ui.features.coach.CoachUiState
import com.pacho.regisoc.ui.features.physicaltrainer.PhysicalTrainerListScreen
import com.pacho.regisoc.ui.features.physicaltrainer.PhysicalTrainerUiState
import com.pacho.regisoc.ui.features.player.PlayerListScreen
import com.pacho.regisoc.ui.features.player.PlayerUiState

@Composable
fun StaffScreen(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    playerUiState: PlayerUiState,
    coachUiState: CoachUiState,
    physicalTrainerUiState: PhysicalTrainerUiState,
    onLoadPlayers: () -> Unit,
    onLoadCoaches: () -> Unit,
    onLoadTrainers: () -> Unit,
    onAddPlayer: () -> Unit,
    onEditPlayer: (PlayerResponse) -> Unit,
    onDeletePlayer: (Long) -> Unit,
    onViewPlayer: (PlayerResponse) -> Unit,
    onAddCoach: () -> Unit,
    onEditCoach: (CoachResponse) -> Unit,
    onDeleteCoach: (Long) -> Unit,
    onViewCoach: (CoachResponse) -> Unit,
    onAddTrainer: () -> Unit,
    onEditTrainer: (PhysicalTrainerResponse) -> Unit,
    onDeleteTrainer: (Long) -> Unit,
    onViewTrainer: (PhysicalTrainerResponse) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Jugadores"
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Entrenadores"
                    )
                }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Preparadores Físicos"
                    )
                }
            )
        }

        when (selectedTab) {
            0 -> {
                PlayerListScreen(
                    uiState = playerUiState,
                    onLoad = onLoadPlayers,
                    onAddPlayer = onAddPlayer,
                    onEditPlayer = onEditPlayer,
                    onDeletePlayer = onDeletePlayer,
                    onViewPlayer = onViewPlayer
                )
            }
            1 -> {
                CoachListScreen(
                    uiState = coachUiState,
                    onLoad = onLoadCoaches,
                    onAddCoach = onAddCoach,
                    onEditCoach = onEditCoach,
                    onDeleteCoach = onDeleteCoach,
                    onViewCoach = onViewCoach
                )
            }
            2 -> {
                PhysicalTrainerListScreen(
                    uiState = physicalTrainerUiState,
                    onLoad = onLoadTrainers,
                    onAdd = onAddTrainer,
                    onEdit = onEditTrainer,
                    onDelete = onDeleteTrainer,
                    onView = onViewTrainer
                )
            }
        }
    }
}
