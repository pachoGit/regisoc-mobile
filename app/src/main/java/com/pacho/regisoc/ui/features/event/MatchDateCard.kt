package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus
import com.pacho.regisoc.data.dto.MatchResponse
import com.pacho.regisoc.domain.policy.MatchDateActionPolicy
import com.pacho.regisoc.domain.policy.MatchDateActions
import com.pacho.regisoc.domain.policy.myClub
import com.pacho.regisoc.domain.policy.rivalClub

/**
 * Card de una fecha del evento.
 *
 * El menú de acciones se genera a partir de [MatchDateActionPolicy]: sin rival
 * solo se puede ver el detalle; con rival, las opciones dependen del estado.
 */
@Composable
fun MatchDateCard(
    matchDate: MatchDateResponse,
    myClubId: Long,
    onViewDate: () -> Unit,
    onRegisterLineup: () -> Unit,
    onViewMyLineup: () -> Unit,
    onViewRivalLineup: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    val actions = remember(matchDate, myClubId) {
        MatchDateActionPolicy.actionsFor(matchDate, myClubId)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDate() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(matchDate.status.statusColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = matchDate.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                MatchDateStatusBadge(status = matchDate.status)

                Spacer(modifier = Modifier.height(12.dp))

                val myClub = matchDate.myClub(myClubId)
                val rival = matchDate.rivalClub(myClubId)
                if (rival != null) {
                    MatchTeamsRow(myClub = myClub, rival = rival)
                } else {
                    NoMatchAssignedRow()
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MatchDateMeta(
                        icon = Icons.Default.DateRange,
                        text = matchDate.date
                    )
                    val scheduledTime = matchDate.match?.scheduledTime
                        ?.let { formatScheduledTime(it) }
                    if (!scheduledTime.isNullOrBlank()) {
                        Spacer(modifier = Modifier.width(16.dp))
                        MatchDateMeta(
                            icon = Icons.Default.Schedule,
                            text = scheduledTime
                        )
                    }
                    if (!matchDate.location.isNullOrBlank()) {
                        Spacer(modifier = Modifier.width(16.dp))
                        MatchDateMeta(
                            icon = Icons.Default.LocationOn,
                            text = matchDate.location
                        )
                    }
                }
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }
                MatchDateActionsMenu(
                    expanded = showMenu,
                    actions = actions,
                    onDismiss = { showMenu = false },
                    onViewDate = onViewDate,
                    onRegisterLineup = onRegisterLineup,
                    onViewMyLineup = onViewMyLineup,
                    onViewRivalLineup = onViewRivalLineup
                )
            }
        }
    }
}

@Composable
private fun MatchDateActionsMenu(
    expanded: Boolean,
    actions: MatchDateActions,
    onDismiss: () -> Unit,
    onViewDate: () -> Unit,
    onRegisterLineup: () -> Unit,
    onViewMyLineup: () -> Unit,
    onViewRivalLineup: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 8.dp,
        tonalElevation = 0.dp,
        offset = DpOffset(8.dp, 8.dp)
    ) {
        if (actions.canViewDate) {
            MenuActionItem(
                label = "Ver detalle de la fecha",
                icon = Icons.Default.Visibility,
                onClick = {
                    onDismiss()
                    onViewDate()
                }
            )
        }

        if (actions.canRegisterLineup) {
            MenuActionItem(
                label = "Registrar plantilla",
                icon = Icons.Default.Groups,
                onClick = {
                    onDismiss()
                    onRegisterLineup()
                }
            )
        }

        if (actions.canViewMyLineup) {
            MenuActionItem(
                label = "Ver mi plantilla",
                icon = Icons.Default.Groups,
                onClick = {
                    onDismiss()
                    onViewMyLineup()
                }
            )
        }

        if (actions.canViewRivalLineup) {
            MenuActionItem(
                label = "Ver plantilla rival",
                icon = Icons.Default.Groups,
                onClick = {
                    onDismiss()
                    onViewRivalLineup()
                }
            )
        }
    }
}

@Composable
private fun MenuActionItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        onClick = onClick
    )
}

@Composable
private fun MatchTeamsRow(
    myClub: ClubResponse?,
    rival: ClubResponse?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MatchTeamColumn(
            label = "Mi club",
            club = myClub,
            modifier = Modifier.weight(1f)
        )

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            Text(
                text = "VS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        MatchTeamColumn(
            label = "Rival",
            club = rival,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MatchTeamColumn(
    label: String,
    club: ClubResponse?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = club?.name ?: "Sin asignar",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NoMatchAssignedRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SportsSoccer,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Aún no tiene rival o partido asignado",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MatchDateMeta(
    icon: ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatScheduledTime(value: String): String {
    val time = value.substringAfter("T", missingDelimiterValue = value)
    return if (time.length >= 5) time.take(5) else time
}

private const val MY_CLUB_ID = 1L

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

private val previewMatchDate = MatchDateResponse(
    id = 1,
    date = "2026-08-05",
    location = "Estadio Central",
    status = MatchDateStatus.UPCOMING,
    name = "Fecha 10",
    match = previewMatch
)

private val previewFinishedMatchDate = previewMatchDate.copy(
    id = 2,
    status = MatchDateStatus.FINISHED,
    name = "Fecha 9"
)

private val previewMatchDateWithoutMatch = MatchDateResponse(
    id = 3,
    date = "2026-08-19",
    status = MatchDateStatus.UPCOMING,
    name = "Fecha 11"
)

@Preview
@Composable
private fun MatchDateCardUpcomingPreview() {
    MaterialTheme {
        MatchDateCard(
            matchDate = previewMatchDate,
            myClubId = MY_CLUB_ID,
            onViewDate = {},
            onRegisterLineup = {},
            onViewMyLineup = {},
            onViewRivalLineup = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateCardFinishedPreview() {
    MaterialTheme {
        MatchDateCard(
            matchDate = previewFinishedMatchDate,
            myClubId = MY_CLUB_ID,
            onViewDate = {},
            onRegisterLineup = {},
            onViewMyLineup = {},
            onViewRivalLineup = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateCardWithoutMatchPreview() {
    MaterialTheme {
        MatchDateCard(
            matchDate = previewMatchDateWithoutMatch,
            myClubId = MY_CLUB_ID,
            onViewDate = {},
            onRegisterLineup = {},
            onViewMyLineup = {},
            onViewRivalLineup = {}
        )
    }
}
