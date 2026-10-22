package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.dto.EventStatus
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus
import com.pacho.regisoc.data.dto.MatchResponse

@Composable
fun MatchDateListScreen(
    event: EventResponse,
    uiState: MatchDateUiState,
    myClubId: Long,
    onLoad: () -> Unit,
    onViewDate: (MatchDateResponse) -> Unit,
    onRegisterLineup: (MatchDateResponse) -> Unit,
    onViewMyLineup: (MatchDateResponse) -> Unit,
    onViewRivalLineup: (MatchDateResponse) -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (uiState) {
                is MatchDateUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MatchDateUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is MatchDateUiState.Success -> {
                    val matchDates = uiState.matchDates
                    if (matchDates.isEmpty()) {
                        EmptyMatchDatesSection()
                    } else {
                        MatchDatesContent(
                            event = event,
                            matchDates = matchDates,
                            myClubId = myClubId,
                            onViewDate = onViewDate,
                            onRegisterLineup = onRegisterLineup,
                            onViewMyLineup = onViewMyLineup,
                            onViewRivalLineup = onViewRivalLineup
                        )
                    }
                }
            }
        }
}

@Composable
private fun MatchDatesContent(
    event: EventResponse,
    matchDates: List<MatchDateResponse>,
    myClubId: Long,
    onViewDate: (MatchDateResponse) -> Unit,
    onRegisterLineup: (MatchDateResponse) -> Unit,
    onViewMyLineup: (MatchDateResponse) -> Unit,
    onViewRivalLineup: (MatchDateResponse) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "event-header") {
            MatchDatesHeader(event)
        }

        item(key = "summary") {
            MatchDatesSummary(matchDates)
        }

        MatchDateStatus.entries.forEach { status ->
            val group = matchDates.filter { it.status == status }
            if (group.isNotEmpty()) {
                item(key = "header-${status.name}") {
                    MatchDateSectionHeader(status = status, count = group.size)
                }
                items(group, key = { it.id }) { matchDate ->
                    MatchDateCard(
                        matchDate = matchDate,
                        myClubId = myClubId,
                        onViewDate = { onViewDate(matchDate) },
                        onRegisterLineup = { onRegisterLineup(matchDate) },
                        onViewMyLineup = { onViewMyLineup(matchDate) },
                        onViewRivalLineup = { onViewRivalLineup(matchDate) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchDatesHeader(event: EventResponse) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = event.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fechas programadas para este evento",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MatchDatesSummary(matchDates: List<MatchDateResponse>) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MatchDateStatus.entries.forEach { status ->
                val count = matchDates.count { it.status == status }
                MatchDateSummaryItem(
                    status = status,
                    count = count,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MatchDateSummaryItem(
    status: MatchDateStatus,
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
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = status.displayLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
private fun MatchDateSectionHeader(
    status: MatchDateStatus,
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
private fun EmptyMatchDatesSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Este evento no tiene fechas",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Las fechas del evento aparecerán aquí",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

private val previewEvent = EventResponse(
    id = 1,
    name = "Torneo Apertura 2026",
    description = "Torneo de fútbol juvenil entre clubes de la región",
    location = "Estadio Central",
    startDate = "2026-08-01",
    endDate = "2026-09-15",
    status = EventStatus.ONGOING
)

private val previewHomeClub = ClubResponse(
    id = 1,
    name = "Club Deportivo Estrella",
    foundedYear = 1950,
    createdBy = "Admin",
    isActive = true
)

private val previewAwayClub = ClubResponse(
    id = 2,
    name = "Club Atlético Rival",
    foundedYear = 1975,
    createdBy = "Admin",
    isActive = true
)

private val previewMatch = MatchResponse(
    id = 1,
    homeClub = previewHomeClub,
    awayClub = previewAwayClub,
    scheduledTime = "2026-03-16T16:00:00"
)

private val previewMatchDates = listOf(
    MatchDateResponse(
        id = 1, date = "2026-08-05", location = "Estadio Central",
        status = MatchDateStatus.UPCOMING,
        match = previewMatch, name = "Fecha 1"
    ),
    MatchDateResponse(
        id = 2, date = "2026-08-19",
        status = MatchDateStatus.ONGOING,
        match = previewMatch, name = "Fecha 2"
    ),
    MatchDateResponse(
        id = 3, date = "2026-08-26", location = "Polideportivo Sur",
        status = MatchDateStatus.FINISHED,
        match = previewMatch, name = "Fecha 3"
    ),
    MatchDateResponse(
        id = 4, date = "2026-09-02",
        status = MatchDateStatus.CANCELED,
        name = "Fecha 4"
    )
)

private const val PREVIEW_MY_CLUB_ID = 1L

@Preview
@Composable
private fun MatchDateListScreenSuccessPreview() {
    MaterialTheme {
        MatchDateListScreen(
            event = previewEvent,
            uiState = MatchDateUiState.Success(previewMatchDates),
            myClubId = PREVIEW_MY_CLUB_ID,
            onLoad = {},
            onViewDate = {},
            onRegisterLineup = {},
            onViewMyLineup = {},
            onViewRivalLineup = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateListScreenEmptyPreview() {
    MaterialTheme {
        MatchDateListScreen(
            event = previewEvent,
            uiState = MatchDateUiState.Success(emptyList()),
            myClubId = PREVIEW_MY_CLUB_ID,
            onLoad = {},
            onViewDate = {},
            onRegisterLineup = {},
            onViewMyLineup = {},
            onViewRivalLineup = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateListScreenErrorPreview() {
    MaterialTheme {
        MatchDateListScreen(
            event = previewEvent,
            uiState = MatchDateUiState.Error("Error al cargar fechas"),
            myClubId = PREVIEW_MY_CLUB_ID,
            onLoad = {},
            onViewDate = {},
            onRegisterLineup = {},
            onViewMyLineup = {},
            onViewRivalLineup = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateListScreenLoadingPreview() {
    MaterialTheme {
        MatchDateListScreen(
            event = previewEvent,
            uiState = MatchDateUiState.Loading,
            myClubId = PREVIEW_MY_CLUB_ID,
            onLoad = {},
            onViewDate = {},
            onRegisterLineup = {},
            onViewMyLineup = {},
            onViewRivalLineup = {}
        )
    }
}
