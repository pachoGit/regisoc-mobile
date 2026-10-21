package com.pacho.regisoc.ui.features.event

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pacho.regisoc.data.dto.EventStatus

internal val EventStatus.displayLabel: String
    get() = when (this) {
        EventStatus.ONGOING -> "En curso"
        EventStatus.UPCOMING -> "Próximamente"
        EventStatus.FINISHED -> "Finalizado"
    }

internal val EventStatus.statusColor: Color
    get() = when (this) {
        EventStatus.ONGOING -> Color(0xFF2E7D32)
        EventStatus.UPCOMING -> Color(0xFFED6C02)
        EventStatus.FINISHED -> Color(0xFF757575)
    }

internal val EventStatus.statusIcon: ImageVector
    get() = when (this) {
        EventStatus.ONGOING -> Icons.Default.PlayArrow
        EventStatus.UPCOMING -> Icons.Default.Schedule
        EventStatus.FINISHED -> Icons.Default.CheckCircle
    }

@Composable
internal fun EventStatusBadge(
    status: EventStatus,
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