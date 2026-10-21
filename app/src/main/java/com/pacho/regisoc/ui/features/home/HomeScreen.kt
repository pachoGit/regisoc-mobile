package com.pacho.regisoc.ui.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.ui.features.club.ClubUiState

@Composable
fun HomeScreen(
    uiState: ClubUiState,
    onViewClub: (ClubResponse) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        WelcomeSection()

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Eventos activos",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (uiState) {
                is ClubUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ClubUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ClubUiState.Success -> {
                    val activeClubs = uiState.clubs.filter { it.isActive }
                    if (activeClubs.isEmpty()) {
                        EmptyEventsSection()
                    } else {
                        ActiveEventsList(activeClubs, onViewClub)
            }
        }
    }
}

        val mockActiveClubs = listOf(
    ClubResponse(id = 1, name = "Torneo Apertura 2026", isActive = true, description = "Torneo de fútbol juvenil"),
    ClubResponse(id = 2, name = "Copa Verano", isActive = true, description = "Competencia interclubes"),
    ClubResponse(id = 3, name = "Liga Regional", isActive = true, description = "Liga de clubes locales")
)

@Preview
@Composable
fun HomeScreenWithEventsPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Success(mockActiveClubs),
            onViewClub = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenEmptyPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Success(emptyList()),
            onViewClub = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenLoadingPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Loading,
            onViewClub = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenErrorPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Error("Error al cargar eventos"),
            onViewClub = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenInactiveClubsPreview() {
    val inactiveClubs = listOf(
        ClubResponse(id = 1, name = "Torneo Pasado", isActive = false)
    )
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Success(inactiveClubs),
            onViewClub = {}
        )
    }
}
    }
}

@Composable
private fun WelcomeSection() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "¡Bienvenido!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Gestiona tu club y jugadores desde un solo lugar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            Icon(
                imageVector = Icons.Default.SportsSoccer,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No hay eventos activos disponibles",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Los eventos activos aparecerán aquí",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ActiveEventsList(
    activeClubs: List<ClubResponse>,
    onViewClub: (ClubResponse) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(activeClubs, key = { it.id }) { club ->
            EventCard(
                club = club,
                onClick = { onViewClub(club) }
            )
        }
    }
}

@Composable
private fun EventCard(
    club: ClubResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = club.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (club.description != null) {
                    Text(
                        text = club.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Activo",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private val mockActiveClubs = listOf(
    ClubResponse(id = 1, name = "Torneo Apertura 2026", isActive = true, description = "Torneo de fútbol juvenil"),
    ClubResponse(id = 2, name = "Copa Verano", isActive = true, description = "Competencia interclubes"),
    ClubResponse(id = 3, name = "Liga Regional", isActive = true, description = "Liga de clubes locales")
)

@Preview
@Composable
private fun HomeScreenWithEventsPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Success(mockActiveClubs),
            onViewClub = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenEmptyPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Success(emptyList()),
            onViewClub = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenLoadingPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Loading,
            onViewClub = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenErrorPreview() {
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Error("Error al cargar eventos"),
            onViewClub = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenInactiveClubsPreview() {
    val inactiveClubs = listOf(
        ClubResponse(id = 1, name = "Torneo Pasado", isActive = false)
    )
    MaterialTheme {
        HomeScreen(
            uiState = ClubUiState.Success(inactiveClubs),
            onViewClub = {}
        )
    }
}
