package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
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
import com.pacho.regisoc.domain.policy.MatchDateActionPolicy
import com.pacho.regisoc.domain.policy.MatchDateActions
import com.pacho.regisoc.domain.policy.myClub
import com.pacho.regisoc.domain.policy.rivalClub
import com.pacho.regisoc.ui.components.InfoRow
import com.pacho.regisoc.ui.components.InfoSection
import com.pacho.regisoc.ui.layouts.MainLayout

@Composable
fun MatchDateDetailScreen(
    matchDate: MatchDateResponse,
    myClubId: Long,
    onBack: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onRegisterLineup: () -> Unit = {},
    onViewMyLineup: () -> Unit = {},
    onViewRivalLineup: () -> Unit = {}
) {
    val actions = MatchDateActionPolicy.actionsFor(matchDate, myClubId)
    val myClub = matchDate.myClub(myClubId)
    val rival = matchDate.rivalClub(myClubId)

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

            MatchupHeader(myClub = myClub, rival = rival)

            Spacer(modifier = Modifier.height(16.dp))

            MatchDateStatusBadge(status = matchDate.status)

            Spacer(modifier = Modifier.height(24.dp))

            LineupActionsSection(
                actions = actions,
                onRegisterLineup = onRegisterLineup,
                onViewMyLineup = onViewMyLineup,
                onViewRivalLineup = onViewRivalLineup
            )

            InfoSection(title = "Información de la Fecha") {
                InfoRow(label = "Estado", value = matchDate.status.displayLabel)
                InfoRow(label = "Fecha", value = matchDate.date)
                InfoRow(label = "Hora", value = matchDate.match?.scheduledTime?.let { formatScheduledTime(it) } ?: "No registrada")
                InfoRow(label = "Ubicación", value = matchDate.location ?: "No registrada")
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "Mi Club") {
                ClubInfoRows(club = myClub)
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "Club Rival") {
                ClubInfoRows(club = rival)
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

/**
 * Acciones disponibles sobre la planilla de esta fecha, de acuerdo al
 * [com.pacho.regisoc.domain.policy.MatchDateActionPolicy].
 */
@Composable
private fun LineupActionsSection(
    actions: MatchDateActions,
    onRegisterLineup: () -> Unit,
    onViewMyLineup: () -> Unit,
    onViewRivalLineup: () -> Unit
) {
    val hasActions = actions.canRegisterLineup ||
        actions.canViewMyLineup ||
        actions.canViewRivalLineup
    if (!hasActions) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (actions.canRegisterLineup) {
            Button(
                onClick = onRegisterLineup,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Groups, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar plantilla")
            }
        }

        if (actions.canViewMyLineup) {
            Button(
                onClick = onViewMyLineup,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Groups, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver mi plantilla")
            }
        }

        if (actions.canViewRivalLineup) {
            OutlinedButton(
                onClick = onViewRivalLineup,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver plantilla rival")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ClubInfoRows(club: ClubResponse?) {
    if (club == null) {
        InfoRow(label = "Nombre", value = "No registrado")
        return
    }
    InfoRow(label = "Nombre", value = club.name)
    InfoRow(label = "Año de fundación", value = club.foundedYear?.toString() ?: "No registrado")
    InfoRow(label = "Estado", value = if (club.isActive) "Activo" else "Inactivo")
    val description = club.description
    if (!description.isNullOrBlank()) {
        InfoRow(label = "Descripción", value = description)
    }
}

@Composable
private fun MatchupHeader(
    myClub: ClubResponse?,
    rival: ClubResponse?
) {
    if (myClub == null && rival == null) {
        NoMatchAssignedCard()
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MatchClubColumn(
            club = myClub,
            label = "Mi club",
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
            club = rival,
            label = "Rival",
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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

private const val PREVIEW_MY_CLUB_ID = 1L

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

private val previewFinishedMatchDate = previewMatchDate.copy(
    status = MatchDateStatus.FINISHED
)

private val previewMatchDateWithoutMatch = MatchDateResponse(
    id = 4,
    date = "2026-09-02",
    status = MatchDateStatus.UPCOMING,
    name = "Fecha 4"
)

@Preview
@Composable
private fun MatchDateDetailScreenUpcomingPreview() {
    MaterialTheme {
        MatchDateDetailScreen(
            matchDate = previewMatchDate,
            myClubId = PREVIEW_MY_CLUB_ID,
            onBack = {},
            onTabSelected = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateDetailScreenFinishedPreview() {
    MaterialTheme {
        MatchDateDetailScreen(
            matchDate = previewFinishedMatchDate,
            myClubId = PREVIEW_MY_CLUB_ID,
            onBack = {},
            onTabSelected = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateDetailScreenWithoutMatchPreview() {
    MaterialTheme {
        MatchDateDetailScreen(
            matchDate = previewMatchDateWithoutMatch,
            myClubId = PREVIEW_MY_CLUB_ID,
            onBack = {},
            onTabSelected = {}
        )
    }
}
