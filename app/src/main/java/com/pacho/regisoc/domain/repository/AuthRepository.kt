package com.pacho.regisoc.domain.repository

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.auth.LoginRequest
import com.pacho.regisoc.data.dto.auth.LoginResponse
import com.pacho.regisoc.data.dto.auth.MeResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>

    suspend fun me(): Result<MeResponse>
}