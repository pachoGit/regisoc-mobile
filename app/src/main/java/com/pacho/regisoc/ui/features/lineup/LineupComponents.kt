package com.pacho.regisoc.ui.features.lineup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.LineupStatus
import com.pacho.regisoc.data.dto.PlayerPosition
import com.pacho.regisoc.data.dto.PlayerResponse

internal val LineupStatus.displayLabel: String
    get() = when (this) {
        LineupStatus.OPEN -> "Abierta"
        LineupStatus.CLOSE -> "Cerrada"
    }

internal val LineupStatus.statusColor: Color
    get() = when (this) {
        LineupStatus.OPEN -> Color(0xFFF57F17)
        LineupStatus.CLOSE -> Color(0xFF2E7D32)
    }

internal val LineupStatus.statusIcon: ImageVector
    get() = when (this) {
        LineupStatus.OPEN -> Icons.Default.Edit
        LineupStatus.CLOSE -> Icons.Default.Lock
    }

internal val PlayerPosition.label: String
    get() = when (this) {
        PlayerPosition.GOALKEEPER -> "Arquero"
        PlayerPosition.DEFENDER -> "Defensor"
        PlayerPosition.MIDFIELDER -> "Mediocampista"
        PlayerPosition.FORWARD -> "Delantero"
    }

internal data class LineupMemberOption(
    val id: Long,
    val name: String
)

@Composable
internal fun LineupStatusBadge(
    status: LineupStatus,
    modifier: Modifier = Modifier
) {
    val color = status.statusColor
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.12f),
        contentColor = color
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = status.statusIcon,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.displayLabel,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
internal fun LineupStaffSelector(
    label: String,
    icon: ImageVector,
    options: List<LineupMemberOption>,
    selectedId: Long?,
    enabled: Boolean,
    onSelect: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = options.firstOrNull { it.id == selectedId }?.name ?: "Ninguno"

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            enabled = enabled,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = selectedName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Ninguno") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                onClick = {
                    onSelect(null)
                    expanded = false
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        onSelect(option.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
internal fun LineupPlayerRow(
    player: PlayerResponse,
    selected: Boolean,
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selected,
                onCheckedChange = { onToggle() },
                enabled = enabled
            )
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
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${player.firstName} ${player.lastName}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = player.position?.label ?: "Sin posición",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun LineupStatusBadgeOpenPreview() {
    MaterialTheme {
        LineupStatusBadge(status = LineupStatus.OPEN)
    }
}

@Preview
@Composable
private fun LineupStatusBadgeClosePreview() {
    MaterialTheme {
        LineupStatusBadge(status = LineupStatus.CLOSE)
    }
}

@Preview
@Composable
private fun LineupStaffSelectorPreview() {
    MaterialTheme {
        LineupStaffSelector(
            label = "Entrenador",
            icon = Icons.Default.Edit,
            options = listOf(
                LineupMemberOption(id = 10, name = "Roberto Díaz"),
                LineupMemberOption(id = 11, name = "Marcos Silva")
            ),
            selectedId = 10,
            enabled = true,
            onSelect = {}
        )
    }
}

@Preview
@Composable
private fun LineupPlayerRowSelectedPreview() {
    MaterialTheme {
        LineupPlayerRow(
            player = PlayerResponse(
                id = 1, clubId = 1, firstName = "Juan", lastName = "Pérez",
                documentNumber = "12345678", age = 25, dateOfBirth = "2000-01-15",
                position = PlayerPosition.GOALKEEPER
            ),
            selected = true,
            enabled = true,
            onToggle = {}
        )
    }
}

@Preview
@Composable
private fun LineupPlayerRowUnselectedPreview() {
    MaterialTheme {
        LineupPlayerRow(
            player = PlayerResponse(
                id = 2, clubId = 1, firstName = "Carlos", lastName = "Gómez",
                documentNumber = "23456789", age = 27, dateOfBirth = "1998-04-10",
                position = PlayerPosition.DEFENDER
            ),
            selected = false,
            enabled = true,
            onToggle = {}
        )
    }
}