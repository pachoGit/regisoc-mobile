package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.data.session.SessionManager

class LogoutUseCase(
    private val sessionManager: SessionManager
) {
    operator fun invoke() {
        sessionManager.clearSession()
    }
}
