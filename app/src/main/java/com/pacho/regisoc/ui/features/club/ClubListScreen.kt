package com.pacho.regisoc.ui.features.club

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
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.ui.components.ClubCard

@Composable
fun ClubListScreen(
    uiState: ClubUiState,
    onAddClub: () -> Unit,
    onEditClub: (ClubResponse) -> Unit,
    onDeleteClub: (Long) -> Unit,
    onViewClub: (ClubResponse) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClub,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar club"
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
                is ClubUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ClubUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ClubUiState.Success -> {
                    val clubs = uiState.clubs
                    if (clubs.isEmpty()) {
                        Text(
                            text = "No hay clubs registrados",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(clubs, key = { it.id }) { club ->
                                ClubCard(
                                    club = club,
                                    onEdit = { onEditClub(club) },
                                    onDelete = { onDeleteClub(club.id) },
                                    onView = { onViewClub(club) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val mockClubs = listOf(
    ClubResponse(id = 1, name = "Club Deportivo Estrella", foundedYear = 1950, description = "Club histórico de la ciudad"),
    ClubResponse(id = 2, name = "Atlético Nacional", foundedYear = 1965, description = "Club con gran tradición")
)

@Preview
@Composable
private fun ClubListScreenSuccessPreview() {
    MaterialTheme {
        ClubListScreen(
            uiState = ClubUiState.Success(mockClubs),
            onAddClub = {},
            onEditClub = {},
            onDeleteClub = {},
            onViewClub = {}
        )
    }
}

@Preview
@Composable
private fun ClubListScreenEmptyPreview() {
    MaterialTheme {
        ClubListScreen(
            uiState = ClubUiState.Success(emptyList()),
            onAddClub = {},
            onEditClub = {},
            onDeleteClub = {},
            onViewClub = {}
        )
    }
}

@Preview
@Composable
private fun ClubListScreenErrorPreview() {
    MaterialTheme {
        ClubListScreen(
            uiState = ClubUiState.Error("Error al cargar clubs"),
            onAddClub = {},
            onEditClub = {},
            onDeleteClub = {},
            onViewClub = {}
        )
    }
}

@Preview
@Composable
private fun ClubListScreenLoadingPreview() {
    MaterialTheme {
        ClubListScreen(
            uiState = ClubUiState.Loading,
            onAddClub = {},
            onEditClub = {},
            onDeleteClub = {},
            onViewClub = {}
        )
    }
}
