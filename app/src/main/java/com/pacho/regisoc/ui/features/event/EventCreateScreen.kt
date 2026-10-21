package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.regisoc.domain.validation.EventValidator
import com.pacho.regisoc.ui.layouts.MainLayout

@Composable
fun EventCreateScreen(
    onSave: (EventFormState) -> Unit,
    onBack: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    var formState by remember { mutableStateOf(EventFormState()) }

    MainLayout(
        title = "Registrar Evento",
        onBackClick = onBack,
        selectedTab = 1,
        onTabSelected = onTabSelected,
        actions = {
            TextButton(
                onClick = {
                    val validation = EventValidator.validate(
                        title = formState.title,
                        startDate = formState.startDate
                    )
                    if (validation.isValid) onSave(formState)
                    else formState = formState.copy(errors = validation.errors)
                }
            ) {
                Text("Guardar", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) {
        EventFormBody(
            formState = formState,
            onValueChange = { formState = it },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
private fun EventCreateScreenPreview() {
    MaterialTheme {
        EventCreateScreen(onSave = {}, onBack = {}, onTabSelected = {})
    }
}