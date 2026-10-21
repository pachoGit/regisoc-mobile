package com.pacho.regisoc.ui.features.club

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.regisoc.domain.validation.ClubValidator
import com.pacho.regisoc.ui.components.FormScaffold
import com.pacho.regisoc.ui.components.PhotoPickerState
import com.pacho.regisoc.ui.components.rememberImagePickerLauncher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubCreateScreen(
    onSave: (ClubFormState) -> Unit,
    onCancel: () -> Unit,
    onUploadPhoto: (ByteArray, String, (PhotoPickerState) -> Unit) -> Unit = { _, _, _ -> }
) {
    var formState by remember { mutableStateOf(ClubFormState()) }

    val imagePickerLauncher = rememberImagePickerLauncher { result ->
        if (result.isSuccess) {
            onUploadPhoto(result.bytes!!, result.fileName!!) { newState ->
                formState = formState.copy(crestPhotoState = newState)
            }
        } else if (result.error != null) {
            formState = formState.copy(crestPhotoState = PhotoPickerState(error = result.error))
        }
    }

    FormScaffold(
        title = "Registrar Club",
        onCancel = onCancel,
        onSave = {
            val validation = ClubValidator.validate(formState.name)
            if (validation.isValid) onSave(formState)
            else formState = formState.copy(errors = validation.errors)
        }
    ) {
        ClubFormBody(
            formState = formState,
            onValueChange = { formState = it },
            onPickCrestImage = { imagePickerLauncher() },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
private fun ClubCreateScreenPreview() {
    MaterialTheme { ClubCreateScreen(onSave = {}, onCancel = {}) }
}
