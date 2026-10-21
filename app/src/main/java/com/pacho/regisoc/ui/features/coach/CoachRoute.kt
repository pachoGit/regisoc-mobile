package com.pacho.regisoc.ui.features.coach

import androidx.compose.runtime.Composable
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun CoachRoute(
    screen: Screen.Coach,
    viewModel: CoachViewModel,
    navigator: AppNavigator
) {
    when (screen) {
        is Screen.Coach.Create -> {
            CoachCreateScreen(
                onSave = { formState ->
                    viewModel.saveCoach(formState)
                    navigator.navigateTo(Screen.Staff.Overview)
                },
                onCancel = { navigator.navigateTo(Screen.Staff.Overview) },
                onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                    viewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                }
            )
        }
        is Screen.Coach.Edit -> {
            CoachEditScreen(
                coach = screen.coach,
                onSave = { formState ->
                    viewModel.saveCoach(formState)
                    navigator.navigateTo(Screen.Staff.Overview)
                },
                onCancel = { navigator.navigateTo(Screen.Staff.Overview) },
                onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                    viewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                }
            )
        }
        is Screen.Coach.Detail -> {
            CoachDetailScreen(
                coach = screen.coach,
                onBack = { navigator.navigateTo(Screen.Staff.Overview) }
            )
        }
    }
}