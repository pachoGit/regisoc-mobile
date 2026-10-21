package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.auth.LoginRequest
import com.pacho.regisoc.data.dto.auth.LoginResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(username: String, password: String): Result<LoginResponse> {
        val request = LoginRequest(username = username, password = password)
        val result = authRepository.login(request)

        if (result is Result.Success) {
            try {
                sessionManager.saveLoginResponse(result.data)
            }
            catch(_: Exception) {
                return Result.Error("Error al iniciar sesion: Este usuario no puede acceder desde esta plataforma")
            }
            val meResult = authRepository.me()
            if (meResult is Result.Success) {
                sessionManager.saveMeResponse(meResult.data)
            }
        }

        return result
    }
}
