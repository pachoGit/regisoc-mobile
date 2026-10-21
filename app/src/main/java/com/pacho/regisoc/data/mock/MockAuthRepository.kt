package com.pacho.regisoc.data.mock

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.auth.ClubInfoResponse
import com.pacho.regisoc.data.dto.auth.LoginRequest
import com.pacho.regisoc.data.dto.auth.LoginResponse
import com.pacho.regisoc.data.dto.auth.MeResponse
import com.pacho.regisoc.domain.repository.AuthRepository

class MockAuthRepository : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return Result.Success(
            LoginResponse(
                token = "mock-token-12345",
                userId = 1L,
                username = request.username,
                role = "CLUB_MANAGER",
                clubId = 1L
            )
        )
    }

    override suspend fun me(): Result<MeResponse> {
        return Result.Success(
            MeResponse(
                id = 1L,
                name = "Usuario",
                surname = "Mock",
                documentNumber = "12345678",
                username = "admin",
                role = "CLUB_MANAGER",
                club = ClubInfoResponse(id = 1L, name = "Club Mock")
            )
        )
    }
}
