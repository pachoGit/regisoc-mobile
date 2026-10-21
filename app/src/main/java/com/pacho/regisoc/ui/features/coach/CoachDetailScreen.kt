package com.pacho.regisoc.ui.features.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.ui.components.DniPhotoPlaceholder
import com.pacho.regisoc.ui.components.InfoRow
import com.pacho.regisoc.ui.components.InfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachDetailScreen(
    coach: CoachResponse,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Entrenador") },
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
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${coach.firstName} ${coach.lastName}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(title = "Datos Personales") {
                InfoRow(label = "DNI", value = coach.documentNumber)
                InfoRow(label = "Edad", value = "${coach.age} años")
                InfoRow(label = "Fecha de Nacimiento", value = coach.dateOfBirth.ifBlank { "No registrada" })
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "Documentación") {
                Text("DNI Frontal", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                if (coach.documentFrontUrl != null) {
                    PhotoAttachedPlaceholder(url = coach.documentFrontUrl)
                } else {
                    DniPhotoPlaceholder()
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("DNI Posterior", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                if (coach.documentBackUrl != null) {
                    PhotoAttachedPlaceholder(url = coach.documentBackUrl)
                } else {
                    DniPhotoPlaceholder()
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PhotoAttachedPlaceholder(url: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

private val previewCoach = CoachResponse(
    id = 1, clubId = 1,
    firstName = "Carlos", lastName = "López",
    documentNumber = "12345678",
    age = 35, dateOfBirth = "1990-03-10"
)

@Preview
@Composable
private fun CoachDetailScreenPreview() {
    MaterialTheme {
        CoachDetailScreen(
            coach = previewCoach,
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun CoachDetailScreenWithPhotosPreview() {
    MaterialTheme {
        CoachDetailScreen(
            coach = previewCoach.copy(
                photoUrl = "https://example.com/photo.jpg",
                documentFrontUrl = "https://ciudadania.pe/actividades-interactivas/static/media/ordenar8.155d0398e5f8867aac02.png",
                documentBackUrl = "https://ciudadania.pe/actividades-interactivas/static/media/ordenar8.155d0398e5f8867aac02.png"
            ),
            onBack = {}
        )
    }
}
