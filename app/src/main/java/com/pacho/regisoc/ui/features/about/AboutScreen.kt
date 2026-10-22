package com.pacho.regisoc.ui.features.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.core.AppInfo
import com.pacho.regisoc.ui.components.ClubAvatar
import com.pacho.regisoc.ui.components.InfoRow
import com.pacho.regisoc.ui.components.InfoSection

/**
 * Pantalla "Sobre mi": resume la cuenta del administrador, su club y la app.
 * Stateless para poder previsualizarse sin sesion ni VM.
 */
@Composable
fun AboutScreen(
    model: AboutUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        AccountHeader(model)

        Spacer(Modifier.height(24.dp))

        InfoSection(title = "Mi cuenta") {
            InfoRow(label = "Nombre", value = model.fullName)
            InfoRow(label = "Usuario", value = model.username)
            InfoRow(label = "Documento", value = model.documentNumber)
        }

        Spacer(Modifier.height(16.dp))

        InfoSection(title = "Mi club") {
            ClubRow(model)
        }

        Spacer(Modifier.height(16.dp))

        InfoSection(title = "La aplicación") {
            InfoRow(label = "Nombre", value = AppInfo.NAME)
            InfoRow(label = "Versión", value = AppInfo.VERSION)
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun AccountHeader(model: AboutUiModel) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ClubAvatar(
                crestUrl = model.clubCrestUrl,
                size = 96.dp,
                contentDescription = "Escudo del club"
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = model.fullName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "@${model.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
            )

            Spacer(Modifier.height(12.dp))

            AssistChip(
                onClick = {},
                enabled = false,
                label = { Text(model.roleLabel) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
private fun ClubRow(model: AboutUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.SportsSoccer,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = model.clubName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutScreenPreview() {
    MaterialTheme {
        AboutScreen(
            model = AboutUiModel(
                fullName = "Juan Perez",
                username = "jperez",
                roleLabel = "Administrador de club",
                documentNumber = "12345678",
                clubName = "Club Deportivo Estrella",
                clubCrestUrl = "https://example.com/crest.png"
            )
        )
    }
}

@Preview(showBackground = true, name = "Sin foto de club")
@Composable
private fun AboutScreenWithoutCrestPreview() {
    MaterialTheme {
        AboutScreen(
            model = AboutUiModel(
                fullName = "Ana Gomez",
                username = "agomez",
                roleLabel = "Administrador de club",
                documentNumber = "87654321",
                clubName = "Sin club asignado",
                clubCrestUrl = null
            )
        )
    }
}
