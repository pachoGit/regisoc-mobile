package com.pacho.regisoc.ui.features.lineup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.data.dto.LineupStatus
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus
import com.pacho.regisoc.data.dto.MatchResponse
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.data.dto.PlayerPosition
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.domain.validation.LineupValidator
import com.pacho.regisoc.ui.layouts.MainLayout

@Composable
fun LineupScreen(
    matchDate: MatchDateResponse,
    uiState: LineupUiState,
    players: List<PlayerResponse>,
    coaches: List<CoachResponse>,
    physicalTrainers: List<PhysicalTrainerResponse>,
    searchQuery: String,
    isSaving: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onTogglePlayer: (Long) -> Unit,
    onSelectCoach: (Long?) -> Unit,
    onSelectPhysicalTrainer: (Long?) -> Unit,
    onSaveDraft: () -> Unit,
    onCloseLineup: () -> Unit,
    onBack: () -> Unit,
    onTabSelected: (Int) -> Unit,
    snackbarHost: @Composable () -> Unit
) {
    MainLayout(
        title = "Alineación",
        onBackClick = onBack,
        selectedTab = 1,
        onTabSelected = onTabSelected,
        snackbarHost = snackbarHost
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is LineupUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is LineupUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
                is LineupUiState.Success -> {
                    LineupEditor(
                        matchDate = matchDate,
                        state = uiState,
                        players = players,
                        coaches = coaches,
                        physicalTrainers = physicalTrainers,
                        searchQuery = searchQuery,
                        isSaving = isSaving,
                        onSearchQueryChange = onSearchQueryChange,
                        onTogglePlayer = onTogglePlayer,
                        onSelectCoach = onSelectCoach,
                        onSelectPhysicalTrainer = onSelectPhysicalTrainer,
                        onSaveDraft = onSaveDraft,
                        onCloseLineup = onCloseLineup
                    )
                }
            }
        }
    }
}

@Composable
private fun LineupEditor(
    matchDate: MatchDateResponse,
    state: LineupUiState.Success,
    players: List<PlayerResponse>,
    coaches: List<CoachResponse>,
    physicalTrainers: List<PhysicalTrainerResponse>,
    searchQuery: String,
    isSaving: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onTogglePlayer: (Long) -> Unit,
    onSelectCoach: (Long?) -> Unit,
    onSelectPhysicalTrainer: (Long?) -> Unit,
    onSaveDraft: () -> Unit,
    onCloseLineup: () -> Unit
) {
    val editable = !state.isClosed
    val coachOptions = coaches.map { LineupMemberOption(it.id, "${it.firstName} ${it.lastName}") }
    val trainerOptions = physicalTrainers.map { LineupMemberOption(it.id, "${it.firstName} ${it.lastName}") }
    val filteredPlayers = remember(searchQuery, players) {
        if (searchQuery.isBlank()) {
            players
        } else {
            val query = searchQuery.trim()
            players.filter { "${it.firstName} ${it.lastName}".contains(query, ignoreCase = true) }
        }
    }
    var showCloseDialog by remember { mutableStateOf(false) }

    if (showCloseDialog) {
        AlertDialog(
            onDismissRequest = { showCloseDialog = false },
            title = { Text("Cerrar planilla") },
            text = { Text("Al cerrar la planilla dejará de ser editable. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCloseDialog = false
                        onCloseLineup()
                    }
                ) {
                    Text("Cerrar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCloseDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LineupHeaderCard(
            matchDate = matchDate,
            state = state,
            editable = editable
        )

        if (!editable) {
            ClosedBanner()
        }

        LineupSectionTitle(text = "Cuerpo técnico")
        LineupStaffSelector(
            label = "Entrenador",
            icon = Icons.Default.Face,
            options = coachOptions,
            selectedId = state.coachId,
            enabled = editable,
            onSelect = onSelectCoach
        )
        LineupStaffSelector(
            label = "Preparador físico",
            icon = Icons.Default.Favorite,
            options = trainerOptions,
            selectedId = state.physicalTrainerId,
            enabled = editable,
            onSelect = onSelectPhysicalTrainer
        )

        LineupSectionTitle(text = "Jugadores (${state.playerIds.size}/${LineupValidator.MAX_PLAYERS})")

        if (editable) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar jugador...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
        }

        if (filteredPlayers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isBlank()) "No hay jugadores disponibles" else "Sin resultados para \"$searchQuery\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            filteredPlayers.forEach { player ->
                val selected = player.id in state.playerIds
                val canSelectMore = state.playerIds.size < LineupValidator.MAX_PLAYERS
                LineupPlayerRow(
                    player = player,
                    selected = selected,
                    enabled = editable && (selected || canSelectMore),
                    onToggle = { onTogglePlayer(player.id) }
                )
            }
        }

        if (editable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onSaveDraft,
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar borrador")
                }
                Button(
                    onClick = { showCloseDialog = true },
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar planilla")
                    }
                }
            }
        }
    }
}

