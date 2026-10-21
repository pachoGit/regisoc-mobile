package com.pacho.regisoc.data

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.LineupResponse
import com.pacho.regisoc.data.dto.SetLineupRequest
import com.pacho.regisoc.domain.repository.LineupRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class LineupApiService(
    private val client: HttpClient,
    private val baseUrl: String
) : LineupRepository {

    override suspend fun getByMatch(matchId: Long, clubId: Long): Result<LineupResponse?> {
        return try {
            val response = client.get {
                url("$baseUrl/match/$matchId/club/$clubId")
            }
            if (response.status == HttpStatusCode.NotFound) {
                return Result.Success(null)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener planilla: ${response.status}")
            }
            Result.Success(response.body<LineupResponse>())
        } catch (e: Exception) {
            Result.Error("Error al obtener planilla: ${e.message}", e)
        }
    }

    override suspend fun create(request: SetLineupRequest): Result<LineupResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al guardar planilla: ${response.status}")
            }
            Result.Success(response.body<LineupResponse>())
        } catch (e: Exception) {
            Result.Error("Error al guardar planilla: ${e.message}", e)
        }
    }

    override suspend fun update(id: Long, request: SetLineupRequest): Result<LineupResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al actualizar planilla: ${response.status}")
            }
            Result.Success(response.body<LineupResponse>())
        } catch (e: Exception) {
            Result.Error("Error al actualizar planilla: ${e.message}", e)
        }
    }

    override suspend fun close(id: Long): Result<Unit> {
        return try {
            val response = client.put {
                url("$baseUrl/close/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al cerrar planilla: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al cerrar planilla: ${e.message}", e)
        }
    }
}
