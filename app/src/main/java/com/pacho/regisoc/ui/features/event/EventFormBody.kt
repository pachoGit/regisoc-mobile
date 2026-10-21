package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.EventStatus

@Composable
fun EventFormBody(
    formState: EventFormState,
    onValueChange: (EventFormState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = formState.title,
            onValueChange = {
                onValueChange(formState.copy(title = it, errors = formState.errors - "title"))
            },
            label = { Text("Título del evento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("title"),
            supportingText = formState.errors["title"]?.let {
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
            maxLines = 5
        )

        OutlinedTextField(
            value = formState.location,
            onValueChange = {
                onValueChange(formState.copy(location = it, errors = formState.errors - "location"))
            },
            label = { Text("Ubicación (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Estado del evento",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EventStatus.entries.forEach { status ->
                    val selected = formState.status == status
                    FilterChip(
                        selected = selected,
                        onClick = {
                            onValueChange(formState.copy(status = status))
                        },
                        label = {
                            Text(
                                text = status.displayLabel,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = status.statusIcon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = status.statusColor.copy(alpha = 0.15f),
                            selectedLabelColor = status.statusColor,
                            selectedLeadingIconColor = status.statusColor
                        )
                    )
                }
            }
        }

        OutlinedTextField(
            value = formState.startDate,
            onValueChange = {
                if (it.length <= 10 && it.all { c -> c.isDigit() || c == '-' }) {
                    onValueChange(formState.copy(startDate = it, errors = formState.errors - "startDate"))
                }
            },
            label = { Text("Fecha de inicio (AAAA-MM-DD)") },
            placeholder = { Text("2026-01-01") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = formState.errors.containsKey("startDate"),
            supportingText = formState.errors["startDate"]?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        OutlinedTextField(
            value = formState.endDate,
            onValueChange = {
                if (it.length <= 10 && it.all { c -> c.isDigit() || c == '-' }) {
                    onValueChange(formState.copy(endDate = it, errors = formState.errors - "endDate"))
                }
            },
            label = { Text("Fecha de fin (opcional)") },
            placeholder = { Text("2026-12-31") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
    }
}