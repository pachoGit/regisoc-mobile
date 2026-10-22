package com.pacho.regisoc.ui.features.lineup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.LineupCoachResponse
import com.pacho.regisoc.data.dto.LineupPhysicalTrainerResponse
import com.pacho.regisoc.data.dto.LineupPlayerResponse
import com.pacho.regisoc.data.dto.LineupResponse
import com.pacho.regisoc.data.dto.LineupStatus
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus
import com.pacho.regisoc.data.dto.PlayerPosition
import com.pacho.regisoc.ui.components.DniPhotoPlaceholder
import com.pacho.regisoc.ui.components.InfoRow
import com.pacho.regisoc.ui.layouts.MainLayout
import kotlinx.datetime.LocalDateTime

/**
 * Pantalla de solo lectura de una planilla ya registrada.
 *
 * [title] distingue "Mi plantilla" de "Plantilla rival"; el resto del
 * contenido es el mismo para ambos casos.
 */
@Composable
fun LineupDetailScreen(
    title: String,
    matchDate: MatchDateResponse,
    clubName: String,
    uiState: LineupDetailUiState,
    onBack: () -> Unit,
    onTabSelected: (Int) -> Unit,
    snackbarHost: @Composable () -> Unit = {}
) {
    MainLayout(
        title = title,
        onBackClick = onBack,
        selectedTab = 1,
        onTabSelected = onTabSelected,
        snackbarHost = snackbarHost
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is LineupDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LineupDetailUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }
                is LineupDetailUiState.Success -> {
                    val lineup = uiState.lineup
                    if (lineup == null) {
                        EmptyLineupSection(clubName = clubName)
                    } else {
                        LineupDetailContent(
                            matchDate = matchDate,
                            clubName = clubName,
                            lineup = lineup
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LineupDetailContent(
    matchDate: MatchDateResponse,
    clubName: String,
    lineup: LineupResponse
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "header") {
            LineupDetailHeader(
                matchDate = matchDate,
                clubName = clubName,
                lineup = lineup
            )
        }

        item(key = "staff-title") {
            LineupDetailSectionTitle(text = "Cuerpo técnico")
        }

        lineup.coach?.let { coach ->
            item(key = "coach-${coach.coachId}") {
                LineupMemberCard(
                    icon = Icons.Default.Face,
                    role = "Entrenador",
                    fullName = "${coach.firstName} ${coach.lastName}",
                    documentNumber = coach.documentNumber,
                    age = coach.age,
                    positionLabel = null,
                    documentFrontUrl = coach.documentFrontUrl,
                    documentBackUrl = coach.documentBackUrl
                )
            }
        }

        lineup.physicalTrainer?.let { trainer ->
            item(key = "trainer-${trainer.physicalTrainerId}") {
                LineupMemberCard(
                    icon = Icons.Default.Favorite,
                    role = "Preparador físico",
                    fullName = "${trainer.firstName} ${trainer.lastName}",
                    documentNumber = trainer.documentNumber,
                    age = trainer.age,
                    positionLabel = null,
                    documentFrontUrl = trainer.documentFrontUrl,
                    documentBackUrl = trainer.documentBackUrl
                )
            }
        }

        if (lineup.coach == null && lineup.physicalTrainer == null) {
            item(key = "staff-empty") {
                Text(
                    text = "Sin cuerpo técnico registrado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        item(key = "players-title") {
            LineupDetailSectionTitle(text = "Jugadores (${lineup.players.size})")
        }

        if (lineup.players.isEmpty()) {
            item(key = "players-empty") {
                Text(
                    text = "Sin jugadores registrados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        } else {
            items(lineup.players, key = { it.playerId }) { player ->
                LineupMemberCard(
                    icon = Icons.Default.SportsSoccer,
                    role = "Jugador",
                    fullName = "${player.firstName} ${player.lastName}",
                    documentNumber = player.documentNumber,
                    age = player.age,
                    positionLabel = player.position?.label,
                    documentFrontUrl = player.documentFrontUrl,
                    documentBackUrl = player.documentBackUrl
                )
            }
        }
    }
}

@Composable
private fun LineupDetailHeader(
    matchDate: MatchDateResponse,
    clubName: String,
    lineup: LineupResponse
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = clubName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                LineupStatusBadge(status = lineup.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${matchDate.name} · ${matchDate.date}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                LineupHeaderCount(
                    label = "Jugadores",
                    value = lineup.players.size.toString(),
                    modifier = Modifier.weight(1f)
                )
                LineupHeaderCount(
                    label = "Entrenador",
                    value = if (lineup.coach != null) "Sí" else "No",
                    modifier = Modifier.weight(1f)
                )
                LineupHeaderCount(
                    label = "P. Físico",
                    value = if (lineup.physicalTrainer != null) "Sí" else "No",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LineupHeaderCount(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
private fun LineupDetailSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 4.dp)
    )
}

/**
 * Ficha de un integrante de la planilla: datos personales principales
 * y, al expandirlo, las imágenes de sus documentos.
 */
@Composable
private fun LineupMemberCard(
    icon: ImageVector,
    role: String,
    fullName: String,
    documentNumber: String,
    age: Int,
    positionLabel: String?,
    documentFrontUrl: String?,
    documentBackUrl: String?,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val hasDocuments = !documentFrontUrl.isNullOrBlank() || !documentBackUrl.isNullOrBlank()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = hasDocuments) { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = listOfNotNull(role, positionLabel).joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (hasDocuments) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = if (expanded) "Ocultar documentos" else "Ver documentos",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.rotate(if (expanded) 180f else 0f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(label = "DNI", value = documentNumber.ifBlank { "No registrado" })
            InfoRow(label = "Edad", value = "$age años")

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    DocumentSection(
                        label = "DNI Frontal",
                        url = documentFrontUrl
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DocumentSection(
                        label = "DNI Posterior",
                        url = documentBackUrl
                    )
                }
            }
        }
    }
}

@Composable
private fun DocumentSection(
    label: String,
    url: String?
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(6.dp))
    if (url.isNullOrBlank()) {
        DniPhotoPlaceholder()
    } else {
        AsyncImage(
            model = url,
            contentDescription = label,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Composable
private fun EmptyLineupSection(clubName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$clubName aún no registró su plantilla",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private val previewClub = ClubResponse(
    id = 1,
    name = "Club Deportivo Estrella",
    foundedYear = 1950,
    createdBy = "Admin",
    isActive = true
)

private val previewMatchDate = MatchDateResponse(
    id = 1,
    date = "2026-03-15",
    status = MatchDateStatus.ONGOING,
    name = "Fecha 1"
)

private val previewLineup = LineupResponse(
    id = 1,
    matchId = 1,
    clubId = 1,
    status = LineupStatus.CLOSE,
    createdAt = LocalDateTime(2026, 3, 10, 10, 0),
    updatedAt = LocalDateTime(2026, 3, 12, 15, 30),
    coach = LineupCoachResponse(
        coachId = 10,
        firstName = "Roberto",
        lastName = "Díaz",
        documentNumber = "11111111",
        age = 45,
        dateOfBirth = "1980-03-12",
        documentFrontUrl = "https://ciudadania.pe/actividades-interactivas/static/media/ordenar8.155d0398e5f8867aac02.png"
    ),
    physicalTrainer = LineupPhysicalTrainerResponse(
        physicalTrainerId = 20,
        firstName = "Ana",
        lastName = "López",
        documentNumber = "22222222",
        age = 38,
        dateOfBirth = "1987-09-30"
    ),
    players = listOf(
        LineupPlayerResponse(
            playerId = 1,
            firstName = "Juan",
            lastName = "Pérez",
            documentNumber = "12345678",
            age = 25,
            dateOfBirth = "2000-01-15",
            position = PlayerPosition.GOALKEEPER,
            documentFrontUrl = "https://ciudadania.pe/actividades-interactivas/static/media/ordenar8.155d0398e5f8867aac02.png",
            documentBackUrl = "https://ciudadania.pe/actividades-interactivas/static/media/ordenar8.155d0398e5f8867aac02.png"
        ),
        LineupPlayerResponse(
            playerId = 2,
            firstName = "Carlos",
            lastName = "Gómez",
            documentNumber = "23456789",
            age = 27,
            dateOfBirth = "1998-04-10",
            position = PlayerPosition.DEFENDER
        )
    )
)

@Preview
@Composable
private fun LineupDetailScreenPreview() {
    MaterialTheme {
        LineupDetailScreen(
            title = "Mi plantilla",
            matchDate = previewMatchDate,
            clubName = previewClub.name,
            uiState = LineupDetailUiState.Success(previewLineup),
            onBack = {},
            onTabSelected = {}
        )
    }
}

@Preview
@Composable
private fun LineupDetailScreenEmptyPreview() {
    MaterialTheme {
        LineupDetailScreen(
            title = "Plantilla rival",
            matchDate = previewMatchDate,
            clubName = "Club Atlético Rival",
            uiState = LineupDetailUiState.Success(null),
            onBack = {},
            onTabSelected = {}
        )
    }
}

@Preview
@Composable
private fun LineupDetailScreenLoadingPreview() {
    MaterialTheme {
        LineupDetailScreen(
            title = "Mi plantilla",
            matchDate = previewMatchDate,
            clubName = previewClub.name,
            uiState = LineupDetailUiState.Loading,
            onBack = {},
            onTabSelected = {}
        )
    }
}

@Preview
@Composable
private fun LineupDetailScreenErrorPreview() {
    MaterialTheme {
        LineupDetailScreen(
            title = "Mi plantilla",
            matchDate = previewMatchDate,
            clubName = previewClub.name,
            uiState = LineupDetailUiState.Error("Error al cargar la plantilla"),
            onBack = {},
            onTabSelected = {}
        )
    }
}
