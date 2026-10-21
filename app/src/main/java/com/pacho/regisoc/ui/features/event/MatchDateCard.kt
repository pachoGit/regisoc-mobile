package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
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
import com.pacho.regisoc.data.dto.MatchStatus

@Composable
fun MatchDateCard(
    matchDate: MatchDateResponse,
    onView: () -> Unit,
    onRegisterLineup: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onView() },
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
                    text = "${matchDate.name}",
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

                val match = matchDate.match
                if (match != null) {
                    MatchTeamsRow(match = match)
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
                    val scheduledTime = match?.scheduledTime?.let { formatScheduledTime(it) }
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
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shadowElevation = 8.dp,
                    tonalElevation = 0.dp,
                    offset = DpOffset(8.dp, 8.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Ver detalle") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            showMenu = false
                            onView()
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    if (matchDate.match != null) {
                        DropdownMenuItem(
                            text = { Text("Registrar plantilla") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            onClick = {
                                showMenu = false
                                onRegisterLineup()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchTeamsRow(
    match: MatchResponse
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MatchTeamColumn(
            label = "Local",
            club = match.homeClub,
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
            label = "Visitante",
            club = match.awayClub,
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
    scheduledTime = "2026-03-16T16:00:00",
    status = MatchStatus.UPCOMING
)

private val previewMatchDate = MatchDateResponse(
    id = 1,
    date = "2026-08-05",
    location = "Estadio Central",
    status = MatchDateStatus.ONGOING,
    name = "Fecha 10",
    match = previewMatch
)

private val previewMatchDateWithoutMatch = MatchDateResponse(
    id = 2,
    date = "2026-08-19",
    status = MatchDateStatus.UPCOMING,
    name = "Fecha 11"
)

@Preview
@Composable
private fun MatchDateCardPreview() {
    MaterialTheme {
        MatchDateCard(
            matchDate = previewMatchDate,
            onView = {},
            onRegisterLineup = {}
        )
    }
}

@Preview
@Composable
private fun MatchDateCardWithoutMatchPreview() {
    MaterialTheme {
        MatchDateCard(
            matchDate = previewMatchDateWithoutMatch,
            onView = {},
            onRegisterLineup = {}
        )
    }
}