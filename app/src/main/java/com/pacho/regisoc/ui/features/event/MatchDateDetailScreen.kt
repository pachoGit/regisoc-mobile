package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus
import com.pacho.regisoc.data.dto.MatchResponse
import com.pacho.regisoc.ui.components.InfoRow
import com.pacho.regisoc.ui.components.InfoSection
import com.pacho.regisoc.ui.layouts.MainLayout

@Composable
fun MatchDateDetailScreen(
    matchDate: MatchDateResponse,
    onBack: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onOpenLineup: () -> Unit = {}
) {
    MainLayout(
        title = "Detalle de la Fecha",
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

            MatchupHeader(matchDate = matchDate)

            Spacer(modifier = Modifier.height(16.dp))

            MatchDateStatusBadge(status = matchDate.status)

            Spacer(modifier = Modifier.height(24.dp))

            if (matchDate.match != null) {
                Button(
                    onClick = onOpenLineup,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver o generar alineación")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            InfoSection(title = "Información de la Fecha") {
                InfoRow(label = "Estado", value = matchDate.status.displayLabel)
                InfoRow(label = "Fecha", value = matchDate.date)
                InfoRow(label = "Hora", value = matchDate.match?.scheduledTime?.let { formatScheduledTime(it) } ?: "No registrada")
                InfoRow(label = "Ubicación", value = matchDate.location ?: "No registrada")
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "Mi Club") {
                val club = matchDate.homeClub
                if (club != null) {
                    InfoRow(label = "Nombre", value = club.name)
                    InfoRow(label = "Año de fundación", value = club.foundedYear?.toString() ?: "No registrado")
                    InfoRow(label = "Estado", value = if (club.isActive) "Activo" else "Inactivo")
                    val description = club.description
                    if (!description.isNullOrBlank()) {
                        InfoRow(label = "Descripción", value = description)
                    }
                } else {
                    InfoRow(label = "Nombre", value = "No registrado")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "Club Rival") {
                val club = matchDate.awayClub
                if (club != null) {
                    InfoRow(label = "Nombre", value = club.name)
                    InfoRow(label = "Año de fundación", value = club.foundedYear?.toString() ?: "No registrado")
                    InfoRow(label = "Estado", value = if (club.isActive) "Activo" else "Inactivo")
                    val description = club.description
                    if (!description.isNullOrBlank()) {
                        InfoRow(label = "Descripción", value = description)
                    }
                } else {
                    InfoRow(label = "Nombre", value = "No registrado")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "Información del Sistema") {
                InfoRow(label = "Creado", value = matchDate.createdAt ?: "-")
                InfoRow(label = "Actualizado", value = matchDate.updatedAt ?: "-")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MatchupHeader(
    matchDate: MatchDateResponse
) {
    val match = matchDate.match
    if (match == null) {
        NoMatchAssignedCard()
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MatchClubColumn(
            club = match.homeClub,
            label = "Local",
            modifier = Modifier.weight(1f)
        )

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            Text(
                text = "VS",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        MatchClubColumn(
            club = match.awayClub,
            label = "Visitante",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MatchClubColumn(
    club: ClubResponse?,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = club?.name ?: "Sin asignar",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NoMatchAssignedCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Aún no tiene rival o partido asignado",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatScheduledTime(value: String): String {
    val time = value.substringAfter("T", missingDelimiterValue = value)
    return if (time.length >= 5) time.take(5) else time
}

private val previewHomeClub = ClubResponse(
    id = 1,
    name = "Club Deportivo Estrella",
    foundedYear = 1950,
    description = "Club histórico de la ciudad fundado en 1950",
    createdBy = "Admin",
    isActive = true,
    createdAt = "2024-01-15T10:30:00",
    updatedAt = "2026-06-20T14:45:00"
)

private val previewAwayClub = ClubResponse(
    id = 2,
    name = "Club Atlético Rival",
    foundedYear = 1975,
    description = "Club de la zona norte de la ciudad",
    createdBy = "Admin",
    isActive = true,
    createdAt = "2024-02-10T09:00:00",
    updatedAt = "2026-06-25T11:20:00"
)

private val previewMatchDate = MatchDateResponse(
    id = 1,
    date = "2026-08-05",
    location = "Estadio Central",
    status = MatchDateStatus.UPCOMING,
    match = MatchResponse(
        id = 1,
        homeClub = previewHomeClub,
        awayClub = previewAwayClub,
        scheduledTime = "2026-03-16T16:00:00"
    ),
    createdAt = "2026-07-20T10:30:00",
    updatedAt = "2026-07-25T14:45:00"
)

@Preview
@Composable
private fun MatchDateDetailScreenPreview() {
    MaterialTheme {
        MatchDateDetailScreen(
            matchDate = previewMatchDate,
            onBack = {},
            onTabSelected = {}
        )
    }
}
