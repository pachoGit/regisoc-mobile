package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.dto.EventStatus

@Composable
fun EventListScreen(
    uiState: EventUiState,
    onLoad: () -> Unit,
    onAddEvent: () -> Unit,
    onViewEvent: (EventResponse) -> Unit,
    onViewDates: (EventResponse) -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is EventUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is EventUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is EventUiState.Success -> {
                val events = uiState.events
                if (events.isEmpty()) {
                    EmptyEventsSection()
                } else {
                    EventsContent(
                        events = events,
                        onViewEvent = onViewEvent,
                        onViewDates = onViewDates
                    )
                }
            }
        }
    }
}

@Composable
private fun EventsContent(
    events: List<EventResponse>,
    onViewEvent: (EventResponse) -> Unit,
    onViewDates: (EventResponse) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "summary") {
            EventsSummary(events)
        }

        EventStatus.entries.forEach { status ->
            val group = events.filter { it.status == status }
            if (group.isNotEmpty()) {
                item(key = "header-${status.name}") {
                    EventSectionHeader(status = status, count = group.size)
                }
                items(group, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        onView = { onViewEvent(event) },
                        onViewDates = { onViewDates(event) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventsSummary(events: List<EventResponse>) {
    val ongoing = events.count { it.status == EventStatus.ONGOING }
    val upcoming = events.count { it.status == EventStatus.UPCOMING }
    val finished = events.count { it.status == EventStatus.FINISHED }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryItem(status = EventStatus.ONGOING, count = ongoing, modifier = Modifier.weight(1f))
            SummaryItem(status = EventStatus.UPCOMING, count = upcoming, modifier = Modifier.weight(1f))
            SummaryItem(status = EventStatus.FINISHED, count = finished, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SummaryItem(
    status: EventStatus,
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = status.statusIcon,
            contentDescription = null,
            tint = status.statusColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = status.displayLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EventSectionHeader(
    status: EventStatus,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 12.dp, bottom = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(status.statusColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = status.displayLabel,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(50),
            color = status.statusColor.copy(alpha = 0.12f),
            contentColor = status.statusColor
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun EmptyEventsSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aun no se le ha registrado a un evento",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pronto le registraran a su primer evento",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

private val mockEvents = listOf(
    EventResponse(
        id = 1, name = "Torneo Apertura 2026",
        description = "Torneo de fútbol juvenil entre clubes locales",
        location = "Estadio Central", startDate = "2026-08-01", endDate = "2026-09-15",
        status = EventStatus.ONGOING
    ),
    EventResponse(
        id = 2, name = "Copa Verano",
        description = "Competencia interclubes de la región",
        location = "Complejo Arenales", startDate = "2026-09-20",
        status = EventStatus.UPCOMING
    ),
    EventResponse(
        id = 3, name = "Liga Regional",
        description = "Liga de clubes locales",
        startDate = "2026-05-10", endDate = "2026-07-30",
        status = EventStatus.FINISHED
    )
)

@Preview
@Composable
private fun EventListScreenSuccessPreview() {
    MaterialTheme {
        EventListScreen(
            uiState = EventUiState.Success(mockEvents),
            onLoad = {},
            onAddEvent = {},
            onViewDates = {},
            onViewEvent = {}
        )
    }
}

@Preview
@Composable
private fun EventListScreenEmptyPreview() {
    MaterialTheme {
        EventListScreen(
            uiState = EventUiState.Success(emptyList()),
            onLoad = {},
            onAddEvent = {},
            onViewDates = {},
            onViewEvent = {}
        )
    }
}

@Preview
@Composable
private fun EventListScreenErrorPreview() {
    MaterialTheme {
        EventListScreen(
            uiState = EventUiState.Error("Error al cargar eventos"),
            onLoad = {},
            onAddEvent = {},
            onViewDates = {},
            onViewEvent = {}
        )
    }
}

@Preview
@Composable
private fun EventListScreenLoadingPreview() {
    MaterialTheme {
        EventListScreen(
            uiState = EventUiState.Loading,
            onLoad = {},
            onAddEvent = {},
            onViewDates = {},
            onViewEvent = {}
        )
    }
}