@Composable
private fun LineupHeaderCard(
    matchDate: MatchDateResponse,
    state: LineupUiState.Success,
    editable: Boolean
) {
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Planilla del partido",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                LineupStatusBadge(
                    status = if (editable) LineupStatus.OPEN else LineupStatus.CLOSE
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${matchDate.name} · ${matchDate.date}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                HeaderCount(
                    label = "Jugadores",
                    value = "${state.playerIds.size}/${LineupValidator.MAX_PLAYERS}",
                    modifier = Modifier.weight(1f)
                )
                HeaderCount(
                    label = "Entrenador",
                    value = if (state.coachId != null) "Sí" else "No",
                    modifier = Modifier.weight(1f)
                )
                HeaderCount(
                    label = "P. Físico",
                    value = if (state.physicalTrainerId != null) "Sí" else "No",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HeaderCount(
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
private fun ClosedBanner() {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Planilla cerrada: ya no se puede editar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LineupSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 4.dp)
    )
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
    scheduledTime = "2026-03-16T16:00:00"
)

private val previewMatchDate = MatchDateResponse(
    id = 1,
    date = "2026-03-15",
    status = MatchDateStatus.UPCOMING,
    name = "Fecha 1",
    match = previewMatch
)

private val previewPlayers = listOf(
    PlayerResponse(
        id = 1, clubId = 1, firstName = "Juan", lastName = "Pérez",
        documentNumber = "12345678", age = 25, dateOfBirth = "2000-01-15",
        position = PlayerPosition.GOALKEEPER
    ),
    PlayerResponse(
        id = 2, clubId = 1, firstName = "Carlos", lastName = "Gómez",
        documentNumber = "23456789", age = 27, dateOfBirth = "1998-04-10",
        position = PlayerPosition.DEFENDER
    ),
    PlayerResponse(
        id = 3, clubId = 1, firstName = "Luis", lastName = "Fernández",
        documentNumber = "34567890", age = 24, dateOfBirth = "2001-07-22",
        position = PlayerPosition.MIDFIELDER
    ),
    PlayerResponse(
        id = 4, clubId = 1, firstName = "Miguel", lastName = "Torres",
        documentNumber = "45678901", age = 23, dateOfBirth = "2002-11-05",
        position = PlayerPosition.FORWARD
    )
)

private val previewCoaches = listOf(
    CoachResponse(
        id = 10, clubId = 1, firstName = "Roberto", lastName = "Díaz",
        documentNumber = "11111111", age = 45, dateOfBirth = "1980-03-12"
    )
)

private val previewPhysicalTrainers = listOf(
    PhysicalTrainerResponse(
        id = 20, clubId = 1, firstName = "Ana", lastName = "López",
        documentNumber = "22222222", age = 38, dateOfBirth = "1987-09-30"
    )
)

private val previewEditableState = LineupUiState.Success(
    lineupId = 1,
    isClosed = false,
    playerIds = setOf(1, 2),
    coachId = 10,
    physicalTrainerId = 20
)

private val previewClosedState = LineupUiState.Success(
    lineupId = 1,
    isClosed = true,
    playerIds = setOf(1, 2, 3, 4),
    coachId = 10,
    physicalTrainerId = null
)

@Preview
@Composable
private fun LineupScreenEditablePreview() {
    MaterialTheme {
        LineupScreen(
            matchDate = previewMatchDate,
            uiState = previewEditableState,
            players = previewPlayers,
            coaches = previewCoaches,
            physicalTrainers = previewPhysicalTrainers,
            searchQuery = "",
            isSaving = false,
            onSearchQueryChange = {},
            onTogglePlayer = {},
            onSelectCoach = {},
            onSelectPhysicalTrainer = {},
            onSaveDraft = {},
            onCloseLineup = {},
            onBack = {},
            onTabSelected = {},
            snackbarHost = {}
        )
    }
}

@Preview
@Composable
private fun LineupScreenClosedPreview() {
    MaterialTheme {
        LineupScreen(
            matchDate = previewMatchDate,
            uiState = previewClosedState,
            players = previewPlayers,
            coaches = previewCoaches,
            physicalTrainers = previewPhysicalTrainers,
            searchQuery = "",
            isSaving = false,
            onSearchQueryChange = {},
            onTogglePlayer = {},
            onSelectCoach = {},
            onSelectPhysicalTrainer = {},
            onSaveDraft = {},
            onCloseLineup = {},
            onBack = {},
            onTabSelected = {},
            snackbarHost = {}
        )
    }
}

@Preview
@Composable
private fun LineupScreenLoadingPreview() {
    MaterialTheme {
        LineupScreen(
            matchDate = previewMatchDate,
            uiState = LineupUiState.Loading,
            players = previewPlayers,
            coaches = previewCoaches,
            physicalTrainers = previewPhysicalTrainers,
            searchQuery = "",
            isSaving = false,
            onSearchQueryChange = {},
            onTogglePlayer = {},
            onSelectCoach = {},
            onSelectPhysicalTrainer = {},
            onSaveDraft = {},
            onCloseLineup = {},
            onBack = {},
            onTabSelected = {},
            snackbarHost = {}
        )
    }
}

@Preview
@Composable
private fun LineupScreenErrorPreview() {
    MaterialTheme {
        LineupScreen(
            matchDate = previewMatchDate,
            uiState = LineupUiState.Error("Error al cargar la planilla"),
            players = previewPlayers,
            coaches = previewCoaches,
            physicalTrainers = previewPhysicalTrainers,
            searchQuery = "",
            isSaving = false,
            onSearchQueryChange = {},
            onTogglePlayer = {},
            onSelectCoach = {},
            onSelectPhysicalTrainer = {},
            onSaveDraft = {},
            onCloseLineup = {},
            onBack = {},
            onTabSelected = {},
            snackbarHost = {}
        )
    }
}