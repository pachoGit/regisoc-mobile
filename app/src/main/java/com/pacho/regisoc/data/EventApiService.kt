package com.pacho.regisoc.data

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.EventRequest
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.dto.EventUpdateRequest
import com.pacho.regisoc.domain.repository.EventRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class EventApiService(
    private val client: HttpClient,
    private val baseUrl: String
) : EventRepository {

    override suspend fun getByClub(clubId: Long): Result<List<EventResponse>> {
        return try {
            val response = client.get {
                url("$baseUrl/club")
                parameter("clubId", clubId)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener eventos: ${response.status}")
            }
            val events = response.body<List<EventResponse>>()
            Result.Success(events)
        } catch (e: Exception) {
            Result.Error("Error al obtener eventos: ${e.message}", e)
        }
    }

    override suspend fun getById(id: Long): Result<EventResponse?> {
        return try {
            val response = client.get {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener evento: ${response.status}")
            }
            Result.Success(response.body<EventResponse>())
        } catch (e: Exception) {
            Result.Error("Error al obtener evento: ${e.message}", e)
        }
    }

    override suspend fun createEvent(request: EventRequest): Result<EventResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al crear evento: ${response.status}")
            }
            val created = response.body<EventResponse>()
            Result.Success(created)
        } catch (e: Exception) {
            Result.Error("Error al crear evento: ${e.message}", e)
        }
    }

    override suspend fun updateEvent(id: Long, request: EventUpdateRequest): Result<Unit> {
        return try {
            val response = client.put {
                url("$baseUrl/$id")
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al actualizar evento: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al actualizar evento: ${e.message}", e)
        }
    }

    override suspend fun deleteEvent(id: Long): Result<Unit> {
        return try {
            val response = client.delete {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al eliminar evento: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al eliminar evento: ${e.message}", e)
        }
    }
}