package com.pacho.regisoc.ui.features.club

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.ui.components.PhotoPickerField

@Composable
fun ClubFormBody(
    formState: ClubFormState,
    onValueChange: (ClubFormState) -> Unit,
    onPickCrestImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = formState.name,
            onValueChange = {
                onValueChange(formState.copy(name = it, errors = formState.errors - "name"))
            },
            label = { Text("Nombre del Club") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("name"),
            supportingText = formState.errors["name"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        PhotoPickerField(
            label = "Escudo del Club",
            state = formState.crestPhotoState,
            onPickImage = onPickCrestImage
        )

        OutlinedTextField(
            value = formState.foundedYear,
            onValueChange = {
                if (it.all { c -> c.isDigit() } && it.length <= 4) {
                    onValueChange(formState.copy(foundedYear = it, errors = formState.errors - "foundedYear"))
                }
            },
            label = { Text("Año de Fundación (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("foundedYear"),
            supportingText = formState.errors["foundedYear"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        OutlinedTextField(
            value = formState.description,
            onValueChange = {
                onValueChange(formState.copy(description = it, errors = formState.errors - "description"))
            },
            label = { Text("Descripción (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            minLines = 3,
            maxLines = 5,
            isError = formState.errors.containsKey("description"),
            supportingText = formState.errors["description"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )
    }
}
