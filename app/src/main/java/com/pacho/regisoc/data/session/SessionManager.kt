package com.pacho.regisoc.data.session

import com.pacho.regisoc.data.dto.auth.LoginResponse
import com.pacho.regisoc.data.dto.auth.MeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SessionData(
    val token: String = "",
    val userId: Long = 0,
    val username: String = "",
    val role: String = "",
    val clubId: Long = 0,
    val me: MeResponse? = null
)

sealed class SessionState {
    data object LoggedOut : SessionState()
    data object LoggedIn : SessionState()
}

class SessionManager {

    private val _sessionData = MutableStateFlow(SessionData())
    val sessionData: StateFlow<SessionData> = _sessionData.asStateFlow()

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.LoggedOut)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    val isLoggedIn: Boolean
        get() = _sessionState.value is SessionState.LoggedIn

    val token: String
        get() = _sessionData.value.token

    val clubId: Long
        get() = _sessionData.value.clubId

    val role: String
        get() = _sessionData.value.role

    fun saveLoginResponse(response: LoginResponse) {
        val resolvedClubId = requireNotNull(response.clubId) {
            "El clubId no puede ser nulo para el rol ${response.role}"
        }
        _sessionData.update {
            it.copy(
                token = response.token,
                userId = response.userId,
                username = response.username,
                role = response.role,
                clubId = resolvedClubId
            )
        }
        _sessionState.value = SessionState.LoggedIn
    }

    fun saveMeResponse(response: MeResponse) {
        _sessionData.update { it.copy(me = response) }
    }

    fun clearSession() {
        _sessionData.value = SessionData()
        _sessionState.value = SessionState.LoggedOut
    }
}
