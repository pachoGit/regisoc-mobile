package com.pacho.regisoc.ui.features.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pacho.regisoc.data.dto.auth.ClubInfoResponse
import com.pacho.regisoc.data.dto.auth.LoginResponse
import com.pacho.regisoc.data.dto.auth.MeResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    sessionManager: SessionManager,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            navigator.navigateTo(Screen.Home)
        }
    }

    LoginScreen(
        uiState = uiState,
        formState = formState,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onMockModeToggle = viewModel::onMockModeToggle,
        onLogin = {
            if (formState.isMockMode) {
                sessionManager.saveLoginResponse(
                    LoginResponse(
                        token = "mock-token-12345",
                        userId = 1L,
                        username = formState.username.ifBlank { "admin" },
                        role = "CLUB_MANAGER",
                        clubId = 1L
                    )
                )
                sessionManager.saveMeResponse(
                    MeResponse(
                        id = 1L,
                        name = "Usuario",
                        surname = "Mock",
                        documentNumber = "12345678",
                        username = formState.username.ifBlank { "admin" },
                        role = "CLUB_MANAGER",
                        club = ClubInfoResponse(id = 1L, name = "Club Mock")
                    )
                )
                viewModel.onMockLoginSuccess()
            } else {
                viewModel.login()
            }
        },
        snackbarHost = snackbarHost
    )
}
