package com.pacho.regisoc.ui.features.club

import androidx.compose.runtime.Composable
import com.pacho.regisoc.ui.layouts.MainLayout
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun ClubRoute(
    screen: Screen.Club,
    uiState: ClubUiState,
    viewModel: ClubViewModel,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    when (screen) {
        is Screen.Club.List -> {
            MainLayout(
                title = "Clubs",
                selectedTab = 1,
                onTabSelected = navigator::navigateToTab,
                snackbarHost = snackbarHost
            ) {
                ClubListScreen(
                    uiState = uiState,
                    onAddClub = { navigator.navigateTo(Screen.Club.Create) },
                    onEditClub = { navigator.navigateTo(Screen.Club.Edit(it)) },
                    onDeleteClub = viewModel::deleteClub,
                    onViewClub = { navigator.navigateTo(Screen.Club.Detail(it)) }
                )
            }
        }
        is Screen.Club.Create -> {
            ClubCreateScreen(
                onSave = { formState ->
                    viewModel.createClub(formState)
                    navigator.navigateTo(Screen.Club.List)
                },
                onCancel = { navigator.navigateTo(Screen.Club.List) },
                onUploadPhoto = { bytes, fileName, onStateUpdate ->
                    viewModel.uploadCrestPhoto(bytes, fileName, onStateUpdate)
                }
            )
        }
        is Screen.Club.Edit -> {
            ClubEditScreen(
                club = screen.club,
                onSave = { formState ->
                    viewModel.updateClub(formState)
                    navigator.navigateTo(Screen.Club.List)
                },
                onCancel = { navigator.navigateTo(Screen.Club.List) },
                onUploadPhoto = { bytes, fileName, onStateUpdate ->
                    viewModel.uploadCrestPhoto(bytes, fileName, onStateUpdate)
                }
            )
        }
        is Screen.Club.Detail -> {
            ClubDetailScreen(
                club = screen.club,
                onBack = { navigator.navigateTo(Screen.Club.List) }
            )
        }
    }
}