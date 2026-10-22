package com.pacho.regisoc.ui.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.regisoc.ui.account.LocalAccountActions
import com.pacho.regisoc.ui.components.AccountMenuButton

/**
 * Layout base de las pantallas autenticadas.
 *
 * El boton de cuenta (arriba a la derecha) se alimenta de [LocalAccountActions]:
 * asi el menu "Sobre mi" / "Cerrar sesion" esta disponible en todas las pantallas
 * sin propagar parametros por cada ruta.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    title: String,
    onBackClick: (() -> Unit)? = null,
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val accountActions = LocalAccountActions.current

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar"
                            )
                        }
                    }
                },
                actions = {
                    actions()
                    AccountMenuButton(
                        crestUrl = accountActions.clubCrestUrl,
                        userName = accountActions.userName,
                        onAboutClick = accountActions.onAboutClick,
                        onLogoutClick = accountActions.onLogoutClick,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Eventos") },
                    label = { Text("Eventos") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { onTabSelected(2) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Plantilla") },
                    label = { Text("Plantilla") }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            content(paddingValues)
        }
    }
}

@Preview
@Composable
fun MainLayoutPreview() {
    var currentTab by remember { mutableStateOf(0) }
    
    MaterialTheme {
        MainLayout(
            title = "Appregisoc",
            onBackClick = {},
            selectedTab = currentTab,
            onTabSelected = { currentTab = it }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Contenido de la pestaña ${currentTab + 1}")
            }
        }
    }
}
