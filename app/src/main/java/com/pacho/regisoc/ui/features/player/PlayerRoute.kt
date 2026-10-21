package com.pacho.regisoc.ui.features.player

import androidx.compose.runtime.Composable
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun PlayerRoute(
    screen: Screen.Player,
    viewModel: PlayerViewModel,
    navigator: AppNavigator
) {
    when (screen) {
        is Screen.Player.Create -> {
            PlayerCreateScreen(
                onSave = { formState ->
                    viewModel.savePlayer(formState)
                    navigator.navigateTo(Screen.Staff.Overview)
                },
                onCancel = { navigator.navigateTo(Screen.Staff.Overview) },
                onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                    viewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                }
            )
        }
        is Screen.Player.Edit -> {
            PlayerEditScreen(
                player = screen.player,
                onSave = { formState ->
                    viewModel.savePlayer(formState)
                    navigator.navigateTo(Screen.Staff.Overview)
                },
                onCancel = { navigator.navigateTo(Screen.Staff.Overview) },
                onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                    viewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                }
            )
        }
        is Screen.Player.Detail -> {
            PlayerDetailScreen(
                player = screen.player,
                onBack = { navigator.navigateTo(Screen.Staff.Overview) }
            )
        }
    }
}