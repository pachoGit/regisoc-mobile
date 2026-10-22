package com.pacho.regisoc.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Boton de cuenta de la barra superior: muestra el escudo del club y despliega
 * las opciones "Sobre mi" y "Cerrar sesion".
 */
@Composable
fun AccountMenuButton(
    crestUrl: String?,
    userName: String,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(40.dp)
        ) {
            ClubAvatar(
                crestUrl = crestUrl,
                contentDescription = "Menu de cuenta"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (userName.isNotBlank()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    enabled = false,
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
            }
            DropdownMenuItem(
                text = { Text("Sobre mi") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                },
                onClick = {
                    expanded = false
                    onAboutClick()
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Cerrar sesion",
                        color = MaterialTheme.colorScheme.error
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    expanded = false
                    onLogoutClick()
                }
            )
        }
    }
}

@Preview
@Composable
private fun AccountMenuButtonPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AccountMenuButton(
                crestUrl = null,
                userName = "Juan Perez",
                onAboutClick = {},
                onLogoutClick = {}
            )
        }
    }
}
