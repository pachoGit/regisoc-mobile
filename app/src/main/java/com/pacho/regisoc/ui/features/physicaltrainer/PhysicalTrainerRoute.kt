package com.pacho.regisoc.ui.features.physicaltrainer

import androidx.compose.runtime.Composable
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun PhysicalTrainerRoute(
    screen: Screen.PhysicalTrainer,
    viewModel: PhysicalTrainerViewModel,
    navigator: AppNavigator
) {
    when (screen) {
        is Screen.PhysicalTrainer.Create -> {
            PhysicalTrainerCreateScreen(
                onSave = { formState ->
                    viewModel.savePhysicalTrainer(formState)
                    navigator.navigateTo(Screen.Staff.Overview)
                },
                onCancel = { navigator.navigateTo(Screen.Staff.Overview) },
                onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                    viewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                }
            )
        }
        is Screen.PhysicalTrainer.Edit -> {
            PhysicalTrainerEditScreen(
                entity = screen.trainer,
                onSave = { formState ->
                    viewModel.savePhysicalTrainer(formState)
                    navigator.navigateTo(Screen.Staff.Overview)
                },
                onCancel = { navigator.navigateTo(Screen.Staff.Overview) },
                onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                    viewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                }
            )
        }
        is Screen.PhysicalTrainer.Detail -> {
            PhysicalTrainerDetailScreen(
                entity = screen.trainer,
                onBack = { navigator.navigateTo(Screen.Staff.Overview) }
            )
        }
    }
}