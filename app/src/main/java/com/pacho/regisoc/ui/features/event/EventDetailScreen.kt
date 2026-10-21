package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.dto.EventStatus
import com.pacho.regisoc.ui.components.InfoRow
import com.pacho.regisoc.ui.components.InfoSection
import com.pacho.regisoc.ui.layouts.MainLayout

@Composable
fun EventDetailScreen(
    event: EventResponse,
    onBack: () -> Unit,
    onViewDates: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    MainLayout(
        title = "Detalle del Evento",
        onBackClick = onBack,
        selectedTab = 1,
        onTabSelected = onTabSelected
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(event.status.statusColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = event.status.statusColor,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = event.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            EventStatusBadge(status = event.status)

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(title = "Información del Evento") {
                InfoRow(label = "Estado", value = event.status.displayLabel)
                InfoRow(label = "Fecha de inicio", value = event.startDate)
                InfoRow(label = "Fecha de fin", value = event.endDate ?: "No registrado")
                InfoRow(label = "Ubicación", value = event.location ?: "No registrada")
                // InfoRow(label = "Club", value = event.clubId.toString())
            }

            if (!event.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                InfoSection(title = "Descripción") {
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            InfoSection(title = "Información del Sistema") {
                InfoRow(label = "Creado", value = event.createdAt ?: "-")
                InfoRow(label = "Actualizado", value = event.updatedAt ?: "-")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onViewDates,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ver fechas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private val previewEvent = EventResponse(
    id = 1,
    // clubId = 1,
    name = "Torneo Apertura 2026",
    description = "Torneo de fútbol juvenil entre clubes de la región",
    location = "Estadio Central",
    startDate = "2026-08-01",
    endDate = "2026-09-15",
    status = EventStatus.ONGOING,
    createdAt = "2024-01-15T10:30:00",
    updatedAt = "2026-06-20T14:45:00"
)

@Preview
@Composable
private fun EventDetailScreenPreview() {
    MaterialTheme {
        EventDetailScreen(
            event = previewEvent,
            onBack = {},
            onViewDates = {},
            onTabSelected = {}
        )
    }
}