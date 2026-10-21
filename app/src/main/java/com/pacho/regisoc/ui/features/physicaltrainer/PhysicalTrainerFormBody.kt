package com.pacho.regisoc.ui.features.physicaltrainer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.ui.common.PhotoType
import com.pacho.regisoc.ui.components.PhotoPickerField

@Composable
fun PhysicalTrainerFormBody(
    formState: PhysicalTrainerFormState,
    onValueChange: (PhysicalTrainerFormState) -> Unit,
    onPickImage: (PhotoType) -> Unit,
    onShowDatePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Información Obligatoria",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = formState.firstName,
            onValueChange = {
                onValueChange(formState.copy(firstName = it, errors = formState.errors - "firstName"))
            },
            label = { Text("Nombres") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("firstName"),
            supportingText = formState.errors["firstName"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        OutlinedTextField(
            value = formState.lastName,
            onValueChange = {
                onValueChange(formState.copy(lastName = it, errors = formState.errors - "lastName"))
            },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("lastName"),
            supportingText = formState.errors["lastName"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        OutlinedTextField(
            value = formState.documentNumber,
            onValueChange = {
                if (it.all { c -> c.isDigit() } && it.length <= 8) {
                    onValueChange(formState.copy(documentNumber = it, errors = formState.errors - "documentNumber"))
                }
            },
            label = { Text("DNI") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("documentNumber"),
            supportingText = formState.errors["documentNumber"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        OutlinedTextField(
            value = formState.age,
            onValueChange = {
                if (it.all { c -> c.isDigit() } && it.length <= 3) {
                    onValueChange(formState.copy(age = it, errors = formState.errors - "age"))
                }
            },
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("age"),
            supportingText = formState.errors["age"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowDatePicker() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (formState.errors.containsKey("dateOfBirth"))
                    MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Fecha de Nacimiento", style = MaterialTheme.typography.bodyLarge)
                Text(
                    if (formState.dateOfBirth.isBlank()) "Seleccionar"
                    else formState.dateOfBirth,
                    color = if (formState.dateOfBirth.isBlank())
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Fotos del Preparador Físico",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        PhotoPickerField(
            label = "Foto del Preparador",
            state = formState.photoState,
            onPickImage = { onPickImage(PhotoType.TRAINER) }
        )

        PhotoPickerField(
            label = "DNI Frontal",
            state = formState.dniFrontPhotoState,
            onPickImage = { onPickImage(PhotoType.DNI_FRONT) }
        )

        PhotoPickerField(
            label = "DNI Posterior",
            state = formState.dniBackPhotoState,
            onPickImage = { onPickImage(PhotoType.DNI_BACK) }
        )
    }
}
