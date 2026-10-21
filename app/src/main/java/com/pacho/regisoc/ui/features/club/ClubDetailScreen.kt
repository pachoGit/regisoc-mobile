package com.pacho.regisoc.ui.features.club

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.ui.components.InfoRow
import com.pacho.regisoc.ui.components.InfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubDetailScreen(
    club: ClubResponse,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Club") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = club.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(title = "Información del Club") {
                InfoRow(label = "Nombre", value = club.name)
                InfoRow(label = "Año de Fundación", value = club.foundedYear?.toString() ?: "No registrado")
                InfoRow(label = "Creado por", value = club.createdBy)
                InfoRow(label = "Estado", value = if (club.isActive) "Activo" else "Inactivo")
            }

            if (!club.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                InfoSection(title = "Descripción") {
                    Text(
                        text = club.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            InfoSection(title = "Información del Sistema") {
                InfoRow(label = "Creado", value = club.createdAt)
                InfoRow(label = "Actualizado", value = club.updatedAt)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private val previewClub = ClubResponse(
    id = 1,
    name = "Club Deportivo Estrella",
    foundedYear = 1950,
    description = "Club histórico de la ciudad fundado en 1950",
    createdBy = "Admin",
    isActive = true,
    createdAt = "2024-01-15T10:30:00",
    updatedAt = "2024-06-20T14:45:00"
)

@Preview
@Composable
private fun ClubDetailScreenPreview() {
    MaterialTheme {
        ClubDetailScreen(
            club = previewClub,
            onBack = {}
        )
    }
}
