package com.pacho.regisoc.ui.features.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.CoachResponse

@Composable
fun CoachListScreen(
    uiState: CoachUiState,
    onLoad: () -> Unit,
    onAddCoach: () -> Unit,
    onEditCoach: (CoachResponse) -> Unit,
    onDeleteCoach: (Long) -> Unit,
    onViewCoach: (CoachResponse) -> Unit
) {
    LaunchedEffect(Unit) {
        onLoad()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCoach,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar entrenador"
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
                is CoachUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CoachUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is CoachUiState.Success -> {
                    val coaches = uiState.coaches
                    if (coaches.isEmpty()) {
                        Text(
                            text = "No hay entrenadores registrados",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(coaches, key = { it.id }) { coach ->
                                CoachCard(
                                    coach = coach,
                                    onEdit = { onEditCoach(coach) },
                                    onDelete = { onDeleteCoach(coach.id) },
                                    onView = { onViewCoach(coach) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoachCard(
    coach: CoachResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de eliminar a ${coach.firstName} ${coach.lastName}?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

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
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${coach.firstName} ${coach.lastName}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "DNI: ${coach.documentNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Edad: ${coach.age} años",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            showMenu = false
                            onEdit()
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
}

private val mockCoaches = listOf(
    CoachResponse(id = 1, clubId = 1, firstName = "Carlos", lastName = "López", documentNumber = "12345678", age = 35, dateOfBirth = "1990-03-10"),
    CoachResponse(id = 2, clubId = 1, firstName = "Ana", lastName = "Martínez", documentNumber = "87654321", age = 40, dateOfBirth = "1985-07-22")
)

@Preview
@Composable
private fun CoachListScreenSuccessPreview() {
    MaterialTheme {
        CoachListScreen(
            uiState = CoachUiState.Success(mockCoaches),
            onLoad = {},
            onAddCoach = {},
            onEditCoach = {},
            onDeleteCoach = {},
            onViewCoach = {}
        )
    }
}

@Preview
@Composable
private fun CoachListScreenEmptyPreview() {
    MaterialTheme {
        CoachListScreen(
            uiState = CoachUiState.Success(emptyList()),
            onLoad = {},
            onAddCoach = {},
            onEditCoach = {},
            onDeleteCoach = {},
            onViewCoach = {}
        )
    }
}

@Preview
@Composable
private fun CoachListScreenErrorPreview() {
    MaterialTheme {
        CoachListScreen(
            uiState = CoachUiState.Error("Error al cargar entrenadores"),
            onLoad = {},
            onAddCoach = {},
            onEditCoach = {},
            onDeleteCoach = {},
            onViewCoach = {}
        )
    }
}

@Preview
@Composable
private fun CoachListScreenLoadingPreview() {
    MaterialTheme {
        CoachListScreen(
            uiState = CoachUiState.Loading,
            onLoad = {},
            onAddCoach = {},
            onEditCoach = {},
            onDeleteCoach = {},
            onViewCoach = {}
        )
    }
}
