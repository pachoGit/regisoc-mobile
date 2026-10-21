package com.pacho.regisoc.ui.features.player

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.domain.validation.PlayerValidator
import com.pacho.regisoc.ui.common.PhotoType
import com.pacho.regisoc.ui.common.handleImagePickerResult
import com.pacho.regisoc.ui.common.toDateString
import com.pacho.regisoc.ui.common.updateFormPhotoState
import com.pacho.regisoc.ui.components.FormScaffold
import com.pacho.regisoc.ui.components.PhotoPickerState
import com.pacho.regisoc.ui.components.rememberImagePickerLauncher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerEditScreen(
    player: PlayerResponse,
    onSave: (PlayerFormState) -> Unit,
    onCancel: () -> Unit,
    onUploadPhoto: (ByteArray, String, PhotoType, (PhotoPickerState) -> Unit) -> Unit = { _, _, _, _ -> }
) {
    var formState by remember { mutableStateOf(PlayerFormState.fromPlayer(player)) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = formState.dateOfBirth.takeIf { it.isNotBlank() }?.let { null }
    )
    var activePhotoType by remember { mutableStateOf<PhotoType?>(null) }

    val imagePickerLauncher = rememberImagePickerLauncher { result ->
        val type = activePhotoType ?: return@rememberImagePickerLauncher
        activePhotoType = null
        handleImagePickerResult(result, type, onUploadPhoto) { newState ->
            formState = updateFormPhotoState(formState, type, newState) { photo, front, back ->
                formState.copy(photoState = photo, dniFrontPhotoState = front, dniBackPhotoState = back)
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    formState = formState.copy(dateOfBirth = datePickerState.selectedDateMillis?.toDateString() ?: "")
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    FormScaffold(
        title = "Editar Jugador",
        onCancel = onCancel,
        onSave = {
            val validation = PlayerValidator.validate(
                formState.firstName, formState.lastName, formState.documentNumber, formState.age, formState.dateOfBirth
            )
            if (validation.isValid) onSave(formState)
            else formState = formState.copy(errors = validation.errors)
        }
    ) {
        PlayerFormBody(
            formState = formState,
            onValueChange = { formState = it },
            onPickImage = {
                activePhotoType = it
                imagePickerLauncher()
            },
            onShowDatePicker = { showDatePicker = true },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
private fun PlayerEditScreenPreview() {
    MaterialTheme {
        PlayerEditScreen(
            player = PlayerResponse(id = 1, clubId = 1, firstName = "Juan", lastName = "Pérez", documentNumber = "12345678", age = 25, dateOfBirth = "2000-01-15"),
            onSave = {},
            onCancel = {}
        )
    }
}
