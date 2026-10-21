package com.pacho.regisoc.data

import com.pacho.regisoc.data.dto.auth.LoginRequest
import com.pacho.regisoc.data.dto.auth.LoginResponse
import com.pacho.regisoc.data.dto.auth.MeResponse
import com.pacho.regisoc.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import com.pacho.regisoc.core.Result

class AuthApiService(
    private val client: HttpClient,
    private val baseUrl: String
): AuthRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = client.post {
                url(baseUrl + "/login")
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (!response.status.isSuccess()) {
                return Result.Error("Error al iniciar sesion: ${response.status}")
            }

            Result.Success(response.body<LoginResponse>())
        } catch (e: Exception) {
            Result.Error("Error al iniciar sesion: ${e.message}", e)
        }
    }

    override suspend fun me(): Result<MeResponse> {
        return try {
            val response = client.get {
                url("$baseUrl/me")
            }

            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener la informacion: ${response.status}")
            }

            Result.Success(response.body<MeResponse>())
        } catch (e: Exception) {
            Result.Error("Error al obtener la informacion: ${e.message}", e)
        }
    }
}