package com.pacho.regisoc.ui.features.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.ui.components.PlayerCard

@Composable
fun PlayerListScreen(
    uiState: PlayerUiState,
    onLoad: () -> Unit,
    onAddPlayer: () -> Unit,
    onEditPlayer: (PlayerResponse) -> Unit,
    onDeletePlayer: (Long) -> Unit,
    onViewPlayer: (PlayerResponse) -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlayer,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar jugador"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is PlayerUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PlayerUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is PlayerUiState.Success -> {
                    val players = uiState.players
                    if (players.isEmpty()) {
                        Text(
                            text = "No hay jugadores registrados",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(players, key = { it.id }) { player ->
                                PlayerCard(
                                    player = player,
                                    onEdit = { onEditPlayer(player) },
                                    onDelete = { onDeletePlayer(player.id) },
                                    onView = { onViewPlayer(player) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val mockPlayers = listOf(
    PlayerResponse(id = 1, clubId = 1, firstName = "Juan", lastName = "Pérez", documentNumber = "12345678", age = 25, dateOfBirth = "2000-01-15"),
    PlayerResponse(id = 2, clubId = 1, firstName = "María", lastName = "García", documentNumber = "87654321", age = 28, dateOfBirth = "1997-05-20")
)

@Preview
@Composable
private fun PlayerListScreenSuccessPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Success(mockPlayers),
            onLoad = {},
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}

@Preview
@Composable
private fun PlayerListScreenEmptyPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Success(emptyList()),
            onLoad = {},
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}

@Preview
@Composable
private fun PlayerListScreenErrorPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Error("Error al cargar jugadores"),
            onLoad = {},
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}

@Preview
@Composable
private fun PlayerListScreenLoadingPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Loading,
            onLoad = {},
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}
